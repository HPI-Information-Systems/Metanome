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

public class PositionListIndex {

	protected List<LongArrayList> clusters;

	public PositionListIndex(List<LongArrayList> clusters) {
		this.clusters = clusters;
	}

	/**
	 * Constructs an empty {@link PositionListIndex}.
	 */
	public PositionListIndex() {
		this.clusters = new ArrayList<LongArrayList>();
	}
	
	/**
	 * @param otherPLI
	 * @return {@link PositionListIndex}
	 * 
	 *         Intersects the given PositionListIndex with this
	 *         PositionListIndex returning a new PositionListIndex. For the
	 *         intersection the smaller PositionListIndex is converted into a
	 *         HashMap.
	 */
	public PositionListIndex intersect(PositionListIndex otherPLI) {
		//TODO Optimize Smaller PLI as Hashmap?
		return calculateIntersection(otherPLI);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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
		result = prime * result
				+ ((setCluster == null) ? 0 : setCluster.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PositionListIndex other = (PositionListIndex) obj;
		if (clusters == null) {
			if (other.clusters != null)
				return false;
		} else {
			List<LongOpenHashSet> setCluster = convertClustersToSets(clusters);
			List<LongOpenHashSet> otherSetCluster = convertClustersToSets(other.clusters);
			
			for (LongSet cluster : setCluster) {
				if (!otherSetCluster.contains(cluster)) {
					return false;
				}
			}
			for (LongSet cluster : otherSetCluster) {
				if (!setCluster.contains(cluster)) {
					return false;
				}
			}
		}
			
		return true;
	}
	
	private List<LongOpenHashSet> convertClustersToSets(List<LongArrayList> listCluster) {
		List<LongOpenHashSet> setClusters = new LinkedList<LongOpenHashSet>();
		for (LongList cluster : listCluster) {
			setClusters.add(new LongOpenHashSet(cluster));
		}
		
		return setClusters;
	}

	/**
	 * @param otherPLI
	 * @return {@link PositionListIndex}
	 * 
	 *         Intersects the two given {@link PositionListIndex} and returns
	 *         the outcome as new PositionListIndex.
	 */
	protected PositionListIndex calculateIntersection(PositionListIndex otherPLI) {
		Long2LongOpenHashMap hashedPLI = this.asHashMap();
		Map<LongPair, LongArrayList> map = new HashMap<LongPair, LongArrayList>();
		buildMap(otherPLI, hashedPLI, map);
		
		List<LongArrayList> clusters = new ArrayList<LongArrayList>();
		for (LongArrayList cluster : map.values()) {
			if (cluster.size() < 2) {
				continue;
			} 
			clusters.add(cluster);
		}
		return new PositionListIndex(clusters);
	}

	protected void buildMap(PositionListIndex otherPLI, Long2LongOpenHashMap hashedPLI, Map<LongPair, LongArrayList> map) {
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
	 * TODO fix docs
	 * 
	 * @return {@link Long2LongOpenHashMap} Creates
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
	 * Returns the number of columns to remove in order to make column unique.
	 * (raw key error) 
	 * 
	 * @return raw key error
	 */
	public long getRawKeyError() {
		long sumClusterSize = 0;
		
		for (LongArrayList cluster : clusters) {
			sumClusterSize += cluster.size();
		}
		
		return sumClusterSize - clusters.size();
	}

}
