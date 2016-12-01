/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.result_postprocessing.helper;

import de.metanome.algorithm_helper.data_structures.PositionListIndex;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;

import java.util.*;

/**
 * Provides metadata and statistics about a table including all columns
 */
public class TableInformation {

  // Number of columns
  private int columnCount;
  // Table name
  private String tableName;
  // List of column information
  private Map<String, ColumnInformation> columnInformationMap;
  // Relational input generator
  private RelationalInputGenerator relationalInputGenerator;
  // Unique bit set representing this table
  private BitSet bitSet;
  // Map of the column indices to the corresponding position list index
  private Map<BitSet, PositionListIndex> PLIs;

  /**
   * Computes table metadata on the input data
   *
   * @param relationalInputGenerator     The input data generator providing access to the input data
   *                                     stream
   * @param useDataIndependentStatistics true, if data dependent statistics should be calculated,
   *                                     false otherwise
   * @param bitSet                       bit set, which represents this table
   * @throws InputGenerationException Will be thrown if the input data is not accessible
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the input is not iterable
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if input generator could not be build
   */
  public TableInformation(RelationalInputGenerator relationalInputGenerator,
                          boolean useDataIndependentStatistics,
                          BitSet bitSet)
    throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    this.relationalInputGenerator = relationalInputGenerator;
    this.bitSet = bitSet;

    // Get table data
    RelationalInput relationalInput = relationalInputGenerator.generateNewCopy();
    this.columnCount = relationalInput.numberOfColumns();
    this.tableName = StringHelper.removeFileEnding(relationalInput.relationName());

    // Create the column information
    List<String> columnNames = relationalInput.columnNames();
    this.columnInformationMap = new HashMap<>();

    for (int columnIndex = 0; columnIndex < this.columnCount; columnIndex++) {
      BitSet columnBitSet = new BitSet(this.columnCount);
      columnBitSet.set(columnIndex);

      // Compute the column information for the current column
      if (!useDataIndependentStatistics) {
        // Generate a new data iterator for each column
        relationalInput = relationalInputGenerator.generateNewCopy();
        this.columnInformationMap
          .put(columnNames.get(columnIndex),
            new ColumnInformation(columnNames.get(columnIndex),
              columnIndex,
              columnBitSet,
              relationalInput,
              true));
      } else {
        this.columnInformationMap.
          put(columnNames.get(columnIndex),
            new ColumnInformation(columnNames.get(columnIndex),
              columnIndex,
              columnBitSet));
      }
    }
  }

  /**
   * Computes the information content as sum of columns information contents
   *
   * @return Returns the information content of the table in bytes
   */
  public long getInformationContent() {
    long sum = 0l;
    for (ColumnInformation columnInformation : columnInformationMap.values()) {
      sum += columnInformation.getInformationContent(getRowCount());
    }
    return sum;
  }

  public int getColumnCount() {
    return columnCount;
  }

  public String getTableName() {
    return tableName;
  }

  public long getRowCount() {
    return columnInformationMap.values().iterator().next().getRowCount();
  }

  public ColumnInformation getColumn(int columnIndex) {
    Iterator<ColumnInformation> iterator = columnInformationMap.values().iterator();
    for (int i = 0; i <= this.columnCount; i++) {
      ColumnInformation columnInformation = iterator.next();
      if (columnInformation.getColumnIndex() == columnIndex) {
        return columnInformation;
      }
    }
    return null;
  }

  public Map<String, ColumnInformation> getColumnInformationMap() {
    return columnInformationMap;
  }

  public RelationalInputGenerator getRelationalInputGenerator() {
    return relationalInputGenerator;
  }

  public BitSet getBitSet() {
    return bitSet;
  }

  public Map<BitSet, PositionListIndex> getPLIs() {
    return PLIs;
  }

  public void setPLIs(Map<BitSet, PositionListIndex> PLIs) {
    this.PLIs = PLIs;
  }
}
