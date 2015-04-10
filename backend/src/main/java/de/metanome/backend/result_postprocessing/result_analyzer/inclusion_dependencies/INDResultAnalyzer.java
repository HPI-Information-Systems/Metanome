package de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies;

import com.github.slugify.Slugify;

import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.resources.GwtINDResultResource;
import de.metanome.backend.result_postprocessing.io_helper.InputAnalyzer;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationPrinter;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PathUnifier;
import de.metanome.backend.result_postprocessing.visualizing_utils.ftl_template_usage.GoogleChartsPrinter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements experimental methods for ranking, grouping and filtering of INDs.
 *
 * Created by Daniel on 02.03.2015.
 */
public class INDResultAnalyzer extends ResultAnalyzer {

  //<editor-fold desc="Constants">

  private static final String IND_RANKING_RESULT_DIR = PathUnifier.combinePaths(BASE_RESULT_DIR, "inds");

  //</editor-fold>

  private List<TableInformation> tableInformationList;

  //Map table name to input analyzer ID to quickly find which input analyzer/table information belongs to which Inclusion Dependency
  Map<String, Integer> tableNameIndex = new HashMap<>();
  //Workaround for algorithms like SPIDER and BINDER that only use the first part of the tablen ame as identifier
  //put the first part of the name here to find the correct ID
  Map<String, Integer> tableNameWorkaroundIndex = new HashMap<>();


  //<editor-fold desc="Constructor">
  public INDResultAnalyzer(){
    super();
    // Ensure that the output directory exists
    GoogleChartsPrinter.createDirectory(IND_RANKING_RESULT_DIR);
  }
  //</editor-fold>

  @Override
  protected void analyzeResultsWithoutTupleData(List<Result> oldResults) {

    List<INDResult> newResults = createNewINDs(oldResults, false);

    rankINDsWithoutData(newResults);

    persistResults(newResults);
  }

  private void rankINDsWithoutData(List<INDResult> newResults) {
    INDRanking ranking = new INDRanking(newResults, this.tableInformationList);
    ranking.calculateDataIndependentRankings();
  }

  @Override
  protected void analyzeResultsWithTupleData(List<Result> oldResults) {

    List<INDResult> newResults = createNewINDs(oldResults, true);

    rankINDsWithData(newResults);

    persistResults(newResults);
  }

  /**
   * Creates a file header of the postprocessing results output file
   *
   * @param useRowData Determines whether the data-dependent rankings will be printed
   * @param csvDelimiter CSV delimiter of the output file
   * @return Returns a file header of the postprocessing results output file
   */
  private String getFileHeader(boolean useRowData, char csvDelimiter){
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("Dependant").append(csvDelimiter);
    stringBuilder.append("Referenced").append(csvDelimiter);
    stringBuilder.append("Dependant Size Ratio").append(csvDelimiter);
    stringBuilder.append("Referenced Size Ratio").append(csvDelimiter);
    stringBuilder.append("Dependant Column Occurrences").append(csvDelimiter);
    stringBuilder.append("Referenced Column Occurrences").append(csvDelimiter);

    if(useRowData) {
      stringBuilder.append("Dependant Constancy Ratio").append(csvDelimiter);
      stringBuilder.append("Referenced Constancy Ratio").append(csvDelimiter);
    }

    return stringBuilder.toString();
  }

  /**
   * Creates a string representation of given IND result for file output
   *
   * @param indResult IND result to be printed
   * @param useRowData Determines whether the data-dependent rankings will be printed
   * @param csvDelimiter CSV delimiter of the output file
   * @return Returns a string representation of given FD result for file output
   */
  private String getFileEntry(INDResult indResult, boolean useRowData, char csvDelimiter){

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(ColumnCombinationPrinter.prettyPrint(indResult.getDependant().getColumns(),
                                                              ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                              ",", true,
                                                              this.tableInformationList.get(indResult.getDependant().getInputID()), true))

                 .append(csvDelimiter);
    stringBuilder.append(ColumnCombinationPrinter.prettyPrint(indResult.getReferenced().getColumns(),
                                                              ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                              ",", true,
                                                              this.tableInformationList.get(indResult.getReferenced().getInputID()), true))

                 .append(csvDelimiter);


    stringBuilder.append(indResult.getRank().getDependantSizeRatio()).append(csvDelimiter);
    stringBuilder.append(indResult.getRank().getReferencedSizeRatio()).append(csvDelimiter);
    stringBuilder.append(indResult.getRank().getDependantColumnOccurrence()).append(csvDelimiter);
    stringBuilder.append(indResult.getRank().getReferencedColumnOccurrence()).append(csvDelimiter);

    if(useRowData) {
      stringBuilder.append(indResult.getRank().getDependantConstancyRatio()).append(csvDelimiter);
      stringBuilder.append(indResult.getRank().getReferencedConstancyRatio()).append(csvDelimiter);

    }

    return stringBuilder.toString();
  }


  /**
   * Prints the persisted results of postprocessing to file
   *
   * @param useRowData Determines whether the data-dependent rankings will be printed
   */
  @Override
  protected void printResultsToFile(boolean useRowData) {
    // Compute the file name and path
    String fileName = null;
    try {
      fileName = (new Slugify()).slugify(execution.getAlgorithm().getFileName())
                 + "_" + execution.getId() + "_" +
                 (useRowData ? "withData" : "withoutData") +  ".csv";
    } catch (IOException e) {
      e.printStackTrace();
    }
    String filePath = PathUnifier.combinePaths(IND_RANKING_RESULT_DIR, fileName);

    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(filePath));

      // Print the header
      out.write(getFileHeader(useRowData, ';'));
      out.newLine();

      // Print persisted results
      for(INDResult indResult : retrieveResults()){
        out.write(getFileEntry(indResult, useRowData, ';'));
        out.newLine();
      }

      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void rankINDsWithData(List<INDResult> newResults) {
    INDRanking ranking = new INDRanking(newResults, this.tableInformationList);
    ranking.calculateDatDependentRankings();
  }

  @Override
  protected void analyzeResults(List<Result> oldResults) {
  }

  private List<INDResult> createNewINDs(List<Result> oldResults, boolean useRowData) {
    List<INDResult> result;
    tableInformationList = new ArrayList<>();
    Integer i = 0;
    for (RelationalInputGenerator generator : relationalInputGenerators){
      InputAnalyzer analyzer = new InputAnalyzer(generator, useRowData);

      String tableName = analyzer.getTableInformation().getTableName();
      tableNameIndex.put(tableName, i);
      //workaround
      String truncatedTableName = tableName.split("\\.")[0];
      tableNameWorkaroundIndex.put(truncatedTableName, i);
      tableInformationList.add(analyzer.getTableInformation());
      i++;
    }

    result = retrieveResults();
    if(result == null || result.isEmpty()) {
      result =
          INDResult.createFromResults((List<InclusionDependency>) (List<?>) oldResults,
                                      tableInformationList,
                                      tableNameIndex, tableNameWorkaroundIndex);
    }
    return result;
  }

  /**
   * Returns all IND results for the regarded execution stored in the database
   *
   * @return Returns all IND results for the regarded execution stored in the database
   */
  private List<INDResult> retrieveResults(){
    return GwtINDResultResource.retrieveResults(this.execution.getId());
  }

  private void persistResults(List<INDResult> results) {

    for(INDResult result : results){
      result.update(this.tableInformationList);
    }
    GwtINDResultResource.storeResults(results, this.execution.getId());
  }
}
