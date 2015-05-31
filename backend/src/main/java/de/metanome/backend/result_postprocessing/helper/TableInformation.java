/*
 * Copyright 2015 by the Metanome project
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

import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides metadata and statistics about a table including all columns
 */
public class TableInformation {

  // Number of columns
  private int columnCount;
  // Table name
  private String tableName;
  // List of column information
  private List<ColumnInformation> columnInformationList;
  // Relational input generator
  private RelationalInputGenerator relationalInputGenerator;

  /**
   * Computes table metadata on the input data
   *
   * @param relationalInputGenerator   The input data generator providing access to the input data
   *                                   stream
   * @param useDataDependentStatistics true, if data dependent statistics should be calculated,
   *                                   false otherwise
   * @throws InputGenerationException Will be thrown if the input data is not accessible
   */
  public TableInformation(RelationalInputGenerator relationalInputGenerator,
                          boolean useDataDependentStatistics)
      throws InputGenerationException, InputIterationException {
    this.relationalInputGenerator = relationalInputGenerator;

    // Get table data
    RelationalInput relationalInput = relationalInputGenerator.generateNewCopy();
    this.columnCount = relationalInput.numberOfColumns();
    this.tableName = relationalInput.relationName();

    // Create the column information
    List<String> columnNames = relationalInput.columnNames();
    this.columnInformationList = new ArrayList<>(this.columnCount);
    for (int columnIndex = 0; columnIndex < this.columnCount; columnIndex++) {

      // Compute the column information for the current column
      if (useDataDependentStatistics) {
        // Generate a new data iterator for each column
        relationalInput = relationalInputGenerator.generateNewCopy();
        this.columnInformationList
            .add(new ColumnInformation(columnNames.get(columnIndex),
                                       columnIndex,
                                       relationalInput,
                                       true));
      } else {
        this.columnInformationList.
            add(new ColumnInformation(columnNames.get(columnIndex),
                                      columnIndex));
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
    for (ColumnInformation columnInformation : columnInformationList) {
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
    return columnInformationList.get(0).getRowCount();
  }

  public ColumnInformation getColumn(int columnIndex) {
    return columnInformationList.get(columnIndex);
  }

  public List<ColumnInformation> getColumnInformationList() {
    return columnInformationList;
  }

  public RelationalInputGenerator getRelationalInputGenerator() {
    return relationalInputGenerator;
  }

}
