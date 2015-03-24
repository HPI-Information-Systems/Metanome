package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;

import com.google.common.annotations.GwtCompatible;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexander Spivak on 17.12.2014.
 *
 * Holder for all ranks belonging to a functional dependency
 */
@GwtCompatible
public class FDRank implements Serializable {

  // Used for serialization
  private static final long serialVersionUID = -7996221907351569191L;

  //<editor-fold desc="Private attributes">

  // Describes the ratio of the determinant length to the total fd length
  private float determinantSizeRatio;
  // Describes the ratio of the dependant length to the total fd length
  private float dependantSizeRatio;

  // Describes the ratio of nearly constant columns in the determinant to the length of the determinant
  private float determinantConstancyRatio;
  // Describes the ratio of nearly constant columns in the dependant to the length of the dependant
  private float dependantConstancyRatio;

  // Describes the minimal ratio of tuples needed to change for increasing the dependant of the FD
  private float pollution;
  // Describes the column which would be added to the FD's dependant if the "polluted" tuples would be changed
  private String minPollutionColumn;

  // Describes the ratio of the total fd length to the table's column count
  private float coverage;

  // Describes the information gain in bytes when using the FD for normalization
  private float informationGainBytes;
  // Describes the information gain in cells when using the FD for normalization
  private float informationGainCells;

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  public float getDeterminantSizeRatio() {
    return determinantSizeRatio;
  }

  public void setDeterminantSizeRatio(float determinantSizeRatio) {
    this.determinantSizeRatio = determinantSizeRatio;
  }

  public float getDependantSizeRatio() {
    return dependantSizeRatio;
  }

  public void setDependantSizeRatio(float dependantSizeRatio) {
    this.dependantSizeRatio = dependantSizeRatio;
  }

  public float getDeterminantConstancyRatio() {
    return determinantConstancyRatio;
  }

  public void setDeterminantConstancyRatio(float determinantConstancyRatio) {
    this.determinantConstancyRatio = determinantConstancyRatio;
  }

  public float getDependantConstancyRatio() {
    return dependantConstancyRatio;
  }

  public void setDependantConstancyRatio(float dependantConstancyRatio) {
    this.dependantConstancyRatio = dependantConstancyRatio;
  }

  public float getPollution() {
    return pollution;
  }

  public void setPollution(float pollution) {
    this.pollution = pollution;
  }

  public String getMinPollutionColumn() {
    return minPollutionColumn;
  }

  public void setMinPollutionColumn(String minPollutionColumn) {
    this.minPollutionColumn = minPollutionColumn;
  }

  public float getCoverage() {
    return coverage;
  }

  public void setCoverage(float coverage) {
    this.coverage = coverage;
  }

  public float getInformationGainBytes() {
    return informationGainBytes;
  }

  public void setInformationGainBytes(float informationGainBytes) {
    this.informationGainBytes = informationGainBytes;
  }

  public float getInformationGainCells() {
    return informationGainCells;
  }

  public void setInformationGainCells(float informationGainCells) {
    this.informationGainCells = informationGainCells;
  }

  //</editor-fold>

  //<editor-fold desc="Mapping method">

  public Map<String, Float> getRankings() {
    Map<String, Float> rankings = new HashMap<>();

    rankings.put("Coverage", this.getCoverage());

    rankings.put("Determinant Size Ratio", this.getDeterminantSizeRatio());
    rankings.put("Dependant Size Ratio", this.getDependantSizeRatio());

    rankings.put("Determinant Constancy Ratio", this.getDeterminantConstancyRatio());
    rankings.put("Dependant Constancy Ratio", this.getDependantConstancyRatio());

    rankings.put("Information Gain Bytes", this.getInformationGainBytes());
    rankings.put("Information Gain Cells", this.getInformationGainCells());

    rankings.put("Pollution", this.getPollution());

    return rankings;
  }

  //</editor-fold>

}
