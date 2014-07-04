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

import it.unimi.dsi.fastutil.longs.LongArrayList;

/**
 * Task class for the recursive calculation of conditions in {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PositionListIndex}
 *
 * @author Jens Hildebrandt
 */
public class ConditionTask {

  public int clusterIndex;
  public LongArrayList containedClusters;
  public LongArrayList containedRows;

  public ConditionTask(int clusterIndex) {
    this.clusterIndex = clusterIndex;
    this.containedClusters = new LongArrayList();
    this.containedRows = new LongArrayList();
  }


  public ConditionTask(int clusterIndex, LongArrayList containedClusters,
                       LongArrayList containedRows) {
    this.clusterIndex = clusterIndex;
    this.containedClusters = new LongArrayList(containedClusters);
    this.containedRows = new LongArrayList(containedRows);
  }
}
