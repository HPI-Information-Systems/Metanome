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

package de.metanome.frontend.client.results.pagination_table;

import com.google.gwt.user.cellview.client.TextColumn;

import de.metanome.backend.result_postprocessing.result_comparator.UniqueColumnCombinationResultComparator;
import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;
import de.metanome.backend.results_db.ResultType;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination table for unique column combination results.
 */
public class UniqueColumnCombinationPaginationTable
    extends AbstractPaginationTable<UniqueColumnCombinationResult> {

  /**
   * Constructs the table for given result type
   *
   * @param resultType the result type
   */
  public UniqueColumnCombinationPaginationTable(ResultType resultType) {
    super(resultType);
  }

  /**
   * Initializes the table columns
   *
   * @return Returns the list of sort properties for sortable columns
   */
  @Override
  protected List<String> initializeColumns() {
    List<String> columnNames = new ArrayList<>();

    // Unique Column Combination column
    TextColumn<UniqueColumnCombinationResult>
        columnCombinationColumn =
        new TextColumn<UniqueColumnCombinationResult>() {
          @Override
          public String getValue(UniqueColumnCombinationResult functionalDependency) {
            return functionalDependency.getColumnCombination().toString();
          }
        };
    this.table.addColumn(columnCombinationColumn, "Column Combination");
    columnNames.add(UniqueColumnCombinationResultComparator.COLUMN_COMBINATION_COLUMN);

    // Set all columns as sortable
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumn(i).setSortable(true);
      table.getColumn(i).setDefaultSortAscending(true);
    }

    return columnNames;
  }

}
