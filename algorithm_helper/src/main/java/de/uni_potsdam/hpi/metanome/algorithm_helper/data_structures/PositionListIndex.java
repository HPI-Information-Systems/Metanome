/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Position list indices (or stripped partitions) are an index structure that stores the positions
 * of equal values in a nested list. A column with the values a, a, b, c, b, c transfers to the
 * position list index ((0, 1), (2, 4), (3, 5)). Clusters of size 1 are discarded. A position list
 * index should be created using the {@link PLIBuilder}.
 */
public class PositionListIndex {

  protected List<LongArrayList> clusters;
  protected long rawKeyError = -1;

  public PositionListIndex(List<LongArrayList> clusters) {
    this.clusters = clusters;
  }

  /**
   * Constructs an empty {@link PositionListIndex}.
   */
  public PositionListIndex() {
    this.clusters = new ArrayList<>();
  }

  /**
   * Intersects the given PositionListIndex with this PositionListIndex returning a new
   * PositionListIndex. For the intersection the smaller PositionListIndex is converted into a
   * HashMap.
   *
   * @param otherPLI the other {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PositionListIndex}
   *                 to intersect
   * @return the intersected {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PositionListIndex}
   */
  public PositionListIndex intersect(PositionListIndex otherPLI) {
    //TODO Optimize Smaller PLI as Hashmap?
    return calculateIntersection(otherPLI);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    List<LongOpenHashSet> setCluster = convertClustersToSets(clusters);

    Collections.sort(setCluster, new Comparator<LongSet>() {

      @Override
      public int compare(LongSet o1, LongSet o2) {
        return o1.hashCode() - o2.hashCode();
      }
    });
    result = prime * result + (setCluster.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    PositionListIndex other = (PositionListIndex) obj;
    if (clusters == null) {
      if (other.clusters != null) {
        return false;
      }
    } else {
      List<LongOpenHashSet> setCluster = convertClustersToSets(clusters);
      List<LongOpenHashSet> otherSetCluster = convertClustersToSets(other.clusters);

      for (LongOpenHashSet cluster : setCluster) {
        if (!otherSetCluster.contains(cluster)) {
          return false;
        }
      }
      for (LongOpenHashSet cluster : otherSetCluster) {
        if (!setCluster.contains(cluster)) {
          return false;
        }
      }
    }

    return true;
  }

  protected List<LongOpenHashSet> convertClustersToSets(List<LongArrayList> listCluster) {
    List<LongOpenHashSet> setClusters = new LinkedList<>();
    for (LongList cluster : listCluster) {
      setClusters.add(new LongOpenHashSet(cluster));
    }

    return setClusters;
  }

  /**
   * Intersects the two given {@link PositionListIndex} and returns the outcome as new
   * PositionListIndex.
   *
   * @param otherPLI the other {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PositionListIndex}
   *                 to intersect
   * @return the intersected {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PositionListIndex}
   */
  protected PositionListIndex calculateIntersection(PositionListIndex otherPLI) {
    Long2LongOpenHashMap hashedPLI = this.asHashMap();
    Map<LongPair, LongArrayList> map = new HashMap<>();
    buildMap(otherPLI, hashedPLI, map);

    List<LongArrayList> clusters = new ArrayList<>();
    for (LongArrayList cluster : map.values()) {
      if (cluster.size() < 2) {
        continue;
      }
      clusters.add(cluster);
    }
    return new PositionListIndex(clusters);
  }

  protected void buildMap(PositionListIndex otherPLI, Long2LongOpenHashMap hashedPLI,
                          Map<LongPair, LongArrayList> map) {
    long uniqueValueCount = 0;
    for (LongArrayList sameValues : otherPLI.clusters) {
      for (long rowCount : sameValues) {
        if (hashedPLI.containsKey(rowCount)) {
          LongPair pair = new LongPair(uniqueValueCount, hashedPLI.get(rowCount));
          updateMap(map, rowCount, pair);
        }
      }
      uniqueValueCount++;
    }
  }

  protected void updateMap(Map<LongPair, LongArrayList> map, long rowCount, LongPair pair) {
    if (map.containsKey(pair)) {
      LongArrayList currentList = map.get(pair);
      currentList.add(rowCount);
    } else {
      LongArrayList newList = new LongArrayList();
      newList.add(rowCount);
      map.put(pair, newList);
    }
  }

  /**
   * Returns the position list index in a map representation. Every row index maps to a value
   * reconstruction. As the original values are unknown they are represented by a counter. The
   * position list index ((0, 1), (2, 4), (3, 5)) would be represented by {0=0, 1=0, 2=1, 3=2, 4=1,
   * 5=2}.
   *
   * @return the pli as hash map
   */
  public Long2LongOpenHashMap asHashMap() {
    Long2LongOpenHashMap hashedPLI = new Long2LongOpenHashMap(clusters.size());
    long uniqueValueCount = 0;
    for (LongArrayList sameValues : clusters) {
      for (long rowIndex : sameValues) {
        hashedPLI.put(rowIndex, uniqueValueCount);
      }
      uniqueValueCount++;
    }
    return hashedPLI;
  }

  /**
   * Returns the number of non unary clusters.
   *
   * @return the number of clusters in the {@link PositionListIndex}
   */
  public long size() {
    return clusters.size();
  }

  /**
   * @return the {@link PositionListIndex} contains only unary clusters.
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * @return the column represented by the {@link PositionListIndex} is unique.
   */
  public boolean isUnique() {
    return isEmpty();
  }

  /**
   * Returns the number of columns to remove in order to make column unique. (raw key error)
   *
   * @return raw key error
   */
  public long getRawKeyError() {
    if (rawKeyError == -1) {
      rawKeyError = calculateRawKeyError();
    }

    return rawKeyError;
  }

  protected long calculateRawKeyError() {
    long sumClusterSize = 0;

    for (LongArrayList cluster : clusters) {
      sumClusterSize += cluster.size();
    }

    return sumClusterSize - clusters.size();
  }

}
