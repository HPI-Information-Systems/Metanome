package de.metanome.frontend.client.results;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;


/**
 * Abstract ranking data provider, which allows simple pagination implementation by overriding the template methods
 *
 * Created by Alexander Spivak on 04.03.2015.
 */
public abstract class AbstractRankingAsyncDataProvider<CellType>  extends AsyncDataProvider<CellType> {

  //<editor-fold desc="Attributes">

  // ID of the algorithm execution the data should be retrieved for
  protected long executionID;

  // Page starting tuple index (inclusive)
  protected int start;
  // Page ending tuple index (exclusive)
  protected int end;

  // Count of accessible results
  protected int totalResultsCount;

  // Is data sorted ascending or descending?
  protected boolean isAscending;
  // Current sort property
  protected String sortPropertyName;
  // Names of all supported sorting properties
  protected List<String> sortPropertyNames;

  // Table to which the data provider is connected
  protected CellTable<CellType> table;

  //</editor-fold>

  //<editor-fold desc="Setter">

  public void setExecutionID(long executionID) {
    this.executionID = executionID;
  }

  public void setSortPropertyNames(List<String> sortPropertyNames) {
    this.sortPropertyNames = sortPropertyNames;
  }

  public void setTable(CellTable<CellType> table) {
    this.table = table;
  }

  //</editor-fold>

  //<editor-fold desc="Pagination trigger">

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
    if(columnSortList != null) {
      // Provide a default sorting column
      if(columnSortList.size() == 0){
        columnSortList.push(table.getColumn(0));
      }
      isAscending = columnSortList.get(0).isAscending();
      sortPropertyName = sortPropertyNames.get(table.getColumnIndex((Column<CellType, ?>)columnSortList.get(0).getColumn()));
    }

    // Update the data
    retrieveDataCountFromBackend(getResultCountCallback());
  }

  //</editor-fold>

  //<editor-fold desc="Data access callbacks">

  /**
   * Counts the results and updates the page data based on the returned value
   *
   * @return Returns a callback for counting results and updating page data
   */
  private MethodCallback<GwtLong> getResultCountCallback(){
    return new MethodCallback<GwtLong>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
      }

      @Override
      public void onSuccess(Method method, GwtLong resultsCount) {
        // Store the total results count
        totalResultsCount = (int)resultsCount.getValue();
        // Update data based on this knowledge
        retrieveDataFromBackend(getResultsRetrieveCallback());
      }
    };
  }

  /**
   * Retrieves the requested page data from the database and updates the table
   *
   * @return Returns a callback for retrieving page data and updating the table
   */
  private MethodCallback<List<CellType>> getResultsRetrieveCallback(){
    return new MethodCallback<List<CellType>>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
      }

      @Override
      public void onSuccess(Method method, List<CellType> retrievedResults) {
        // Update end index if needed
        end = Math.min(end, totalResultsCount);

        // Update the data
        updateRowData(start, retrievedResults);
        // Update the total row count
        updateRowCount(totalResultsCount, true);

        // Redraw the table
        table.redraw();
      }
    };
  }

  //</editor-fold>

  //<editor-fold desc="Callback hook methods">

  /**
   * Retrieves the total results count from the backend and triggers the provided callback
   *
   * @param resultsCountCallback Callback which should be called after retrieving the results count
   */
  protected abstract void retrieveDataCountFromBackend(MethodCallback<GwtLong> resultsCountCallback);

  /**
   * Retrieves the results from the backend and triggers the provided callback
   *
   * @param resultsRetrieveCallback Callback which should be called after retrieving the results
   */
  protected abstract void retrieveDataFromBackend(MethodCallback<List<CellType>> resultsRetrieveCallback);

  //</editor-fold>

}
