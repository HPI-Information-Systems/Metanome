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

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Jens Hildebrandt
 */

public class SuperSetGraph {

  protected int numberOfColumns = -1;
  protected SubSuperSetGraph graph = new SubSuperSetGraph();

  public SuperSetGraph(int numberOfColumns) {
    this.numberOfColumns = numberOfColumns;
  }

  public SuperSetGraph add(ColumnCombinationBitset columnCombination) {
    graph.add(columnCombination.invert(numberOfColumns));
    return this;
  }

  public SuperSetGraph addAll(Collection<ColumnCombinationBitset> columnCombinations) {
    for (ColumnCombinationBitset columnCombination : columnCombinations) {
      graph.add(columnCombination.invert(numberOfColumns));
    }
    return this;
  }

  public ArrayList<ColumnCombinationBitset> getExistingSupersets(ColumnCombinationBitset subset) {
    ArrayList<ColumnCombinationBitset> result = new ArrayList<>();
    ArrayList<ColumnCombinationBitset>
        invertedColumns =
        graph.getExistingSubsets(subset.invert(numberOfColumns));

    for (ColumnCombinationBitset bitset : invertedColumns) {
      result.add(bitset.invert(numberOfColumns));
    }
    return result;
  }

  public boolean containsSuperset(ColumnCombinationBitset subset) {
    return graph.containsSubset(subset.invert(numberOfColumns));
  }

  public boolean isEmpty() {
    return this.graph.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SuperSetGraph graph1 = (SuperSetGraph) o;

    if (numberOfColumns != graph1.numberOfColumns) {
      return false;
    }
    if (!graph.equals(graph1.graph)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = numberOfColumns;
    result = 31 * result + graph.hashCode();
    return result;
  }
}
