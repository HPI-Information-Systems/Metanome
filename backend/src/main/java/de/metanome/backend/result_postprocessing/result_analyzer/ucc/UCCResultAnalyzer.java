package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import com.github.slugify.Slugify;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.resources.GwtUCCResultResource;
import de.metanome.backend.result_postprocessing.io_helper.InputAnalyzer;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationPrinter;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PathUnifier;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.calculation.UCCValueCalculation;
import de.metanome.backend.result_postprocessing.visualizing_utils.JSONPrinter;
import de.metanome.backend.result_postprocessing.visualizing_utils.ftl_template_usage.GoogleChartsPrinter;
import de.metanome.backend.results_db.Execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tommy Neubert on 10.12.2014.
 */
public class UCCResultAnalyzer extends ResultAnalyzer {

  private static final String UCC_RANKING_RESULT_DIR = PathUnifier.combinePaths(BASE_RESULT_DIR, "uccs");

    private static UCCRanking.RANKING rankingAlgo = UCCRanking.RANKING.MIN_MAX;

    private InputAnalyzer inputAnalyzer;

  //<editor-fold desc="Constructor">
  public UCCResultAnalyzer(){
    super();
    // Ensure that the output directory exists
    GoogleChartsPrinter.createDirectory(UCCResultAnalyzer.UCC_RANKING_RESULT_DIR);
  }
  //</editor-fold>

  @Override
  protected void analyzeResultsWithoutTupleData(List<Result> oldResults) {
      this.inputAnalyzer = new InputAnalyzer(relationalInputGenerators.get(0), false);
      List<ColumnCombination> newResults = UCCAnalyzer.createFromResult(oldResults, this.inputAnalyzer.getTableInformation().getColumnCount(), inputAnalyzer.getTableInformation());

    this.inputAnalyzer = new InputAnalyzer(relationalInputGenerators.get(0), false);

    List<UCCResult> uccResults = retrieveResults();
    if(uccResults == null || uccResults.isEmpty()) {
      List<ColumnCombination> newResult = UCCAnalyzer.createFromResult(oldResults,
                                                                       this.inputAnalyzer
                                                                           .getTableInformation()
                                                                           .getColumnCount(),
                                                                       inputAnalyzer
                                                                           .getTableInformation());
      uccResults = new ArrayList<>(newResult.size());

        UCCValueCalculation calculation = new UCCValueCalculation(this.inputAnalyzer.getTableInformation(), newResults);

      for(ColumnCombination result : newResult){
        UCCResult uccResult = new UCCResult(result);

        uccResult.setRank(new UCCRank());
        uccResult.getRank().setLength(result.getColumnCount());
        uccResult.getRank().setAverageOccurrence((Double) calculation.calculate(result, false).get(UCCValueCalculation.VALUES.AVG_OCC));

        uccResults.add(uccResult);
      }

    }

    persistResults(uccResults);
    /*UCCRanking ranking = new UCCRanking(rankingAlgo, newResult, this.inputAnalyzer.getTableInformation());

    List<UCCRanking.RankingResult> result = ranking.getResult();
    Map<Long, List<UCCRanking.RankingResult>> finalRankings = new HashMap<Long, List<UCCRanking.RankingResult>>();
    for(UCCRanking.RankingResult rankRes : result) {
      if(!finalRankings.containsKey(rankRes.getRank())) {
        finalRankings.put(rankRes.getRank(), new LinkedList<UCCRanking.RankingResult>());
      }
      finalRankings.get(rankRes.getRank()).add(rankRes);
    }*/

  }

  @Override
  protected void analyzeResultsWithTupleData(List<Result> oldResults) {
    this.inputAnalyzer = new InputAnalyzer(relationalInputGenerators.get(0), true);

    List<UCCResult> uccResults = retrieveResults();
    UCCValueCalculation calculation = new UCCValueCalculation(uccResults, this.inputAnalyzer.getTableInformation());

    for(UCCResult uccResult : uccResults){
        Map<UCCValueCalculation.VALUES, Object> values = calculation.calculate(uccResult.getUcc(), true);
        uccResult.getRank().setMax((Double)values.get(UCCValueCalculation.VALUES.MAX));
      uccResult.getRank().setMin((Double)values.get(UCCValueCalculation.VALUES.MIN));
      uccResult.getRank().setRandomness((Double)values.get(UCCValueCalculation.VALUES.RND));
    }

    persistResults(uccResults);
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

    stringBuilder.append("UCC").append(csvDelimiter);
    stringBuilder.append("Length").append(csvDelimiter);
    stringBuilder.append("Average Occurrence").append(csvDelimiter);

    if(useRowData) {
      stringBuilder.append("Min").append(csvDelimiter);
      stringBuilder.append("Max").append(csvDelimiter);
      stringBuilder.append("Randomness").append(csvDelimiter);
    }

    return stringBuilder.toString();
  }

  /**
   * Creates a string representation of given UCC result for file output
   *
   * @param uccResult UCC result to be printed
   * @param useRowData Determines whether the data-dependent rankings will be printed
   * @param csvDelimiter CSV delimiter of the output file
   * @return Returns a string representation of given FD result for file output
   */
  private String getFileEntry(UCCResult uccResult, boolean useRowData, char csvDelimiter){

    TableInformation tableInformation = this.inputAnalyzer.getTableInformation();

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(ColumnCombinationPrinter.prettyPrint(uccResult.getUcc(),
                                                              ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                              ",", true, tableInformation, true))
        .append(csvDelimiter);

    stringBuilder.append(uccResult.getRank().getLength()).append(csvDelimiter);
    stringBuilder.append(uccResult.getRank().getAverageOccurrence()).append(csvDelimiter);

    if(useRowData) {
      stringBuilder.append(uccResult.getRank().getMin()).append(csvDelimiter);
      stringBuilder.append(uccResult.getRank().getMax()).append(csvDelimiter);
      stringBuilder.append(uccResult.getRank().getRandomness()).append(csvDelimiter);
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
    String filePath = PathUnifier.combinePaths(UCC_RANKING_RESULT_DIR, fileName);

    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(filePath));

      // Print the header
      out.write(getFileHeader(useRowData, ';'));
      out.newLine();

      // Print persisted results
      for(UCCResult uccResult : retrieveResults()){
        out.write(getFileEntry(uccResult, useRowData, ';'));
        out.newLine();
      }

      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
    protected void analyzeResults(List<Result> oldResults) {
        this.inputAnalyzer = new InputAnalyzer(relationalInputGenerators.get(0), true);
        List<ColumnCombination> newResult = UCCAnalyzer.createFromResult(oldResults, this.inputAnalyzer.getTableInformation().getColumnCount(), inputAnalyzer.getTableInformation());

        UCCRanking ranking = new UCCRanking(rankingAlgo, newResult, this.inputAnalyzer.getTableInformation());
        List<UCCRanking.RankingResult> result = ranking.getResult();
        Map<Long, List<UCCRanking.RankingResult>> finalRankings = new HashMap<Long, List<UCCRanking.RankingResult>>();
        for(UCCRanking.RankingResult rankRes : result) {
            if(!finalRankings.containsKey(rankRes.getRank())) {
                finalRankings.put(rankRes.getRank(), new LinkedList<UCCRanking.RankingResult>());
            }
            finalRankings.get(rankRes.getRank()).add(rankRes);
        }
        GoogleChartsPrinter
            .uccRankingAsTable(result, this.inputAnalyzer.getTableInformation(), "io/UCCAnalyzer");
        this.print(finalRankings);
    }

    private void print(Map<Long, List<UCCRanking.RankingResult>> result) {
        for(Map.Entry<Long, List<UCCRanking.RankingResult>> entry : result.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println("-----");
            for(UCCRanking.RankingResult rr : entry.getValue()) {
                System.out.println("\t" + rr.getUcc() + " - " + rr.getNormalizedValue());
                for(Integer i : rr.getUcc().getColumnIndices()) {
                    System.out.println("\t\t" + inputAnalyzer.getTableInformation().getColumn(i).getColumnName());
                }
            }
            System.out.println();
        }
    }

  private List<UCCResult> retrieveResults(){
    return GwtUCCResultResource.retrieveResults(this.execution.getId());
  }

  private void persistResults(List<UCCResult> results) {

    for(UCCResult result : results){
      result.update(this.inputAnalyzer.getTableInformation());
    }

    GwtUCCResultResource.storeResults(results, this.execution.getId());
  }

    public void createPieChart(Execution ex, long columnId) {
        this.execution = ex;

        // Extract inputs to recreate the table information
        try {
            extractInputs(execution);
        } catch (AlgorithmConfigurationException e) {
            e.printStackTrace();
        }

        // Perform input data analysis
        this.inputAnalyzer = new InputAnalyzer(relationalInputGenerators.get(0), true);
        TableInformation tableInformation = this.inputAnalyzer.getTableInformation();

        // Retrieve the algorithm results
        List<Result> oldResults = this.extractResults(this.execution);

        // Transform the algorithm result structure into the result analyzer one
        // Metanome FDResult -> FDResultAnalyzer FDResult
//        List<FDResult> transformedResults = FDResult.createFromResults(oldResults, tableInformation);
//
//        // Combine all FDs with the same determinant
//        List<FDResult> compressedResults = mergeFDs(transformedResults);
//
//        // Compute the dependant index
//        Map<ColumnCombination, List<ColumnCombination>> dependantsIndex = indexDependants(compressedResults);
//
//        // Get the storage directory path
//        String currentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//        // Print to JSON file
//        JSONPrinter
//                .printFDsAsPrefixTreeJSON(currentPath + "../../visualization/FDResultAnalyzer/",
//                        transformedResults,
//                        dependantsIndex,
//                        tableInformation);
    }
}
