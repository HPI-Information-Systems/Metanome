package de.metanome.backend.result_postprocessing.visualizing_utils.ftl_template_usage;

import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDRank;

/**
 * Data holder for FD ranks and additional information during FTL template printing
 *
 * Created by Alexander Spivak on 18.02.2015.
 */
public class FDRankTemplateModel {

  //<editor-fold desc="Private attributes">

  // Ranks of the FD
  private FDRank fdRank;
  // Tooltip for the ranking
  private String rankingTooltip;
  // String representation of the determinant
  private String determinant;
  // Tooltip for the determinant
  private String determinantTooltip;
  // String representation of the dependant
  private String dependant;
  // Tooltip for the dependant
  private String dependantTooltip;
  // String representation of columns which are added with pseudo-transitivity
  private String additionalColumns;

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  public FDRank getFdRank() {
    return fdRank;
  }

  public void setFdRank(FDRank fdRank) {
    this.fdRank = fdRank;
  }

  public String getRankingTooltip() {
    return rankingTooltip;
  }

  public void setRankingTooltip(String rankingTooltip) {
    this.rankingTooltip = rankingTooltip;
  }

  public String getDeterminant() {
    return determinant;
  }

  public void setDeterminant(String determinant) {
    this.determinant = determinant;
  }

  public String getDeterminantTooltip() {
    return determinantTooltip;
  }

  public void setDeterminantTooltip(String determinantTooltip) {
    this.determinantTooltip = determinantTooltip;
  }

  public String getDependant() {
    return dependant;
  }

  public void setDependant(String dependant) {
    this.dependant = dependant;
  }

  public String getDependantTooltip() {
    return dependantTooltip;
  }

  public void setDependantTooltip(String dependantTooltip) {
    this.dependantTooltip = dependantTooltip;
  }

  public String getAdditionalColumns() {
    return additionalColumns;
  }

  public void setAdditionalColumns(String additionalColumns) {
    this.additionalColumns = additionalColumns;
  }

  //</editor-fold>

}
