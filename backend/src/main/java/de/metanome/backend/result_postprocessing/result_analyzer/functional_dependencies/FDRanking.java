package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;

import de.metanome.backend.result_postprocessing.io_helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PLIList;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Daniel Roeder and Alexander Spivak on 26.11.2014.
 *
 * Provides calculation methods for the computation of functional dependency ranks
 */
public class FDRanking {

  //<editor-fold desc="Private attributes">

  // List of all, fully extended functional dependencies
  private List<FDResult> fdList;

  // Provides the access to the table information
  private TableInformation tableInformation;

  //</editor-fold>

  //<editor-fold desc="Getter">

  public List<FDResult> getFdList() {
    return fdList;
  }

  //</editor-fold>

  //<editor-fold desc="Constructor">

  /**
   * Creates a FD ranking computation structure
   * @param fdList List of all, fully extended functional dependencies
   * @param tableInformation Table information access
   */
  public FDRanking(List<FDResult> fdList, TableInformation tableInformation) {
    this.fdList = fdList;
    this.tableInformation = tableInformation;
  }

  //</editor-fold>

  //<editor-fold desc="Public rank calculation methods">

  /**
   * Calculates the Spearman coefficient of the rankings and prints the resulting matrix to file
   * @param filePath Path to file to which the resulting matrix should be printed
   */
  public void calculateSpearmanCoefficient(String filePath) {

    SpearmansCorrelation correlation = new SpearmansCorrelation();

    // Prepare the Spearman calculations
    List<String> rankingNames = new ArrayList<>(fdList.get(0).getRank().getRankings().keySet());
    Integer rankingCount = rankingNames.size();
    Integer fdCount = fdList.size();

    // Create the ranking matrix
    double[][] rankingArrays = new double[rankingCount][fdCount];
    // Calculate the Spearman coefficient
    for (Integer i = 0; i <= fdCount -1; i++) {
      for (Integer j = 0; j <= rankingCount - 1; j++) {
        rankingArrays[j][i] = Double.valueOf(
            fdList.get(i).getRank().getRankings().get(rankingNames.get(j)));
      }
    }

    // Write the correlation matrix to file
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
      bw.write(";");
      for (Integer i = 0; i <= rankingCount; i++){
        for (Integer j = 0 ; j <= rankingCount; j++){
          if (i == 0 && j >= 1)
            bw.write(rankingNames.get(j-1) + ";");
          else if (j == 0 && i >= 1)
            bw.write(rankingNames.get(i-1) + ";");
          else if (i >= 1 && j >= 1)
            bw.write(String.valueOf(correlation.correlation(rankingArrays[i-1],rankingArrays[j-1])) + ";");
          if (j.equals(rankingCount))
            bw.newLine();
        }
      }
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Calculates every possible rank for every FD stored in the computation structure
   */
  public void calculateRankings() {
    // Calculate the rank for each functional dependency
    calculateDataIndependentRankings();
    calculateDataDependentRankings();
  }

  /**
   * Calculates the ranks which don't need row access for every FD stored in the computation structure
   */
  public void calculateDataIndependentRankings() {
    for (FDResult fd : fdList) {
      calculateDataIndependentRanks(fd);
    }
  }

  /**
   * Calculates the ranks which need row access for every FD stored in the computation structure
   */
  public void calculateDataDependentRankings() {
    for (FDResult fd : fdList) {
      calculateDataDependentRanks(fd);
    }
  }

  /**
   * Fills the additionalColumns Map with columns we added to the FD as part of our combining process
   * @param nonMaximizedResults List of not maximized results
   */
  public void computeAdditionalColumns(List<FDResult> nonMaximizedResults) {
    // Make sure both lists have the same determinant ordering
    Collections.sort(fdList, new FDResultLexicalDeterminantComparator());
    Collections.sort(nonMaximizedResults, new FDResultLexicalDeterminantComparator());

    Iterator<FDResult> it1 = fdList.iterator();
    Iterator<FDResult> it2 = nonMaximizedResults.iterator();
    while (it1.hasNext() && it2.hasNext()){
      FDResult fd1 = it1.next();
      FDResult fd2 = it2.next();
      // Doubly make sure that the determinants are the same
      if (fd1.getDeterminant().equals(fd2.getDeterminant())) {
        // Earlier it was fd2, fd1
        fd1.setAdditionalColumns(fd1.getDependant().subtract(fd2.getDependant()));
      }
    }
  }

  //</editor-fold>

  //<editor-fold desc="Private rank calculation methods">

  /**
   * Calculates the ranks which don't need row access for a FD
   * @param fdResult FD for which the ranks should be calculated
   */
  private void calculateDataIndependentRanks(FDResult fdResult){
    if(fdResult.getRank() == null){
      fdResult.setRank(new FDRank());
    }

    // Get variables needed for each FDResult
    Integer determinantSize = fdResult.getDeterminant().getColumnCount();
    Integer dependantSize = fdResult.getDependant().getColumnCount();
    Float overallSize = (float) determinantSize + dependantSize;

    // Calculate the size ratios of the determinant and dependant
    calculateSizeRatios(fdResult, determinantSize, dependantSize, overallSize);
    // Calculate the coverage of the FD
    calculateCoverage(fdResult, overallSize);

  }

  /**
   * Calculates the ranks which need row access for an FD
   * @param fdResult FD for which the ranks should be calculated
   */
  private void calculateDataDependentRanks(FDResult fdResult){
    if(fdResult.getRank() == null){
      fdResult.setRank(new FDRank());
    }

    // Calculate the constancy ratios of the determinant and dependant
    Integer determinantSize = fdResult.getDeterminant().getColumnCount();
    Integer dependantSize = fdResult.getDependant().getColumnCount();
    calculateConstancyRatio(fdResult, determinantSize, dependantSize);

    // Calculate the pollution of the FD
    calculatePollution(fdResult);

    // Calculate the information gain in bytes and cells for the FD
    calculateInformationGainCells(fdResult);
    calculateInformationGainBytes(fdResult);
  }

  /**
   * Calculates the determinant and dependant constancy ratios for the given FD
   * @param fd Functional dependency for which the determinant and dependant constancy rank should be calculated
   * @param determinantSize Absolute size of the determinant
   * @param dependantSize Absolute size of the dependant
   */
  private void calculateConstancyRatio(FDResult fd, Integer determinantSize,
                                       Integer dependantSize) {

    // Get column information to check the uniqueness ratio
    List<ColumnInformation> columnInformationList = tableInformation.getColumnInformationList();

    // Prepare constancy column counters
    Integer determinantNearlyConstantColumns = 0;
    Integer dependantNearlyConstantColumns = 0;

    // Define a uniqueness threshold
    Float uniquenessThreshold = 0.1f;

    // Find the constancy column count for the determinant
    for (Integer index : fd.getDeterminant().getColumnIndices()){
      if (columnInformationList.get(index).getUniquenessRate() < uniquenessThreshold)
        determinantNearlyConstantColumns++;
    }

    // Find the constancy column count for the dependant
    for (Integer index : fd.getDependant().getColumnIndices()){
      if (columnInformationList.get(index).getUniquenessRate() < uniquenessThreshold)
        dependantNearlyConstantColumns++;
    }

    // Calculate and store the constancy ratios
    fd.getRank().setDeterminantConstancyRatio(
        (float) determinantNearlyConstantColumns / determinantSize);
    fd.getRank().setDependantConstancyRatio((float) dependantNearlyConstantColumns / dependantSize);
  }

  /**
   * Calculates the coverage of the given FD
   * @param fd Functional dependency for which the coverage should be calculated
   * @param overallSize Sum of the determinant and dependant lengths
   */
  private void calculateCoverage(FDResult fd, Float overallSize) {
    fd.getRank().setCoverage(overallSize / tableInformation.getColumnCount());
  }

  /**
   * Calculates the determinant and dependant size ratios for the given FD
   * @param fd Functional dependency for which the determinant and dependant size rank should be calculated
   * @param determinantSize Absolute size of the determinant
   * @param dependantSize Absolute size of the dependant
   * @param overallSize Sum of the determinant and dependant lengths
   */
  private void calculateSizeRatios(FDResult fd, Integer determinantSize, Integer dependantSize, Float overallSize) {
    fd.getRank().setDeterminantSizeRatio(determinantSize / overallSize);
    fd.getRank().setDependantSizeRatio(dependantSize / overallSize);
  }

  /**
   * Calculates the pollution rank for a given functional dependency
   * The pollution rank is defined as the minimum number of tuples which need to be changed so that a new column can be added to the dependant
   * @param fd Functional dependency for which the pollution rank should be calculated
   */
  private void calculatePollution(FDResult fd){
    // Ignore functional dependencies which are already determine all columns
    if(fd.getDependant().getColumnCount()+fd.getDeterminant().getColumnCount() == tableInformation.getColumnCount()){
      fd.getRank().setPollution(0.0f);
      fd.getRank().setMinPollutionColumn("");
      return;
    }

    // Determine all columns which should be checked as possible dependants
    ColumnCombination columnsToTest = new ColumnCombination(tableInformation.getColumnCount(), true);
    columnsToTest.xor(fd.getDependant());
    columnsToTest.xor(fd.getDeterminant());

    // PLIs are needed for the determinant and each test column
    ColumnCombination pliColumns = fd.getDeterminant().copy();
    pliColumns.or(columnsToTest);

    // Build PLIs
    tableInformation.buildPLIs(pliColumns);
    // Compute PLI for determinant side
    PLIList determinantPLI = tableInformation.getPliHolder().intersectLists(fd.getDeterminant());

    // Check each column for key error
    int wrongTuples = -1;
    for(Integer columnToTest : columnsToTest){
      // Create combination for tested column
      ColumnCombination testCombination = new ColumnCombination(columnToTest, tableInformation.getColumnCount());
      // Intersect the determinant PLI with the PLI of the testing column
      PLIList tester = PLIList.intersect(determinantPLI, tableInformation.getPliHolder().getPliMap()
          .get(testCombination));
      // Compute the number of "wrong" tuples and check for minimality
      int error = Math.abs(tester.computeKeyError()-determinantPLI.computeKeyError());
      wrongTuples = (wrongTuples == -1) ? error : Math.min(wrongTuples, error);
      // Set the name of the minimal polluted column
      if(wrongTuples == error){
        fd.getRank().setMinPollutionColumn(tableInformation.getColumn(columnToTest).getColumnName());
      }
    }

    // Calculate and store the pollution rank
    fd.getRank().setPollution((float) wrongTuples / (float) tableInformation.getRowCount());
  }

  /**
   * Calculates the information gain of the given FD in cells
   * @param fd Functional dependency for which the information gain in cells should be calculated
   */
  private void calculateInformationGainCells(FDResult fd){
    // Calculate the cell count of the original table
    long icTable = tableInformation.getColumnCount() * tableInformation.getRowCount();

    // When "normalized" with the given FD, the original table lose the dependant columns
    long icTableWithoutDependant = (tableInformation.getColumnCount()-fd.getDependant().getColumnCount()) * tableInformation.getRowCount();

    // Calculate the number of rows for the new "determinant-dependant" table
    long uniqueRows = tableInformation.getUniqueValuesCount(fd.getDeterminant());

    // Calculate the number of cells in the new "determinant-dependant" table
    long icDetDep = (fd.getDeterminant().getColumnCount() + fd.getDependant().getColumnCount())
                    * uniqueRows;

    // Store the information gain in the ranking structure
    fd.getRank().setInformationGainCells(icTable - (icTableWithoutDependant + icDetDep));
  }

  /**
   * Calculates the information gain of the given FD in bytes
   * @param fd Functional dependency for which the information gain in bytes should be calculated
   */
  private void calculateInformationGainBytes(FDResult fd){

    // Get the information content of the whole table in bytes
    long icTable = tableInformation.getInformationContent();

    ColumnCombination invertedDependant = fd.getDependant().flippedCopy();

    // Calculate the information content of the "new" original table without the dependant columns
    long icTableWithoutDependant = 0l;
    for(Integer column : invertedDependant){
      icTableWithoutDependant += tableInformation.getColumn(column).getInformationContent(tableInformation.getRowCount());
    }

    long icDetDep = 0l;

    // Calculate the number of rows for the new "determinant-dependant" table
    long uniqueRows = tableInformation.getUniqueValuesCount(fd.getDeterminant());

    // Calculate the information content of the "determinant-dependant" tablle
    ColumnCombination detDep = fd.getDeterminant().copy();
    detDep.or(fd.getDependant());
    for(Integer column : detDep){
      icDetDep += tableInformation.getColumn(column).getInformationContent(uniqueRows);
    }

    // Store the information gain in the ranking structure
    fd.getRank().setInformationGainBytes(icTable - (icTableWithoutDependant + icDetDep));
  }

  //</editor-fold>

  //<editor-fold desc="Partial FD rank method">

  /**
   * Computes partial errors for given results
   *
   * @param pathPLI PLI of the partial determinant for the current path
   * @param index Index of currently regarded column
   * @param begin Inclusive start index of regarded sublist
   * @param end Inclusive end index of regarded sublist
   * @param results List of transformed (not compressed) FD results
   * @param tableInformation Table information
   * @param resultStorage Storage of partial key errors
   */
  private static void recursivePartialErrors(PLIList pathPLI,
                                             int index,
                                             int begin,
                                             int end,
                                             List<FDResult> results,
                                             TableInformation tableInformation,
                                             Map<FunctionalDependency, Integer> resultStorage){

    // Index can be -1 for starting the first computation round, so ignore the name there
    if(index >= 0) {

      // Get PLI for indexed column of the current sublist
      PLIList columnPLI = tableInformation.getPliHolder().getPliMap().get(new ColumnCombination(index, tableInformation.getColumnCount()));
      // Update path PLI
      pathPLI = (pathPLI == null) ? columnPLI : PLIList.intersect(pathPLI, columnPLI);

      // Perform calculations for all list elements
      for(int i = begin; i <= end; i++){
        // Get dependant PLI
        PLIList dependantPLI = tableInformation.getPliHolder().getPliMap().get(results.get(i).getDependant());
        // Intersect the PLIs of dependant and determinant
        PLIList detDepPLI = PLIList.intersect(pathPLI, dependantPLI);
        // Compute the partial FD key error
        int partialError = pathPLI.computeKeyError() - detDepPLI.computeKeyError();

        // Compute the partial determinant
        ColumnCombination partialDeterminant = new ColumnCombination(tableInformation.getColumnCount());
        for(Integer column : results.get(i).getDeterminant()){
          if(column > index)
            break;
          partialDeterminant.set(column);
        }

        // Store the result
        resultStorage.put(new FunctionalDependency(partialDeterminant, results.get(i).getDependant()),
                          partialError);
      }

    }

    // Iterate over all values in the list between begin and end and call the function itself for all ranges with different value after index
    // Get the next used column after index
    int nextSetBit = results.get(begin).getDeterminant().nextSetBit(index + 1);
    int lastBegin = begin;
    // Iterate over list in given range
    for(int i=begin+1; i<=end; i++){
      // Check the next used column, if it differs then the next range is found
      if(results.get(i).getDeterminant().nextSetBit(index+1) != nextSetBit){
        // Don't call the next level of recursion when no further columns exist
        if(nextSetBit != -1) {
          // Add object created from range to children
          recursivePartialErrors(pathPLI, nextSetBit, lastBegin, i - 1, results, tableInformation, resultStorage);
        }
        // Prepare next range computation
        nextSetBit = results.get(i).getDeterminant().nextSetBit(index+1);
        lastBegin = i;
      }
    }

    // Don't forget to add the last range (if it exists)
    if(results.get(lastBegin).getDeterminant().nextSetBit(index+1) != -1) {
      recursivePartialErrors(pathPLI, results.get(lastBegin).getDeterminant().nextSetBit(index + 1),
                             lastBegin, end, results, tableInformation, resultStorage);
    }

  }

  /**
   * Calculates all partial FD key errors for given FD list
   *
   * @param results List of transformed (not compressed) FD results
   * @param tableInformation Table information
   * @return Returns all partial FD key errors for given FD listo
   */
  public static Map<FunctionalDependency, Integer> calculatePartialKeyErrors(List<FDResult> results,
                                                                             TableInformation tableInformation){
    Map<FunctionalDependency, Integer> result = new HashMap<>();

    Collections.sort(results, new FDResultLexicalDeterminantComparator());

    tableInformation.buildPLIs(new ColumnCombination(tableInformation.getColumnCount(), true));

    recursivePartialErrors(null, -1, 0, results.size() - 1, results, tableInformation, result);

    return result;
  }

  //</editor-fold>
}
