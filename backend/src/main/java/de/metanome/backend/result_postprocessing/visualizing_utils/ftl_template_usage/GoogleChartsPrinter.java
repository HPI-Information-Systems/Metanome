package de.metanome.backend.result_postprocessing.visualizing_utils.ftl_template_usage;

import de.metanome.backend.result_postprocessing.io_helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDRanking;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResult;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PathUnifier;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCRankData;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCRanking;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Provides printing functionality for creating Google Chart Tables
 *
 * Created by Alexander Spivak on 26.11.2014.
 */
@SuppressWarnings("unchecked")
public class GoogleChartsPrinter {

  //<editor-fold desc="Private static attributes">

  // Configuration of Freemarker FTL printer
  private static Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
  // FTL template directory
  private static String templateDir = PathUnifier.combinePaths("ftl-templates", "fd-templates");//Config.ftlTemplateDir;

  //</editor-fold>

  //<editor-fold desc="Static attributes initialization">

  /**
   * Configuration initialization
   */
  static {
    // Configure the FTL template directory
    try {
      File templateDirFile = new File(templateDir);
      // Otherwise the backend tests fail
      if(!templateDirFile.exists()){
        templateDirFile.mkdirs();
      }
      configuration.setDirectoryForTemplateLoading(templateDirFile);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // For appropriate floating points format
    configuration.setLocale(Locale.US);
    configuration.setNumberFormat("0.####");
  }

  //</editor-fold>

  //<editor-fold desc="Private helper methods">

  /**
   * Prints the template with given data to given path
   * @param template FTL template which should be filled with given data
   * @param data Data to be inserted in the template
   * @param filePath Path of the file where the result should be stored
   */
  private static void printToFile(Template template, Map<String, Object> data, String filePath) {
    try {
      // File output
      Writer file = new FileWriter(new File(filePath));
      // Process the data and store to file
      template.process(data, file);
      file.flush();
      file.close();
    } catch (IOException | TemplateException e) {
      e.printStackTrace();
    }
  }

  //</editor-fold>

  //<editor-fold desc="Public helper methods">

  /**
   * Creates a directory for given path if no directory exists
   * @param dirPath Path to the directory which should be created
   */
  public static void createDirectory(String dirPath){
    File ioDir = new File(dirPath);
    if(!ioDir.exists()){
      ioDir.mkdirs();
    }
  }

  //</editor-fold>

  //<editor-fold desc="Table information printing methods">

  /**
   * Prints the table information (row count, column count) and the column information
   * @param tableInformation Table information
   * @param filePath Path to the file which should be created
   */
  public static void tableInformationAsTable(TableInformation tableInformation, String filePath) {

    try {
      //Load template from source folder
      Template template = GoogleChartsPrinter.configuration.getTemplate("column-info-template.ftl");

      // Prepare data
      Map<String, Object> templateData = new HashMap<>();
      templateData.put("tableName", tableInformation.getTableName());
      templateData.put("columnCount", tableInformation.getColumnCount());
      templateData.put("rowCount", tableInformation.getRowCount());
      templateData.put("columnInformationList", tableInformation.getColumnInformationList());

      // Print to file
      GoogleChartsPrinter.printToFile(template, templateData, filePath);
    }
    catch (IOException e){
      e.printStackTrace();
    }
  }

  /**
   * Prints the column value distribution as histogram to the given file
   * @param columnInformation Column information containing the column name and the histogram
   * @param tableName Name of the table containing the column
   * @param filePath Path to the file which should be created
   */
  public static void columnInformationAsHistogram(ColumnInformation columnInformation, String tableName, String filePath) {

    try {
      //Load template from source folder
      Template template = GoogleChartsPrinter.configuration.getTemplate("column-histogram-template.ftl");

      // Prepare data
      Map<String, Object> templateData = new HashMap<>();
      templateData.put("columnInformation", columnInformation);
      templateData.put("tableName", tableName);

      // Print to file
      GoogleChartsPrinter.printToFile(template, templateData, filePath);
    }
    catch (IOException e){
      e.printStackTrace();
    }
  }

  /**
   * Creates a linked web pages collection containing all information about the table and the columns in the given directory
   * @param tableInformation Table information for which the web pages should be created
   * @param outputDirectory Path to the directory where the web pages should be generated in
   */
  public static void tableInformationAsWebPage(TableInformation tableInformation, String outputDirectory){
    // Create a directory for general output
    GoogleChartsPrinter.createDirectory(outputDirectory);
    // Create a directory for table
    String tableDirectory = PathUnifier
        .combinePaths(outputDirectory, tableInformation.getTableName() + "-table");
    GoogleChartsPrinter.createDirectory(tableDirectory);
    // Create the table page
    GoogleChartsPrinter.tableInformationAsTable(tableInformation,
                                                PathUnifier.combinePaths(tableDirectory,
                                                                         tableInformation
                                                                             .getTableName()
                                                                         + ".html"));
    // Create a directory for columns
    String columnsDirectory = PathUnifier.combinePaths(tableDirectory, "columns");
    GoogleChartsPrinter.createDirectory(columnsDirectory);
    // Create histograms for columns
    for(ColumnInformation columnInformation : tableInformation.getColumnInformationList()){
      try {
        GoogleChartsPrinter.columnInformationAsHistogram(columnInformation,tableInformation.getTableName(),
                                                         PathUnifier.combinePaths(columnsDirectory,
                                                                                  columnInformation
                                                                                      .getSlugifiedColumnName()
                                                                                  + ".html"));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  //</editor-fold>

  //<editor-fold desc="FD Ranking printing methods">

  /**
   * Creates a web page in the given output directory with the provided FD rankings
   * @param fdRanking Storage of FD rankings
   * @param tableInformation Table information
   * @param outputDirectory Path to the output directory where the web page should be created
   */
  public static void fdRankingsAsTable(FDRanking fdRanking, TableInformation tableInformation, String outputDirectory){
    // Create a directory for general output
    GoogleChartsPrinter.createDirectory(outputDirectory);
    // Create a directory for table
    String tableDirectory = PathUnifier.combinePaths(outputDirectory,
                                                     tableInformation.getTableName() + "-table");
    GoogleChartsPrinter.createDirectory(tableDirectory);

    try {
      //Load template from source folder
      Template template = GoogleChartsPrinter.configuration.getTemplate("fd-ranking-template.ftl");

      // Prepare data
      Map<String, Object> templateData = new HashMap<>();

      Map<String, FDRankTemplateModel> rankingModels = new HashMap<>(fdRanking.getFdList().size());

      for(FDResult fdResult : fdRanking.getFdList()){
        // Create new model
        FDRankTemplateModel rankModel = new FDRankTemplateModel();
        // Set the additional columns
        rankModel.setAdditionalColumns(fdResult.getAdditionalColumnsAsString());
        // Set the rank and the appropriate Tooltip
        rankModel.setFdRank(fdResult.getRank());
        rankModel.setRankingTooltip(fdResult.toString(tableInformation));
        // Set the determinant properties
        rankModel.setDeterminant(fdResult.getDeterminantAsString());
        rankModel.setDeterminantTooltip(fdResult.getDeterminantWithColumnNames());
        // Set the dependant properties
        rankModel.setDependant(fdResult.getDependantAsString());
        rankModel.setDependantTooltip(fdResult.getDependantWithColumnNames());
        // Add to map
        rankingModels.put(fdResult.toString(), rankModel);
      }

      templateData.put("ranking", rankingModels);

      // Print to file
      GoogleChartsPrinter.printToFile(template, templateData,
                                      PathUnifier.combinePaths(tableDirectory, tableInformation
                                                                                   .getSlugifiedTableName()
                                                                               + "-FD-ranking.html"));
    }
    catch (IOException e){
      e.printStackTrace();
    }
  }

  //</editor-fold>

  //<editor-fold desc="UCC Ranking printing methods">

  private static void integrate(Map<Integer, Map<Integer, Long>> occurences, ColumnCombination ucc) {
    for(Integer columnIndex : ucc.getColumnIndices()) {
      if(!occurences.containsKey(columnIndex)) {
        occurences.put(columnIndex, new HashMap<Integer, Long>());
      }
      if(!occurences.get(columnIndex).containsKey(ucc.getColumnCount())) {
        occurences.get(columnIndex).put(ucc.getColumnCount(), 0l);
      }
      occurences.get(columnIndex).put(ucc.getColumnCount(), (occurences.get(columnIndex).get(ucc.getColumnCount()) + 1));
    }
  }

  private static void generatePieChartJSON(String baseDir, TableInformation tableInformation, Map<Integer, Map<Integer, Long>> occurences) {
    for(int columnIndex = 0; columnIndex < tableInformation.getColumnCount(); columnIndex++) {
      String filePath = PathUnifier.combinePaths(baseDir, "pieData");
      filePath = PathUnifier.combinePaths(filePath,
                                          tableInformation.getColumn(columnIndex).getColumnName()
                                          + ".json");
      File jsonFile = new File(filePath);
      try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFile));
        JSONArray array = new JSONArray();
        if(occurences.containsKey(columnIndex)) {
          for(Integer length : occurences.get(columnIndex).keySet()) {
            JSONObject part = new JSONObject();
            part.put("label", String.valueOf(length));
            part.put("value", occurences.get(columnIndex).get(length));
            part.put("color", "#efefef");
            array.add(part);
          }
        } else {
          JSONObject object = new JSONObject();
          object.put("label", "NONE");
          object.put("value", 0);
          object.put("color", "#efefef");
          array.add(object);
        }

        writer.write(array.toJSONString());
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static void uccRankingAsTableAdvanced(Map<String, Object> data, String dir, String tableName) {
    try {
      Template template = GoogleChartsPrinter.configuration.getTemplate("ucc-ranking-advanced-template.ftl");
      GoogleChartsPrinter.printToFile(template, data, PathUnifier.combinePaths(dir, tableName
                                                                                    + "-UCC-Ranking-Advanced.html"));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static double[] calculateMinMax(ColumnCombination ucc, List<ColumnInformation> columnInformationList) {
    List<Integer> indices = ucc.getColumnIndices();
    double min = columnInformationList.get(0).getDistinctValuesCount();
    double max = columnInformationList.get(0).getDistinctValuesCount();
    for(Integer i : indices) {
      min = Math.min(min, (double) columnInformationList.get(i).getDistinctValuesCount());
      max = Math.max(max, (double) columnInformationList.get(i).getDistinctValuesCount());
    }
    double rows = (double) columnInformationList.get(0).getRowCount();
    return new double[] { min / rows, max / rows };
  }

  /**
   * Creates a web page in the given output directory with the provided UCC rankings
   * @param ranking Storage of UCCs rankings
   * @param tableInformation Table information
   * @param outputDirectory Path to the output directory where the web page should be created
   */
  public static void uccRankingAsTable(List<UCCRanking.RankingResult> ranking, TableInformation tableInformation, String outputDirectory) {
    // Create the output directory
    GoogleChartsPrinter.createDirectory(outputDirectory);
    // Create the table directory
    String tableDirectory = PathUnifier.combinePaths(outputDirectory,
                                                     tableInformation.getTableName() + "-table");
    GoogleChartsPrinter.createDirectory(tableDirectory);

    //Load template from source folder
    try {
      Template template = GoogleChartsPrinter.configuration.getTemplate("ucc-ranking-template.ftl");
      Map<String, Object> templateData = new HashMap<>();
      List<UCCRankData> data = new LinkedList<>();
      List<ColumnCombination> uccs = new LinkedList<>();
      for(UCCRanking.RankingResult rankEntry : ranking) {
        uccs.add(rankEntry.getUcc());
      }
      Map<Integer, Map<Integer, Long>> occurences = new HashMap<>();
      for(UCCRanking.RankingResult rankEntry : ranking){
        integrate(occurences, rankEntry.getUcc());
        double[] minMax = calculateMinMax(rankEntry.getUcc(), tableInformation.getColumnInformationList());
        data.add(new UCCRankData(rankEntry.getRank(), rankEntry.getUcc(), rankEntry.getNormalizedValue(), minMax[0], minMax[1], uccs, tableInformation.getColumnInformationList()));
      }
      generatePieChartJSON(tableDirectory, tableInformation, occurences);
      templateData.put("uccRanking", data);
      templateData.put("tableName", tableInformation.getTableName());

      GoogleChartsPrinter.printToFile(template, templateData, PathUnifier.combinePaths(
          tableDirectory, tableInformation.getTableName() + "-UCC-Ranking.html"));
      uccRankingAsTableAdvanced(templateData, tableDirectory, tableInformation.getTableName());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //</editor-fold>

}
