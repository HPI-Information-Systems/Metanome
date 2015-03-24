package de.metanome.frontend.client.results;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.TextColumn;

import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtFDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper.GwtLong;
import de.metanome.frontend.client.services.ResultAnalyzerExecutorRestService;

import java.util.ArrayList;
import java.util.List;

/**
 * FD ranking table page providing a simple solution to create paginated tables by overwriting the template methods
 *
 * Created by Alexander Spivak on 04.03.2015.
 */
public class FDResultsRankingTablePage extends AbstractRankingTablePage<GwtFDResult> {

  /**
   * Constructs the page for given algorithm execution
   *
   * @param executionID ID of the algorithm execution for which the ranks should be shown
   */
  public FDResultsRankingTablePage(long executionID){
    super(executionID);
  }

  /**
   * Constructs a data provider of appropriate type
   *
   * @return Returns a constructed asynchronous data provider
   */
  @Override
  protected AbstractRankingAsyncDataProvider<GwtFDResult> getDataProvider() {
    return new FDRankingAsyncDataProvider();
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
    return "FD ranking table";
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
    TextColumn<GwtFDResult> determinantColumn = new TooltipTextColumn<GwtFDResult>() {
      @Override
      protected String getTooltip(GwtFDResult tuple) {
        return tuple.getDeterminant().getTooltip();
      }

      @Override
      public String getValue(GwtFDResult tuple) {
        return tuple.getDeterminant().getValue();
      }

    };
    this.table.addColumn(determinantColumn, "Determinant");
    columnNames.add("determinantAsString");

    // Dependant column
    TextColumn<GwtFDResult> dependantColumn = new TooltipTextColumn<GwtFDResult>() {
      @Override
      protected String getTooltip(GwtFDResult tuple) {
        return tuple.getDependant().getTooltip();
      }

      @Override
      public String getValue(GwtFDResult tuple) {
        return tuple.getDependant().getValue();
      }
    };
    this.table.addColumn(dependantColumn, "Dependant");
    columnNames.add("dependantAsString");

    // Additional columns column
    TextColumn<GwtFDResult> additionalColumnsColumn = new TooltipTextColumn<GwtFDResult>() {
      @Override
      protected String getTooltip(GwtFDResult tuple) {
        return tuple.getAdditionalColumns().getTooltip();
      }

      @Override
      public String getValue(GwtFDResult tuple) {
        return tuple.getAdditionalColumns().getValue();
      }
    };
    this.table.addColumn(additionalColumnsColumn, "Additional Columns");
    columnNames.add("additionalColumnsAsString");

    // Determinant size ratio column
    TextColumn<GwtFDResult> determinantSizeRatioColumn = new TextColumn<GwtFDResult>() {
      @Override
      public String getValue(GwtFDResult tuple) {
        return Float.valueOf(tuple.getFdRank().getDeterminantSizeRatio()).toString();
      }
    };
    this.table.addColumn(determinantSizeRatioColumn, "Determinant Size Ratio");
    columnNames.add("rank.determinantSizeRatio");

    // Dependant size ratio column
    TextColumn<GwtFDResult> dependantSizeRatioColumn = new TextColumn<GwtFDResult>() {
      @Override
      public String getValue(GwtFDResult tuple) {
        return Float.valueOf(tuple.getFdRank().getDependantSizeRatio()).toString();
      }
    };
    this.table.addColumn(dependantSizeRatioColumn, "Dependant Size Ratio");
    columnNames.add("rank.dependantSizeRatio");

    if(fullTable) {
      // Determinant constancy ratio column
      TextColumn<GwtFDResult> determinantConstancyRatioColumn = new TextColumn<GwtFDResult>() {
        @Override
        public String getValue(GwtFDResult tuple) {
          return Float.valueOf(tuple.getFdRank().getDeterminantConstancyRatio()).toString();
        }
      };
      this.table.addColumn(determinantConstancyRatioColumn, "Determinant Constancy Ratio");
      columnNames.add("rank.determinantConstancyRatio");

      // Dependant constancy ratio column
      TextColumn<GwtFDResult> dependantConstancyRatioColumn = new TextColumn<GwtFDResult>() {
        @Override
        public String getValue(GwtFDResult tuple) {
          return Float.valueOf(tuple.getFdRank().getDependantConstancyRatio()).toString();
        }
      };
      this.table.addColumn(dependantConstancyRatioColumn, "Dependant Constancy Ratio");
      columnNames.add("rank.dependantConstancyRatio");
    }

    // Coverage column
    TextColumn<GwtFDResult> coverageColumn = new TextColumn<GwtFDResult>() {
      @Override
      public String getValue(GwtFDResult tuple) {
        return Float.valueOf(tuple.getFdRank().getCoverage()).toString();
      }
    };
    this.table.addColumn(coverageColumn, "Coverage");
    columnNames.add("rank.coverage");

    if(fullTable) {
      // Pollution column
      TextColumn<GwtFDResult> pollutionColumn = new TextColumn<GwtFDResult>() {
        @Override
        public String getValue(GwtFDResult tuple) {
          return Float.valueOf(tuple.getFdRank().getPollution()).toString();
        }
      };
      this.table.addColumn(pollutionColumn, "Pollution");
      columnNames.add("rank.pollution");

      // Minimal pollution column column
      TextColumn<GwtFDResult> minPollutionColumnColumn = new TextColumn<GwtFDResult>() {
        @Override
        public String getValue(GwtFDResult tuple) {
          return tuple.getFdRank().getMinPollutionColumn();
        }
      };
      this.table.addColumn(minPollutionColumnColumn, "Min Pollution Column");
      columnNames.add("rank.minPollutionColumn");

      // Information gain in bytes column
      TextColumn<GwtFDResult> informationGainBytesColumn = new TextColumn<GwtFDResult>() {
        @Override
        public String getValue(GwtFDResult tuple) {
          return Float.valueOf(tuple.getFdRank().getInformationGainBytes()).toString();
        }
      };
      this.table.addColumn(informationGainBytesColumn, "Information Gain (Bytes)");
      columnNames.add("rank.informationGainBytes");

      // Information gain in cells column
      TextColumn<GwtFDResult> informationGainCellsColumn = new TextColumn<GwtFDResult>() {
        @Override
        public String getValue(GwtFDResult tuple) {
          return Float.valueOf(tuple.getFdRank().getInformationGainCells()).toString();
        }
      };
      this.table.addColumn(informationGainCellsColumn, "Information Gain (Cells)");
      columnNames.add("rank.informationGainCells");
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
    ResultAnalyzerExecutorRestService analyzerExecutorRestService = GWT.create(ResultAnalyzerExecutorRestService.class);
    analyzerExecutorRestService.dataDependentAnalysis(new GwtLong(executionID), getAdditionalRankingsCallback());
  }
}
