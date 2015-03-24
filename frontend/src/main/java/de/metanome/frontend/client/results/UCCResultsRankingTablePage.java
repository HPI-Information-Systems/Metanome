package de.metanome.frontend.client.results;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.TextColumn;

import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtUCCResult;
import de.metanome.frontend.client.services.ResultAnalyzerExecutorRestService;

import java.util.ArrayList;
import java.util.List;

/**
 * UCC ranking table page providing a simple solution to create paginated tables by overwriting the template methods
 *
 * Created by Alexander Spivak on 11.03.2015.
 */

public class UCCResultsRankingTablePage extends AbstractRankingTablePage<GwtUCCResult> {

  /**
   * Constructs the page for given algorithm execution
   *
   * @param executionID ID of the algorithm execution for which the ranks should be shown
   */
  protected UCCResultsRankingTablePage(long executionID) {
    super(executionID);
  }

  @Override
  protected AbstractRankingAsyncDataProvider<GwtUCCResult> getDataProvider() {
    return new UCCRankingAsyncDataProvider();
  }

  @Override
  protected int getElementsPerPage() {
    return 10;
  }

  @Override
  protected String getTableTitle() {
    return "UCC ranking table";
  }

  @Override
  protected List<String> initializeColumns(boolean fullTable) {
    List<String> columnNames = new ArrayList<>();

    // UCC column
    TextColumn<GwtUCCResult> uccColumn = new TooltipTextColumn<GwtUCCResult>() {

      @Override
      public String getValue(GwtUCCResult gwtUCCResult) {
        return gwtUCCResult.getUcc().getValue();
      }

      @Override
      protected String getTooltip(GwtUCCResult gwtUCCResult) {
        return gwtUCCResult.getUcc().getTooltip();
      }
    };

    this.table.addColumn(uccColumn, "UCC");
    columnNames.add("UCCAsString");

    TextColumn<GwtUCCResult> uccLengthColumn = new TextColumn<GwtUCCResult>() {
      @Override
      public String getValue(GwtUCCResult tuple) {
        return Double.valueOf(tuple.getUccRank().getLength()).toString();
      }
    };
    this.table.addColumn(uccLengthColumn, "Length");
    columnNames.add("rank.length");

    TextColumn<GwtUCCResult> uccAvgColumn = new TextColumn<GwtUCCResult>() {
      @Override
      public String getValue(GwtUCCResult tuple) {
        return Double.valueOf(tuple.getUccRank().getAverageOccurrence()).toString();
      }
    };
    this.table.addColumn(uccAvgColumn, "Average occurrence");
    columnNames.add("rank.averageOccurrence");

    if(fullTable) {
      TextColumn<GwtUCCResult> uccMinColumn = new TextColumn<GwtUCCResult>() {
        @Override
        public String getValue(GwtUCCResult tuple) {
          return Double.valueOf(tuple.getUccRank().getMin()).toString();
        }
      };
      this.table.addColumn(uccMinColumn, "Minimum");
      columnNames.add("rank.min");

      TextColumn<GwtUCCResult> uccMaxColumn = new TextColumn<GwtUCCResult>() {
        @Override
        public String getValue(GwtUCCResult tuple) {
          return Double.valueOf(tuple.getUccRank().getMax()).toString();
        }
      };
      this.table.addColumn(uccMaxColumn, "Maximum");
      columnNames.add("rank.max");

      TextColumn<GwtUCCResult> uccRandomnessColumn = new TextColumn<GwtUCCResult>() {
        @Override
        public String getValue(GwtUCCResult tuple) {
          return Double.valueOf(tuple.getUccRank().getRandomness()).toString();
        }
      };
      this.table.addColumn(uccRandomnessColumn, "Randomness");
      columnNames.add("rank.randomness");
    }

    // Set all columns as sortable
    for(int i=0; i < table.getColumnCount(); i++){
      table.getColumn(i).setSortable(true);
      table.getColumn(i).setDefaultSortAscending(true);
    }

    return columnNames;
  }

  @Override
  protected void recomputeRankings() {
    // Make a call to the backend to recompute the rankings
    ResultAnalyzerExecutorRestService
        analyzerExecutorRestService = GWT.create(ResultAnalyzerExecutorRestService.class);
    analyzerExecutorRestService.dataDependentAnalysis(new GwtLong(executionID), getAdditionalRankingsCallback());
  }
}
