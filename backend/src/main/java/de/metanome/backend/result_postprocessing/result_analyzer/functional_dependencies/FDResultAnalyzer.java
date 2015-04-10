package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;

import com.github.slugify.Slugify;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.resources.GwtFDResultResource;
import de.metanome.backend.result_postprocessing.io_helper.InputAnalyzer;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationLexicalComparator;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationPrinter;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationSizeComparator;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PathUnifier;
import de.metanome.backend.result_postprocessing.visualizing_utils.JSONPrinter;
import de.metanome.backend.result_postprocessing.visualizing_utils.ftl_template_usage.GoogleChartsPrinter;
import de.metanome.backend.results_db.Execution;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements experimental methods for ranking, grouping and filtering of FDs.
 *
 * Created by Alexander Spivak and Daniel Roeder on 12.11.2014.
 */
public class FDResultAnalyzer extends ResultAnalyzer {

  //<editor-fold desc="Constants">

  private static final String FD_RANKING_RESULT_DIR = PathUnifier.combinePaths(BASE_RESULT_DIR, "fds");
  private static final String OUTPUT_DIRECTORY_PATH = PathUnifier.unifyPath("io/FDResultAnalyzer");

  //</editor-fold>

  private InputAnalyzer inputAnalyzer = null;

  //<editor-fold desc="Constructor">
  public FDResultAnalyzer(){
    super();
    // Ensure that the output directory exists
    GoogleChartsPrinter.createDirectory(FDResultAnalyzer.FD_RANKING_RESULT_DIR);
    GoogleChartsPrinter.createDirectory(FDResultAnalyzer.OUTPUT_DIRECTORY_PATH);
  }
  //</editor-fold>

  //<editor-fold desc="FD maximization and minimization methods">
  /**
   * Merges FDs with the same determinant together
   * Side-effect: sorts the provided list!!!
   * @param fdResults List of FDs which should be merged
   * @return Returns a list of FD where each determinant appears only one time
   */
  private List<FDResult> mergeFDs(List<FDResult> fdResults){

    Collections.sort(fdResults);

    List<FDResult> mergedFDs = new ArrayList<>();

    if(fdResults.isEmpty()){
      return mergedFDs;
    }

    // Define a size comparator for lexical determinant comparison
    ColumnCombinationSizeComparator comparator = new ColumnCombinationSizeComparator();
    // Start iteration from first result
    FDResult merger = fdResults.get(0);

    // Walk through the sorted list as long the determinants are equal and merge FDs together
    for(FDResult fdResult : fdResults){
      if(comparator.compare(merger.getDeterminant(), fdResult.getDeterminant()) == 0){
        merger = merger.merge(fdResult);
      }
      else{
        mergedFDs.add(merger);
        merger = fdResult;
      }
    }
    // Add the last merge result to the result list
    mergedFDs.add(merger);

    return mergedFDs;
  }

  /**
   * Computes the maximal dependant (all dependent, non-trivial columns) for the given determinant
   * @param determinant Determinant of the FD which should be maximized
   * @param dependant Current maximized dependant of the FD
   * @param index Helper structure for the computation
   * @return Returns maximized dependant for the given determinant
   */
  private ColumnCombination recursiveMaxDependants(ColumnCombination determinant, ColumnCombination dependant,
                                                   Map<ColumnCombination, ColumnCombination> index){
    // Prepare the result holder
    ColumnCombination result = dependant.copy();

    // Iterate over all columns of the determinant
    for (Integer column : determinant){
      // Remove the column from determinant and get the maximal dependant of that new determinant
      ColumnCombination tester = determinant.copy();
      tester.set(column, false);
      // If tester contained in index, it has already been maximized and no recursion is needed
      if(index.containsKey(tester)) {
        result.or(index.get(tester));
      }
      else{
        //otherwise, we have to go to the next level
        //don't recurse at the lowest level
        if(tester.getColumnCount() > 1) {
          result.or(recursiveMaxDependants(tester, result, index));
        }
      }
    }

    return result;
  }

  /**
   * Computes a list of maximized FDs from given list of FDs
   * @param fdResults List of non-maximized FDs
   * @return Returns a list of maximized FDs
   */
  private List<FDResult> computeMaxDependants(List<FDResult> fdResults){
    // Perform a deep copy of FDResults because recursive algorithm is in-place
    List<FDResult> maximizedFDResults = new ArrayList<>(fdResults.size());
    for (FDResult fd : fdResults){
      maximizedFDResults.add(fd.copy());
    }

    // Use dynamic programing
    Collections.sort(maximizedFDResults);

    //  Used only to speed up the algorithm (alternative is search in list)
    // ATTENTION: if you use the list search do not rely on sorting because it will break after the dependant change
    Map<ColumnCombination, ColumnCombination> detDepIndex = new HashMap<>();

    for(FDResult fd : maximizedFDResults){
      // Compute the maximal dependent and store the result
      fd.getDependant().or(recursiveMaxDependants(fd.getDeterminant(),fd.getDependant(),detDepIndex));
      // Update the index
      detDepIndex.put(fd.getDeterminant(), fd.getDependant());
    }

    return maximizedFDResults;
  }
  //</editor-fold>

  //<editor-fold desc="FD index methods">

  /**
   * Creates a reverted index on FD dependants
   * @param fdResults List of FDs which are relevant for the index
   * @return Returns a mapping: for each dependant a sorted list of all determinants
   */
  private Map<ColumnCombination, List<ColumnCombination>> indexDependants(List<FDResult> fdResults){
    Map<ColumnCombination, List<ColumnCombination>> result = new HashMap<>();

    // Iterate over FDs and add the determinants to the related dependant list
    for(FDResult fdResult : fdResults){
      List<ColumnCombination> dependantColumns = fdResult.getDependant().getColumnsAsCombinations();
      for(ColumnCombination dependantColumn : dependantColumns) {
        if (!result.containsKey(dependantColumn)) {
          result.put(dependantColumn, new ArrayList<ColumnCombination>());
        }
        result.get(dependantColumn).add(fdResult.getDeterminant());
      }
    }

    // Sort lists of the index in lexical order
    ColumnCombinationSizeComparator comparator = new ColumnCombinationSizeComparator();
    for(Map.Entry<ColumnCombination, List<ColumnCombination>> indexEntry : result.entrySet()){
      Collections.sort(indexEntry.getValue(), comparator);
    }

    return result;
  }

  //</editor-fold>

  //<editor-fold desc="FD bucket methods">

  /**
   * Bucketize the FDs: Each bucket contains all FDs which coverage the same columns (LHS + RHS)
   * @param fds List of functional dependencies to be bucketized
   * @return Returns an index of column combinations pointing to all FDs which have that columns
   */
  private Map<ColumnCombination,List<FDResult>> groupByColumns(List<FDResult> fds){
    // Prepare the result holder
    Map<ColumnCombination,List<FDResult>> result = new HashMap<>();
    // Iterate over all FDs
    for (FDResult fd : fds){
      // Compute the union of dependant and determinant
      ColumnCombination union = fd.getDependant().merge(fd.getDeterminant());

      // Add FD list if not already there
      if(!result.containsKey(union)){
        result.put(union, new ArrayList<FDResult>());
      }

      // Add the result
      result.get(union).add(fd);
    }

    // Sort inside the buckets
    for (Map.Entry<ColumnCombination,List<FDResult>> entry : result.entrySet()){
      Collections.sort(entry.getValue());
    }

    return result;
  }

  //</editor-fold>

  //<editor-fold desc="Print to file methods">

  /**
   * Prints given functional dependencies to file
   * @param fds List of functional dependencies to print
   * @param filePath Path to the file inside the output directory
   */
  private void printFDs(List<FDResult> fds, String filePath){

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(PathUnifier.combinePaths(
          FDResultAnalyzer.OUTPUT_DIRECTORY_PATH, filePath)));
      for (FDResult fd : fds) {
        writer.write(fd.toString(this.inputAnalyzer.getTableInformation()) + System.lineSeparator());
      }
      writer.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  /**
   * Prints the given bucketized FDs into a specified file.
   * @param buckets The bucketized FDs to be printed
   * @param filePath Path to the file inside the output directory
   * @param ignoreSmallBuckets If true, all buckets with size = 1 will be ignored
   */
  private void printBucketizedFDs(Map<ColumnCombination,List<FDResult>> buckets, String filePath, Boolean ignoreSmallBuckets){

    // Prepare a sorted key set to print buckets in sorted order
    List<Map.Entry<ColumnCombination, List<FDResult>>> columnCombinationList = new ArrayList<>(buckets.entrySet());
    Collections.sort(columnCombinationList, new FDBucketComparator());

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(PathUnifier.combinePaths(
          FDResultAnalyzer.OUTPUT_DIRECTORY_PATH, filePath)));
      // Iterate in sorted way over buckets
      for (Map.Entry<ColumnCombination, List<FDResult>> cc : columnCombinationList) {
        // Get bucket
        List<FDResult> dependencyList = cc.getValue();
        // Print if it should not be ignored
        if (dependencyList.size() > 1 || !ignoreSmallBuckets) {
          String ccString = ColumnCombinationPrinter.prettyPrint(cc.getKey(),
                                                                 ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                                 ",", true,
                                                                 this.inputAnalyzer.getTableInformation());

          String toFile = "Bucket: " + ccString + System.lineSeparator();

          for (FDResult dependency : dependencyList) {
            toFile += "\t" + dependency.toString(this.inputAnalyzer.getTableInformation()) + System.lineSeparator();
          }

          toFile += "================================" + System.lineSeparator();

          writer.write(toFile);
        }
      }

      writer.close();

    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  /**
   * Prints the dependants index to given file
   * @param dependantsIndex Dependant index to be printed
   * @param filePath Path to the file inside the output directory
   */
  private void printDependantsIndex(Map<ColumnCombination, List<ColumnCombination>> dependantsIndex,
                                    String filePath) {

    // Prepare a sorted key set to provide a sorted index
    List<ColumnCombination> indexValues = new ArrayList<>(dependantsIndex.keySet());
    Collections.sort(indexValues, new ColumnCombinationSizeComparator());

    // Output all FDs pointing to a column to file
    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(PathUnifier.combinePaths(
          FDResultAnalyzer.OUTPUT_DIRECTORY_PATH, filePath)));

      // Iterate over all index values (columns as column combinations)
      for (ColumnCombination indexValue : indexValues) {
        // Column index of the current index value
        int columnIndex = indexValue.getColumnIndices().get(0);
        // Output the dependant column
        writer.write(String.format("%s dependant for %d FDs (uniqueness: %f)",
                                   this.inputAnalyzer.getTableInformation().getColumn(columnIndex).getColumnName(),
                                   dependantsIndex.get(indexValue).size(),
                                   this.inputAnalyzer.getTableInformation().getColumn(columnIndex).getUniquenessRate()
                                  ) + System.lineSeparator());

        // Output all determinants pointing to the current dependant column
        for (ColumnCombination determinant : dependantsIndex.get(indexValue)) {
          String determinantString = ColumnCombinationPrinter.prettyPrint(determinant,
                                                                          ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                                          ",", true,
                                                                          this.inputAnalyzer.getTableInformation());

          writer.write("\t\t<- " + determinantString + System.lineSeparator());
        }
      }

      writer.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  //</editor-fold>

  //<editor-fold desc="FD analyzing methods">
  /**
   * Bucketizes the transformed and maximized results and prints the results to files
   * @param transformedResults Transformed results to be bucketized and printed
   * @param maximizedResults Maximized results to be bucketized and printed
   */
  private void bucketizeFDs(List<FDResult> transformedResults, List<FDResult> maximizedResults){
    // Create buckets based on union of determinant and dependant in FDs
    Map<ColumnCombination, List<FDResult>> resultBuckets = groupByColumns(transformedResults);
    Map<ColumnCombination,List<FDResult>> maximizedResultBuckets = groupByColumns(maximizedResults);

    // Output buckets to file
    this.printBucketizedFDs(resultBuckets, "FD_buckets.txt", true);
    this.printBucketizedFDs(maximizedResultBuckets, "FD_minimized_buckets.txt", true);
  }

  /**
   * Ranks the provided maximized results without using tuple data
   *
   * @param compressedResults Compressed results to be used for computation of additional columns
   * @param maximizedResults Maximized results to be ranked and printed
   */
  private void rankFDsWithoutData(List<FDResult> compressedResults, List<FDResult> maximizedResults){
    TableInformation tableInformation = this.inputAnalyzer.getTableInformation();
    // Calculate the rankings
    FDRanking ranking = new FDRanking(maximizedResults, tableInformation);
    ranking.calculateDataIndependentRankings();
    ranking.computeAdditionalColumns(compressedResults);

    // Persist results
    persistResults(ranking.getFdList());
  }

  /**
   * Ranks the provided maximized results using tuple data
   *
   * @param compressedResults Compressed results to be used for computation of additional columns
   * @param maximizedResults Maximized results to be ranked and printed
   */
  private void rankFDsWithData(List<FDResult> compressedResults, List<FDResult> maximizedResults){
    TableInformation tableInformation = this.inputAnalyzer.getTableInformation();
    // Calculate the rankings
    FDRanking ranking = new FDRanking(maximizedResults, tableInformation);
    ranking.calculateDataDependentRankings();
    ranking.computeAdditionalColumns(compressedResults);

    // Persist results
    persistResults(ranking.getFdList());
  }

  /**
   * Ranks the provided maximized results and prints them to file
   * @param compressedResults Compressed results to be used for computation of additional columns
   * @param maximizedResults Maximized results to be ranked and printed
   */
  private void rankFDs(List<FDResult> compressedResults, List<FDResult> maximizedResults){
    TableInformation tableInformation = this.inputAnalyzer.getTableInformation();
    // Calculate the rankings
    FDRanking ranking = new FDRanking(maximizedResults, tableInformation);
    ranking.calculateRankings();
    ranking.computeAdditionalColumns(compressedResults);
    ranking.calculateSpearmanCoefficient(PathUnifier.combinePaths(
        FDResultAnalyzer.OUTPUT_DIRECTORY_PATH,
        "Spearman_Coefficient.csv"));

    GoogleChartsPrinter.fdRankingsAsTable(ranking, tableInformation, FDResultAnalyzer.OUTPUT_DIRECTORY_PATH);

    persistResults(ranking.getFdList());
  }

  @Override
  protected void analyzeResultsWithoutTupleData(List<Result> oldResults) {
    analyzeResults(oldResults, false);
  }

  @Override
  protected void analyzeResultsWithTupleData(List<Result> oldResults) {
    analyzeResults(oldResults, true);
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

    stringBuilder.append("Determinant").append(csvDelimiter);
    stringBuilder.append("Dependant").append(csvDelimiter);
    stringBuilder.append("Additional Columns").append(csvDelimiter);
    stringBuilder.append("Determinant Size Ratio").append(csvDelimiter);
    stringBuilder.append("Dependant Size Ratio").append(csvDelimiter);
    stringBuilder.append("Coverage").append(csvDelimiter);

    if(useRowData) {
      stringBuilder.append("Determinant Constancy Ratio").append(csvDelimiter);
      stringBuilder.append("Dependant Constancy Ratio").append(csvDelimiter);
      stringBuilder.append("Pollution").append(csvDelimiter);
      stringBuilder.append("Minimum Pollution Column").append(csvDelimiter);
      stringBuilder.append("Information Gain Bytes").append(csvDelimiter);
      stringBuilder.append("Information Gain Cells").append(csvDelimiter);
    }

    return stringBuilder.toString();
  }

  /**
   * Creates a string representation of given FD result for file output
   *
   * @param fdResult FD result to be printed
   * @param useRowData Determines whether the data-dependent rankings will be printed
   * @param csvDelimiter CSV delimiter of the output file
   * @return Returns a string representation of given FD result for file output
   */
  private String getFileEntry(FDResult fdResult, boolean useRowData, char csvDelimiter){

    TableInformation tableInformation = this.inputAnalyzer.getTableInformation();

    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append(ColumnCombinationPrinter.prettyPrint(fdResult.getDeterminant(),
                                                              ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                              ",", true, tableInformation, true))
                 .append(csvDelimiter);
    stringBuilder.append(ColumnCombinationPrinter.prettyPrint(fdResult.getDependant(),
                                                              ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                              ",", true, tableInformation, true))
                 .append(csvDelimiter);
    stringBuilder.append(ColumnCombinationPrinter.prettyPrint(fdResult.getAdditionalColumns(),
                                                              ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                              ",", true, tableInformation, true))
                 .append(csvDelimiter);

    stringBuilder.append(fdResult.getRank().getDeterminantSizeRatio()).append(csvDelimiter);
    stringBuilder.append(fdResult.getRank().getDependantSizeRatio()).append(csvDelimiter);
    stringBuilder.append(fdResult.getRank().getCoverage()).append(csvDelimiter);

    if(useRowData) {
      stringBuilder.append(fdResult.getRank().getDeterminantConstancyRatio()).append(csvDelimiter);
      stringBuilder.append(fdResult.getRank().getDependantConstancyRatio()).append(csvDelimiter);
      stringBuilder.append(fdResult.getRank().getPollution()).append(csvDelimiter);
      stringBuilder.append(fdResult.getRank().getMinPollutionColumn()).append(csvDelimiter);
      stringBuilder.append(fdResult.getRank().getInformationGainBytes()).append(csvDelimiter);
      stringBuilder.append(fdResult.getRank().getInformationGainCells()).append(csvDelimiter);
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
    String filePath = PathUnifier.combinePaths(FD_RANKING_RESULT_DIR, fileName);

    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(filePath));

      // Print the header
      out.write(getFileHeader(useRowData, ';'));
      out.newLine();

      // Print persisted results
      for(FDResult fdResult : retrieveResults()){
        out.write(getFileEntry(fdResult, useRowData, ';'));
        out.newLine();
      }

      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Implements an analysis of results based on input data and
   * some filtering/ordering operations on the result set according to analysis results.
   *
   * @param oldResults Provides access to the results created by the algorithm
   * @param useRowData Should the usage of tuple data be allowed?
   */
  private void analyzeResults(List<Result> oldResults, boolean useRowData){
    // Perform input data analysis
    this.inputAnalyzer = new InputAnalyzer(relationalInputGenerators.get(0), useRowData);
    TableInformation tableInformation = this.inputAnalyzer.getTableInformation();


    // Transform the algorithm result structure into the result analyzer one
    // Metanome FDResult -> FDResultAnalyzer FDResult
    List<FDResult> transformedResults = FDResult.createFromResults(oldResults, tableInformation);

    // Combine all FDs with the same determinant
    List<FDResult> compressedResults = mergeFDs(transformedResults);

    // Use pseudo-transitivity to maximize the dependant
    List<FDResult> maximizedResults = retrieveResults();
    if(maximizedResults== null || maximizedResults.isEmpty()) {
      maximizedResults = this.computeMaxDependants(compressedResults);
    }

    if(useRowData) {
      rankFDsWithData(compressedResults, maximizedResults);
    }
    else{
      rankFDsWithoutData(compressedResults, maximizedResults);
    }

  }

  /**
   * Manages all filtering, ranking and further FD evaluation processes
   *
   * @param oldResults Provides access to the results created by the algorithm
   */
  @Override
  protected void analyzeResults(List<Result> oldResults) {

    // Perform input data analysis
    this.inputAnalyzer = new InputAnalyzer(relationalInputGenerators.get(0), true);
    TableInformation tableInformation = this.inputAnalyzer.getTableInformation();

    // Print the input data analysis results as web page
    GoogleChartsPrinter.tableInformationAsWebPage(tableInformation, FDResultAnalyzer.OUTPUT_DIRECTORY_PATH);

    // Transform the algorithm result structure into the result analyzer one
    // Metanome FDResult -> FDResultAnalyzer FDResult
    List<FDResult> transformedResults = FDResult.createFromResults(oldResults, tableInformation);

    // Combine all FDs with the same determinant
    List<FDResult> compressedResults = mergeFDs(transformedResults);
    // Use pseudo-transitivity to maximize the dependant
    List<FDResult> maximizedResults = this.computeMaxDependants(compressedResults);

    // Output minimized FDs to file
    this.printFDs(compressedResults, "minimized-fds.txt");

    // Compute and print the FD ranking
    rankFDs(compressedResults, maximizedResults);

    // Bucketize and print the FDs
    bucketizeFDs(transformedResults, maximizedResults);

  }

  /**
   * Creates a prefix tree JSON for results of the given execution
   *
   * @param execution Execution describing the last run of a FD algorithm
   */
  public void createPrefixTree(Execution execution){
    this.execution = execution;

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
    List<FDResult> transformedResults = FDResult.createFromResults(oldResults, tableInformation);

    // Combine all FDs with the same determinant
    List<FDResult> compressedResults = mergeFDs(transformedResults);

    // Compute the dependant index
    Map<ColumnCombination, List<ColumnCombination>> dependantsIndex = indexDependants(compressedResults);

    // Get the storage directory path
    String currentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    // Print to JSON file
    JSONPrinter
        .printFDsAsPrefixTreeJSON(currentPath + "../../visualization/FDResultAnalyzer/",
                                  transformedResults,
                                  dependantsIndex,
                                  tableInformation);
  }

  //</editor-fold>

  //<editor-fold desc="Persistence methods">

  /**
   * Returns all FD results for the regarded execution stored in the database
   *
   * @return Returns all FD results for the regarded execution stored in the database
   */
  private List<FDResult> retrieveResults(){
    return GwtFDResultResource.retrieveResults(this.execution.getId());
  }

  /**
   * Stores or updates the provided list of FD results and the contained ranks
   *
   * @param results List of FD results to be persisted in the database
   */
  private void persistResults(List<FDResult> results){
    for(FDResult result : results){
      result.update(this.inputAnalyzer.getTableInformation());
    }
    GwtFDResultResource.storeResults(results, this.execution.getId());
  }

  //</editor-fold>
}
