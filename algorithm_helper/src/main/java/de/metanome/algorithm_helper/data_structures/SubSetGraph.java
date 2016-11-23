/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithm_helper.data_structures;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.*;

/**
 * A graph that allows efficient lookup of all subsets in the graph for a given
 * ColumnCombinationBitset.
 *
 * @author Jens Ehrlich
 * @author Jakob Zwiener
 */
public class SubSetGraph {

  protected Int2ObjectMap<SubSetGraph> subGraphs = new Int2ObjectOpenHashMap<>();
  protected boolean subSetEnds = false;

  /**
   * Adds a column combination to the graph. Returns the graph after adding.
   *
   * @param columnCombination a column combination to add
   * @return the graph
   */
  public SubSetGraph add(ColumnCombinationBitset columnCombination) {
    SubSetGraph subGraph = this;

    for (int setColumnIndex : columnCombination.getSetBits()) {
      subGraph = subGraph.lazySubGraphGeneration(setColumnIndex);
    }
    subGraph.subSetEnds = true;
    return this;
  }

  /**
   * Adds all columnCombinations in the {@link java.util.Collection} to the graph.
   *
   * @param columnCombinations a {@link java.util.Collection} of {@link de.metanome.algorithm_helper.data_structures.ColumnCombinationBitset}s
   *                           to add to the graph
   * @return the graph
   */
  public SubSetGraph addAll(Collection<ColumnCombinationBitset> columnCombinations) {
    for (ColumnCombinationBitset columnCombination : columnCombinations) {
      add(columnCombination);
    }

    return this;
  }

  /**
   * Looks for the subgraph or builds and adds a new one.
   *
   * @param setColumnIndex the column index to perform the lookup on
   * @return the subgraph behind the column index
   */
  protected SubSetGraph lazySubGraphGeneration(int setColumnIndex) {
    SubSetGraph subGraph = subGraphs.get(setColumnIndex);

    if (subGraph == null) {
      subGraph = new SubSetGraph();
      subGraphs.put(setColumnIndex, subGraph);
    }

    return subGraph;
  }

  /**
   * Returns all Subsets of the given ColumnCombination that are in the graph.
   *
   * @param columnCombinationToQuery given superset to search for subsets
   * @return a list containing all found subsets
   */
  public ArrayList<ColumnCombinationBitset> getExistingSubsets(
    ColumnCombinationBitset columnCombinationToQuery) {
    ArrayList<ColumnCombinationBitset> subsets = new ArrayList<>();
    if (this.isEmpty()) {
      return subsets;
    }
    // Create task queue and initial task.
    Queue<SubSetFindTask> openTasks = new LinkedList<>();
    openTasks.add(new SubSetFindTask(this, 0, new ColumnCombinationBitset()));

    while (!openTasks.isEmpty()) {
      SubSetFindTask currentTask = openTasks.remove();
      // If the current subgraph is empty a subset has been found
      if (currentTask.subGraph.isEmpty()) {
        subsets.add(new ColumnCombinationBitset(currentTask.path));
        continue;
      }

      if (currentTask.subGraph.subSetEnds) {
        subsets.add(new ColumnCombinationBitset(currentTask.path));
      }

      // Iterate over the remaining column indices
      for (int i = currentTask.numberOfCheckedColumns; i < columnCombinationToQuery.size(); i++) {
        int currentColumnIndex = columnCombinationToQuery.getSetBits().get(i);
        // Get the subgraph behind the current index
        SubSetGraph subGraph =
          currentTask.subGraph.subGraphs.get(currentColumnIndex);
        // column index is not set on any set --> check next column index
        if (subGraph != null) {
          // Add the current column index to the path
          ColumnCombinationBitset path =
            new ColumnCombinationBitset(currentTask.path)
              .addColumn(currentColumnIndex);

          openTasks.add(new SubSetFindTask(subGraph, i + 1, path));
        }
      }
    }

    return subsets;
  }

  /**
   * The method returns when the first subset is found in the graph. This is possibly faster than
   * {@link SubSetGraph#getExistingSubsets(ColumnCombinationBitset)}, because a smaller part of the
   * graph must be traversed.
   *
   * @param superset the super set
   * @return whether at least a single subset is contained in the graph
   */
  public boolean containsSubset(ColumnCombinationBitset superset) {
    if (this.isEmpty()) {
      return false;
    }
    Queue<SubSetFindTask> openTasks = new LinkedList<>();
    openTasks.add(new SubSetFindTask(this, 0, new ColumnCombinationBitset()));

    while (!openTasks.isEmpty()) {
      SubSetFindTask currentTask = openTasks.remove();
      // If the current subgraph is empty a subset has been found
      if (currentTask.subGraph.isEmpty()) {
        return true;
      }

      if (currentTask.subGraph.subSetEnds) {
        return true;
      }

      // Iterate over the remaining column indices
      for (int i = currentTask.numberOfCheckedColumns; i < superset.size(); i++) {
        int currentColumnIndex = superset.getSetBits().get(i);
        // Get the subgraph behind the current index
        SubSetGraph subGraph =
          currentTask.subGraph.subGraphs.get(currentColumnIndex);
        // column index is not set on any set --> check next column index
        if (subGraph != null) {
          // Add the current column index to the path
          ColumnCombinationBitset path =
            new ColumnCombinationBitset(currentTask.path)
              .addColumn(currentColumnIndex);

          openTasks.add(new SubSetFindTask(subGraph, i + 1, path));
        }
      }
    }

    return false;
  }

  /**
   * The method returns all minimal subsets contained in the graph using a breadth-first search
   * pattern. Non minimal subsets are not traversed.
   *
   * @return a list containing all minimal subsets
   */
  public Set<ColumnCombinationBitset> getMinimalSubsets() {
    if (this.isEmpty()) {
      return new TreeSet<>();
    }

    SubSetGraph graph = new SubSetGraph();
    TreeSet<ColumnCombinationBitset> result = new TreeSet<>();
    TreeSet<SubSetFindTask> openTasks = new TreeSet<>();
    openTasks.add(new SubSetFindTask(this, 0, new ColumnCombinationBitset()));

    while (!openTasks.isEmpty()) {
      SubSetFindTask currentTask = openTasks.pollFirst();
      if (currentTask.subGraph.subSetEnds) {
        if (!graph.containsSubset(currentTask.path)) {
          graph.add(currentTask.path);
          result.add(currentTask.path);
        }
      } else {
        // Iterate over the remaining column indices
        for (int bitNumber : currentTask.subGraph.subGraphs.keySet()) {
          // Get the subgraph behind the current index
          SubSetGraph subGraph =
            currentTask.subGraph.subGraphs.get(bitNumber);
          // column index is not set on any set --> check next column index
          if (subGraph != null) {
            // Add the current column index to the path
            ColumnCombinationBitset path =
              new ColumnCombinationBitset(currentTask.path)
                .addColumn(bitNumber);

            openTasks
              .add(new SubSetFindTask(subGraph, bitNumber + 1, path));
          }
        }
      }
    }
    return result;
  }

  /**
   * @return whether the graph is empty
   */
  public boolean isEmpty() {
    return subGraphs.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SubSetGraph that = (SubSetGraph) o;

    return !(subGraphs != null ? !subGraphs.equals(that.subGraphs) : that.subGraphs != null);
  }

  @Override
  public int hashCode() {
    return subGraphs != null ? subGraphs.hashCode() : 0;
  }
}

/**
 * Task used to find subsets (avoiding recursion).
 */
class SubSetFindTask implements Comparable<SubSetFindTask> {

  public SubSetGraph subGraph;
  public int numberOfCheckedColumns;
  public ColumnCombinationBitset path;

  public SubSetFindTask(
    SubSetGraph subGraph,
    int numberOfCheckedColumns,
    ColumnCombinationBitset path) {
    this.subGraph = subGraph;
    this.numberOfCheckedColumns = numberOfCheckedColumns;
    this.path = path;
  }

  @Override
  public int compareTo(SubSetFindTask o) {
    return this.path.compareTo(o.path);
  }
}
