package de.metanome.frontend.client.results;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.PageSizePager;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

/**
 * Abstract ranking table page providing a simple solution to create paginated tables by overwriting the template methods
 *
 * Created by Alexander Spivak on 04.03.2015.
 */
public abstract class AbstractRankingTablePage<CellType> extends FlowPanel implements TabContent {

  //<editor-fold desc="Protected attributes">

  // Message receiver copied from other page implementations
  protected TabWrapper messageReceiver;

  // Execution ID for which the rankings should be shown
  protected long executionID = 0l;
  // Executing message label
  protected Label executingLabel;
  // Executing icon
  protected Image executingIcon;
  // Pager
  protected AbstractPager controlsPager;
  protected AbstractPager pageSizePager;
  // GWT cell table used for displaying FD rank results
  protected CellTable<CellType> table;

  //</editor-fold>

  //<editor-fold desc="Setter">

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }

  //</editor-fold>

  //<editor-fold desc="Constructor">

  /**
   * Constructs the page for given algorithm execution
   *
   * @param executionID ID of the algorithm execution for which the ranks should be shown
   */
  protected AbstractRankingTablePage(final long executionID){
    // Store the execution ID
    this.executionID = executionID;

    // Create a button for computing data dependent rankings
    final Button additionalRanksButton = new Button("Calculate additional ranks");
    additionalRanksButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        // Disable and hide the button
        additionalRanksButton.setEnabled(false);
        additionalRanksButton.setVisible(false);

        // Remove the table to recreate it later
        removeTable();

        // Execution progress labels
        initializeExecutionLabels();

        // Recompute rankings (backend call)
        recomputeRankings();
      }
    });

    // Add the button to the page
    this.add(additionalRanksButton);

    // Create the table without data dependant columns
    createTable(false);
  }

  //</editor-fold>

  //<editor-fold desc="Table initialization helper">

  /**
   * Initializes execution progress labels
   */
  private void initializeExecutionLabels(){
    // Add loading icon
    // Add label for the algorithm, which is executed at the moment
    this.executingLabel = new Label("Computing data dependant rankings. Please wait!");
    add(this.executingLabel);

    // Add a running indicator
    this.executingIcon = new Image("ajax-loader.gif");
    add(this.executingIcon);
  }

  /**
   * Removes the table and the pager
   */
  private void removeTable(){
    this.remove(this.table);
    this.remove(this.controlsPager);
    this.remove(this.pageSizePager);
  }

  /**
   * Creates the paginated table with paging control buttons
   *
   * @param fullTable Should the table contains all columns or just the data independent ones
   */
  private void createTable(boolean fullTable){

    this.table = new CellTable<>();

    // Create a pager to support pagination
    this.pageSizePager = new PageSizePager(getElementsPerPage());

    SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
    this.controlsPager = new SimplePager(SimplePager.TextLocation.CENTER,
                                         pagerResources, true, 0, true);
    this.pageSizePager.setDisplay(this.table);
    this.controlsPager.setDisplay(this.table);
    //pager.setPageSize(this.getElementsPerPage());

    // Initialize table columns
    List<String> columnPropertyNames = initializeColumns(fullTable);

    // Setup table properties
    this.table.setRowCount(0, true);
    this.table.setTitle(this.getTableTitle());
    this.table.setVisible(true);

    // Add pagination control elements to the page
    this.add(this.controlsPager);
    this.add(this.pageSizePager);
    // Add table element to the page
    this.add(this.table);

    // Setup the asynchronous data provider used to retrieve data for paginated table
    AbstractRankingAsyncDataProvider<CellType> dataProvider = getDataProvider();
    dataProvider.setExecutionID(this.executionID);
    dataProvider.setTable(this.table);
    dataProvider.setSortPropertyNames(columnPropertyNames);
    dataProvider.addDataDisplay(this.table);

    // Create and register an asynchronous sort handler
    ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(this.table);
    this.table.addColumnSortHandler(columnSortHandler);
  }

  /**
   * Returns a callback, which removes the execution label and icon and recreates the full table
   *
   * @return Returns a callback, which removes the execution label and icon and recreates the full table
   */
  protected MethodCallback<Void> getAdditionalRankingsCallback(){
    return new MethodCallback<Void>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {

      }

      @Override
      public void onSuccess(Method method, Void aVoid) {
        remove(executingLabel);
        remove(executingIcon);
        createTable(true);
      }
    };
  }

  //</editor-fold>

  //<editor-fold desc="Template methods">

  /**
   * Template method to construct a data provider of appropriate type
   *
   * @return Returns a constructed asynchronous data provider
   */
  protected abstract AbstractRankingAsyncDataProvider<CellType> getDataProvider();

  /**
   * Template method to redefine the elements per page count
   *
   * @return Returns the number of elements per page
   */
  protected abstract int getElementsPerPage();

  /**
   * Template method to redefine the table title
   *
   * @return Returns the table title
   */
  protected abstract String getTableTitle();

  /**
   * Initializes the table columns
   *
   * @param fullTable Should the columns for data dependant ranks be created or not?
   * @return Returns the list of sort properties for sortable columns
   */
  protected abstract List<String> initializeColumns(boolean fullTable);

  /**
   * Recomputes the rankings after the data-dependent ranking request
   */
  protected abstract void recomputeRankings();

  //</editor-fold>

}
