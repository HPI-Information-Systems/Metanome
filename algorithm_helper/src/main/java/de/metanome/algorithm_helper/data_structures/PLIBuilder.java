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

import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.*;

/**
 * Constructs a list of {@link PositionListIndex}es from the given {@link
 * de.metanome.algorithm_integration.input.RelationalInput}. A list of all columns' sorted distinct
 * values can be constructed as a byproduct.
 */
public class PLIBuilder {

  protected long numberOfTuples = -1;
  protected List<HashMap<String, LongArrayList>> columns = null;
  protected RelationalInput input;
  protected boolean nullEqualsNull;

  public PLIBuilder(RelationalInput input) {
    this.input = input;
    this.nullEqualsNull = true;
  }

  public PLIBuilder(RelationalInput input, boolean nullEqualsNull) {
    this(input);
    this.nullEqualsNull = nullEqualsNull;
  }

  /**
   * Builds a {@link PositionListIndex} for every column in the input.
   *
   * @return list of plis for all columns
   * @throws InputIterationException if the input cannot be iterated
   */
  public List<PositionListIndex> getPLIList() throws InputIterationException {
    List<List<LongArrayList>> rawPLIs = getRawPLIs();
    List<PositionListIndex> result = new ArrayList<>();
    for (List<LongArrayList> rawPLI : rawPLIs) {
      result.add(new PositionListIndex(rawPLI));
    }
    return result;
  }

  /**
   * Calculates the raw PositionListIndices
   *
   * @return list of associated clusters (PLI)
   * @throws InputIterationException if the input cannot be iterated
   */
  protected List<List<LongArrayList>> getRawPLIs() throws InputIterationException {
    if (columns == null) {
      columns = new ArrayList<>();
      calculateUnpurgedPLI();
    }
    return purgePLIEntries();
  }

  /**
   * Returns the number of tuples in the input after calculating the plis. Can be used after
   * calculateUnpurgedPLI was called.
   *
   * @return number of tuples in dataset
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the number of tuples is less or equal to zero
   */
  public long getNumberOfTuples() throws InputIterationException {
    if (this.numberOfTuples == -1) {
      throw new InputIterationException();
    } else {
      return this.numberOfTuples;
    }
  }

  /**
   * Builds a {@link TreeSet} of the values of every column in the input. "null" values are filtered
   * as they are not required for spider.
   *
   * @return all comlumns' sorted distinct values
   * @throws InputIterationException if the input cannot be iterated
   */
  public List<TreeSet<String>> getDistinctSortedColumns() throws InputIterationException {
    if (columns == null) {
      columns = new ArrayList<>();
      calculateUnpurgedPLI();
    }

    List<TreeSet<String>> distinctSortedColumns = new LinkedList<>();

    for (HashMap<String, LongArrayList> columnMap : columns) {
      if (columnMap.containsKey(null)) {
        columnMap.remove(null);
      }
      distinctSortedColumns.add(new TreeSet<>(columnMap.keySet()));
    }

    return distinctSortedColumns;
  }

  protected void calculateUnpurgedPLI() throws InputIterationException {
    long rowCount = 0;
    this.numberOfTuples = 0;
    while (input.hasNext()) {
      this.numberOfTuples++;
      List<String> row = input.next();
      int columnCount = 0;
      for (String cellValue : row) {
        addValue(rowCount, columnCount, cellValue);
        columnCount++;
      }
      rowCount++;
    }
  }

  protected void addValue(long rowCount, int columnCount, String attributeCell) {
    if (columns.size() <= columnCount) {
      columns.add(new HashMap<String, LongArrayList>());
    }

    if (!this.nullEqualsNull && attributeCell == null) {
      return;
    }

    if (columns.get(columnCount).containsKey(attributeCell)) {
      columns.get(columnCount).get(attributeCell).add(rowCount);
    } else {
      LongArrayList newList = new LongArrayList();
      newList.add(rowCount);
      columns.get(columnCount).put(attributeCell, newList);
    }
  }

  protected List<List<LongArrayList>> purgePLIEntries() {
    List<List<LongArrayList>> rawPLIList = new ArrayList<>();
    Iterator<HashMap<String, LongArrayList>> columnsIterator = columns.iterator();
    while (columnsIterator.hasNext()) {
      List<LongArrayList> clusters = new ArrayList<>();
      for (LongArrayList cluster : columnsIterator.next().values()) {
        if (cluster.size() < 2) {
          continue;
        }
        clusters.add(cluster);
      }
      rawPLIList.add(clusters);
      // Free value Maps.
      columnsIterator.remove();
    }
    return rawPLIList;
  }
}
