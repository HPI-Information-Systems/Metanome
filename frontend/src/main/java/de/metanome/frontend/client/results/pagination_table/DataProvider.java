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
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import de.metanome.algorithm_integration.results.Result;
import de.metanome.frontend.client.services.ResultStoreRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class DataProvider<CellType extends Result> extends AsyncDataProvider<CellType> {

  protected final ResultStoreRestService restService = GWT.create(ResultStoreRestService.class);

  protected String resultTypeName;
  protected int totalResultsCount;
  protected boolean isAscending;
  // Page starting tuple index (inclusive)
  protected int start;
  // Page ending tuple index (exclusive)
  protected int end;
  // Current sort property
  protected String sortPropertyName;
  // Names of all supported sorting properties
  protected List<String> sortPropertyNames;
  // Table to which the data provider is connected
  protected CellTable<CellType> table;

  public DataProvider(String type) {
    this.resultTypeName = type;
  }

  public void setSortPropertyNames(List<String> sortPropertyNames) {
    this.sortPropertyNames = sortPropertyNames;
  }

  public CellTable getTable() {
    return table;
  }

  public void setTable(CellTable<CellType> table) {
    this.table = table;
  }

  /**
   * Trigger reacting on page requests
   *
   * @param hasData Describes the table data
   */
  @Override
  protected void onRangeChanged(HasData<CellType> hasData) {
    // Get requested start and end indices
    start = hasData.getVisibleRange().getStart();
    end = start + hasData.getVisibleRange().getLength();

    // Retrieve the sorting properties
    ColumnSortList columnSortList = table.getColumnSortList();
    if (columnSortList != null) {
      // Provide a default sorting column
      if (columnSortList.size() == 0) {
        columnSortList.push(table.getColumn(0));
      }
      isAscending = columnSortList.get(0).isAscending();
      sortPropertyName = sortPropertyNames
              .get(table.getColumnIndex((Column<CellType, ?>) columnSortList.get(0).getColumn()));
    }

    // Update the data
    countResults(getResultCountCallback());
  }

  /**
   * Counts the results and updates the page data based on the returned value
   *
   * @return Returns a callback for counting results and updating page data
   */
  private MethodCallback<Integer> getResultCountCallback() {
    return new MethodCallback<Integer>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
      }

      @Override
      public void onSuccess(Method method, Integer resultsCount) {
        // Store the total results count
        totalResultsCount = resultsCount;
        // Update data based on this knowledge
        getResultsFromTo(getResultsRetrieveCallback());
      }
    };
  }

  /**
   * Retrieves the requested page data from the database and updates the table
   *
   * @return Returns a callback for retrieving page data and updating the table
   */
  private MethodCallback<List<Result>> getResultsRetrieveCallback() {
    return new MethodCallback<List<Result>>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
      }

      @Override
      public void onSuccess(Method method, List<Result> retrievedResults) {
        // Update end index if needed
        end = Math.min(end, totalResultsCount);

        // Update the data
        updateRowData(start, (List<CellType>) retrievedResults);
        // Update the total row count
        updateRowCount(totalResultsCount, true);

        // Redraw the table
        table.redraw();
      }
    };
  }

  /**
   * Retrieves the total results count from the backend and triggers the provided callback
   *
   * @param resultsCountCallback Callback which should be called after retrieving the results count
   */
  protected void countResults(MethodCallback<Integer> resultsCountCallback) {
    restService.count(resultTypeName, resultsCountCallback);
  }

  /**
   * Retrieves the results from the backend and triggers the provided callback
   *
   * @param resultsRetrieveCallback Callback which should be called after retrieving the results
   */
  protected void getResultsFromTo(
      MethodCallback<List<Result>> resultsRetrieveCallback) {
    restService
        .listAllFromTo(resultTypeName, sortPropertyName, isAscending, start, end,
                       resultsRetrieveCallback);
  }

}
