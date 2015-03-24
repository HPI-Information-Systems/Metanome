package de.metanome.backend.result_postprocessing.visualizing_utils;

import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDRanking;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FunctionalDependency;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationLexicalComparator;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PathUnifier;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.KMeans;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Allows to print result structures as JSON to use them in D3 later
 *
 * Created by Alexander Spivak on 03.12.2014.
 */
@SuppressWarnings("unchecked")
public class JSONPrinter {

  //<editor-fold desc="Helper printing method">

  /**
   * Writes a JSON structure to file
   * @param filePath File path to the output file
   * @param jsonObject JSON structure which should be printed
   */
  private static void writeToFile(String filePath, JSONAware jsonObject){
    try {
      File folder = new File(filePath);
      folder.mkdirs();
      File file = new File(PathUnifier.combinePaths(filePath, "PrefixTree.json"));
      if (file.exists()){
        file.delete();
      }
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(jsonObject.toJSONString());
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //</editor-fold>

  //<editor-fold desc="UCC printing methods">

  /**
   * Prints information about clusters of UCCs to file
   *
   * @param filePath    File path to the output file
   * @param clusterList List with Information about each cluster
   */
  public static void printUCCClusters(String filePath, List<HashMap<String, Double>> clusterList) {
    JSONArray clusters = new JSONArray();

    for (HashMap<String, Double> info : clusterList) {
      JSONObject entryJSON = new JSONObject();
      for (Map.Entry<String, Double> infoEntry : info.entrySet()) {
        entryJSON.put(infoEntry.getKey(), infoEntry.getValue());
      }
      clusters.add(entryJSON);
    }

    writeToFile(filePath, clusters);
  }

  /**
   * Prints the histograms of all UCCs
   *
   * @param filePath      File path to the output file
   * @param histogramList List containing all Histograms
   */
  public static void printUCCHistograms(String filePath, List<KMeans.UCCHistogram> histogramList) {
    JSONArray histograms = new JSONArray();

    for (KMeans.UCCHistogram histogram : histogramList) {
      HashMap<String, Double> info = histogram.getValues();
      JSONObject entryJSON = new JSONObject();
      for (Map.Entry<String, Double> infoEntry : info.entrySet()) {
        entryJSON.put(infoEntry.getKey(), infoEntry.getValue());
      }
      histograms.add(entryJSON);
    }

    writeToFile(filePath, histograms);
  }


  /**
   * Prints one UCC for presentation as a diagram showing the uniqueness of each column
   *
   * @param filePath File path to the output file
   * @param uccInfo  List containing all Histograms
   */
  public static void printUCCInfo(String filePath, HashMap<String, Double> uccInfo) {
    JSONArray info = new JSONArray();

    for (Map.Entry<String, Double> column : uccInfo.entrySet()) {
      JSONObject columnInfo = new JSONObject();
      columnInfo.put("Column Name", column.getKey());
      columnInfo.put("Uniqueness", column.getValue());
      info.add(columnInfo);
    }

    writeToFile(filePath, info);
  }

  //</editor-fold>

  //<editor-fold desc="FD prefix tree printing methods">

  /**
   * Recursive helper function for printing prefix trees as JSON
   * @param index Index of the column inside the column combination the current computation should run on
   * @param begin Inclusive index of the left list border which should be regarded in the computation
   * @param end Inclusive index of the right list border which should be regarded in the computation
   * @param combinationList List of all determinants pointing to a column
   * @param tableInformation Table information structure for resolving column names
   * @param partialErrors Partial errors map providing the information about partial FD key errors
   * @return Returns the prefix tree branch as JSON defined by the parameters
   */
  private static JSONObject printRecursive(int index,
                                           int begin,
                                           int end,
                                           ColumnCombination dependant,
                                           ColumnCombination currentPath,
                                           List<ColumnCombination> combinationList,
                                           TableInformation tableInformation,
                                           Map<FunctionalDependency, Integer> partialErrors){
    // Prepare the result structure
    JSONObject result = new JSONObject();
    ColumnCombination newPath = currentPath.copy();

    // Index can be -1 for starting the first computation round, so ignore the name there
    if(index >= 0) {
      result.put("name", tableInformation.getColumn(index).getColumnName());
      // Size property is relevant for sunburst and circle packing diagrams
      result.put("size", tableInformation.getColumn(index).getUniquenessRate());

      newPath.set(index);
      result.put("keyError", partialErrors.get(new FunctionalDependency(newPath, dependant)));
    }

    // Prepare the children array of the result
    JSONArray children = new JSONArray();

    // Iterate over all values in the list between begin and end and call the function itself for all ranges with different value after index
    // Get the next used column after index
    int nextSetBit = combinationList.get(begin).nextSetBit(index+1);
    int lastBegin = begin;
    // Iterate over list in given range
    for(int i=begin+1; i<=end; i++){
      // Check the next used column, if it differs then the next range is found
      if(combinationList.get(i).nextSetBit(index+1) != nextSetBit){
        // Don't call the next level of recursion when no further columns exist
        if(nextSetBit != -1) {
          // Add object created from range to children
          children.add(
              printRecursive(nextSetBit, lastBegin, i - 1, dependant, newPath, combinationList, tableInformation, partialErrors));
        }
        // Prepare next range computation
        nextSetBit = combinationList.get(i).nextSetBit(index+1);
        lastBegin = i;
      }
    }

    // Don't forget to add the last range (if it exists)
    if(combinationList.get(lastBegin).nextSetBit(index+1) != -1) {
      children.add(printRecursive(combinationList.get(lastBegin).nextSetBit(index+1), lastBegin, end, dependant,
                                  newPath, combinationList, tableInformation, partialErrors));
    }

    // Only add the children if they exist
    if(!children.isEmpty()) {
      result.put("children", children);
    }

    return result;
  }

  /**
   * Print the functional dependencies from a dependency index as a prefix tree JSON.
   * Usable in multiple D3 diagrams.
   * @param filePath File path to the output file
   * @param transformedResults List of converted, but not compressed FD results
   * @param dependantsIndex Dependants index (for each column the list of determinants, which causes the column)
   * @param tableInformation Table information structure for resolving column names
   */
  public static void printFDsAsPrefixTreeJSON(String filePath,
                                              List<FDResult> transformedResults,
                                              Map<ColumnCombination, List<ColumnCombination>> dependantsIndex,
                                              TableInformation tableInformation){

    // Table name should be the root
    JSONObject root = new JSONObject();
    root.put("name", tableInformation.getTableName());
    //store table size in root to show it in keyError
    root.put("tableSize", tableInformation.getRowCount());

    // Build tree for each dependant column
    JSONArray rootChildren = new JSONArray();

    // Sort the dependant columns based on their index
    List<ColumnCombination> dependantColumns = new ArrayList<>(dependantsIndex.keySet());
    dependantColumns.sort(new ColumnCombinationLexicalComparator());

    Map<FunctionalDependency, Integer> partialKeyErrors = FDRanking.calculatePartialKeyErrors(transformedResults, tableInformation);

    // Iterate over the sorted keys
    for(ColumnCombination dependantColumn : dependantColumns){
      // Get the right determinants list for the dependant column
      List<ColumnCombination> determinants = dependantsIndex.get(dependantColumn);
      // Sort the determinants in lexical order
      Collections.sort(determinants, new ColumnCombinationLexicalComparator());

      // Create a JSON for the dependant column
      JSONObject dependantJSON = printRecursive(-1,
                                                0,
                                                determinants.size() - 1,
                                                dependantColumn.copy(),
                                                new ColumnCombination(tableInformation.getColumnCount()),
                                                determinants,
                                                tableInformation,
                                                partialKeyErrors);
      // Change the root name to the dependant column name
      dependantJSON.put("name", tableInformation.getColumn(dependantColumn.nextSetBit(0))
          .getColumnName());
      // Add it to the table's children array
      rootChildren.add(dependantJSON);
    }
    root.put("children", rootChildren);

    writeToFile(filePath, root);
  }

  //</editor-fold>

}
