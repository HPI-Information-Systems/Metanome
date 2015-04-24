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

import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.backend.result_postprocessing.result_comparator.OrderDependencyResultComparator;
import de.metanome.backend.results_db.ResultType;

import java.util.ArrayList;
import java.util.List;


public class OrderDependencyPaginationTable
    extends AbstractPaginationTable<OrderDependency> {

  /**
   * Constructs the page for given algorithm execution
   *
   * @param executionID ID of the algorithm execution for which the ranks should be shown
   */
  public OrderDependencyPaginationTable(long executionID, ResultType resultType) {
    super(executionID, resultType);
  }

  /**
   * Initializes the table columns
   *
   * @return Returns the list of sort properties for sortable columns
   */
  @Override
  protected List<String> initializeColumns() {
    List<String> columnNames = new ArrayList<>();

    // lhs column
    TextColumn<OrderDependency> lhsColumn = new TextColumn<OrderDependency>() {
      @Override
      public String getValue(OrderDependency orderDependency) {
        return orderDependency.getLhs().toString();
      }
    };
    this.table.addColumn(lhsColumn, "LHS");
    columnNames.add(OrderDependencyResultComparator.LHS_COLUMN);

    // rhs column
    TextColumn<OrderDependency> rhsColumn = new TextColumn<OrderDependency>() {
      @Override
      public String getValue(OrderDependency orderDependency) {
        return orderDependency.getLhs().toString();
      }
    };
    this.table.addColumn(rhsColumn, "RHS");
    columnNames.add(OrderDependencyResultComparator.RHS_COLUMN);

    // Set all columns as sortable
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumn(i).setSortable(true);
      table.getColumn(i).setDefaultSortAscending(true);
    }

    return columnNames;
  }

}
