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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.PageSizePager;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.FlowPanel;

import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.results_db.ResultType;

import java.util.List;

public abstract class AbstractPaginationTable<CellType extends Result> extends FlowPanel {

  private static final int ELEMENTS_PER_PAGE = 50;

  protected ResultType resultType;
  // Pager
  protected AbstractPager controlsPager;
  protected AbstractPager pageSizePager;
  // GWT cell table
  protected CellTable<CellType> table;


  /**
   * Constructs the page for given algorithm execution
   *
   * @param resultType  the result type of the results the table holds
   */
  protected AbstractPaginationTable( ResultType resultType) {
    // The result table type
    this.resultType = resultType;

    // Create the table
    this.table = new CellTable<>();

    // Create a pager to support pagination
    this.pageSizePager = new PageSizePager(ELEMENTS_PER_PAGE);

    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
    this.controlsPager = new SimplePager(SimplePager.TextLocation.CENTER,
                                         pagerResources, false, 0, true);
    this.pageSizePager.setDisplay(this.table);
    this.controlsPager.setDisplay(this.table);

    // Initialize table columns
    List<String> columnPropertyNames = initializeColumns();

    // Setup table properties
    this.table.setRowCount(0, true);
    this.table.setVisible(true);

    // Add pagination control elements to the page
    this.add(this.controlsPager);
    this.add(this.pageSizePager);
    // Add table element to the page
    this.add(this.table);

    // Setup the asynchronous data provider used to retrieve data for paginated table
    DataProvider<CellType> dataProvider = new DataProvider<>(resultType.getName());
    dataProvider.setTable(this.table);
    dataProvider.setSortPropertyNames(columnPropertyNames);
    dataProvider.addDataDisplay(this.table);

    // Create and register an asynchronous sort handler
    ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(this.table);
    this.table.addColumnSortHandler(columnSortHandler);
  }

  /**
   * Removes the table and the pager
   */
  private void removeTable() {
    this.remove(this.table);
    this.remove(this.controlsPager);
    this.remove(this.pageSizePager);
  }

  /**
   * Initializes the table columns
   *
   * @return Returns the list of sort properties for sortable columns
   */
  protected abstract List<String> initializeColumns();

}
