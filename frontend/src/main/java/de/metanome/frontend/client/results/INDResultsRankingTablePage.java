package de.metanome.frontend.client.results;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.TextColumn;

import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtINDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.frontend.client.services.ResultAnalyzerExecutorRestService;

import java.util.ArrayList;
import java.util.List;

/**
 * IND ranking table page providing a simple solution to create paginated tables by overwriting the template methods
 *
 * Created by Alexander Spivak on 03.03.2015.
 */
public class INDResultsRankingTablePage  extends AbstractRankingTablePage<GwtINDResult> {

  /**
   * Constructs the page for given algorithm execution
   *
   * @param executionID ID of the algorithm execution for which the ranks should be shown
   */
  public INDResultsRankingTablePage(long executionID){
    super(executionID);
  }

  /**
   * Constructs a data provider of appropriate type
   *
   * @return Returns a constructed asynchronous data provider
   */
  @Override
  protected AbstractRankingAsyncDataProvider<GwtINDResult> getDataProvider() {
    return new INDRankingAsyncDataProvider();
  }

  /**
   * Defines the "elements per page" count
   *
   * @return Returns the number of elements per page
   */
  @Override
  protected int getElementsPerPage() {
    return 10;
  }

  /**
   * Defines the table title
   *
   * @return Returns the table title
   */
  @Override
  protected String getTableTitle() {
    return "IND ranking table";
  }

  /**
   * Initializes the table columns
   *
   * @param fullTable Should the columns for data dependant ranks be created or not?
   * @return Returns the list of sort properties for sortable columns
   */
  @Override
  protected List<String> initializeColumns(boolean fullTable) {

    List<String> columnNames = new ArrayList<>();

    // Determinant column
    TextColumn<GwtINDResult> dependantColumn = new TooltipTextColumn<GwtINDResult>() {
      @Override
      protected String getTooltip(GwtINDResult tuple) {
        return tuple.getDependant().getTooltip();
      }

      @Override
      public String getValue(GwtINDResult tuple) {
        return tuple.getDependant().getValue();
      }

    };
    this.table.addColumn(dependantColumn, "Dependant");
    columnNames.add("dependantAsString");

    // Determinant column
    TextColumn<GwtINDResult> referencedColumn = new TooltipTextColumn<GwtINDResult>() {
      @Override
      protected String getTooltip(GwtINDResult tuple) {
        return tuple.getReferenced().getTooltip();
      }

      @Override
      public String getValue(GwtINDResult tuple) {
        return tuple.getReferenced().getValue();
      }

    };
    this.table.addColumn(referencedColumn, "Referenced");
    columnNames.add("referencedAsString");

    TextColumn<GwtINDResult> dependantSizeRatioColumn = new TextColumn<GwtINDResult>() {
      @Override
      public String getValue(GwtINDResult tuple) {
        return Float.valueOf(tuple.getIndRank().getDependantSizeRatio()).toString();
      }
    };
    this.table.addColumn(dependantSizeRatioColumn, "Dependant Size Ratio");
    columnNames.add("rank.dependantSizeRatio");

    TextColumn<GwtINDResult> referencedSizeRatioColumn = new TextColumn<GwtINDResult>() {
      @Override
      public String getValue(GwtINDResult tuple) {
        return Float.valueOf(tuple.getIndRank().getReferencedSizeRatio()).toString();
      }
    };
    this.table.addColumn(referencedSizeRatioColumn, "Referenced Size Ratio");
    columnNames.add("rank.referencedSizeRatio");

    TextColumn<GwtINDResult> dependantColumnOccurrenceColumn = new TextColumn<GwtINDResult>() {
      @Override
      public String getValue(GwtINDResult tuple) {
        return Float.valueOf(tuple.getIndRank().getDependantColumnOccurrence()).toString();
      }
    };
    this.table.addColumn(dependantColumnOccurrenceColumn, "Dependant Column Occurrence");
    columnNames.add("rank.dependantColumnOccurrence");

    TextColumn<GwtINDResult> referencedColumnOccurrenceColumn = new TextColumn<GwtINDResult>() {
      @Override
      public String getValue(GwtINDResult tuple) {
        return Float.valueOf(tuple.getIndRank().getReferencedColumnOccurrence()).toString();
      }
    };
    this.table.addColumn(referencedColumnOccurrenceColumn, "Referenced Column Occurrence");
    columnNames.add("rank.referencedColumnOccurrence");

    TextColumn<GwtINDResult> dependantConstancyRatioColumn = new TextColumn<GwtINDResult>() {
      @Override
      public String getValue(GwtINDResult tuple) {
        return Float.valueOf(tuple.getIndRank().getDependantConstancyRatio()).toString();
      }
    };

    if(fullTable) {
      this.table.addColumn(dependantConstancyRatioColumn, "Dependant Constancy Ratio");
      columnNames.add("rank.dependantConstancyRatio");

      TextColumn<GwtINDResult> referencedConstancyRatioColumn = new TextColumn<GwtINDResult>() {
        @Override
        public String getValue(GwtINDResult tuple) {
          return Float.valueOf(tuple.getIndRank().getReferencedConstancyRatio()).toString();
        }
      };
      this.table.addColumn(referencedConstancyRatioColumn, "Referenced Constancy Ratio");
      columnNames.add("rank.referencedConstancyRatio");
    }

    // Set all columns as sortable
    for(int i=0; i < table.getColumnCount(); i++){
      table.getColumn(i).setSortable(true);
      table.getColumn(i).setDefaultSortAscending(true);
    }

    return columnNames;
  }

  /**
   * Recomputes the rankings after the data-dependent ranking request
   */
  @Override
  protected void recomputeRankings() {
    // Make a call to the backend to recompute the rankings
    ResultAnalyzerExecutorRestService
        analyzerExecutorRestService = GWT.create(ResultAnalyzerExecutorRestService.class);
    analyzerExecutorRestService.dataDependentAnalysis(new GwtLong(executionID), getAdditionalRankingsCallback());
  }

}
