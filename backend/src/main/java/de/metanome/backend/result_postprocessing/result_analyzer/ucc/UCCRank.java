package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import java.io.Serializable;

/**
 * Created by Alexander Spivak on 11.03.2015.
 */
public class UCCRank implements Serializable {

  // Used for serialization
  private static final long serialVersionUID = 6581860388088504926L;

  //<editor-fold desc="Private attributes">

  private int length;
  private double min;
  private double max;
  private double randomness;
  private double averageOccurrence;

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  public double getMax() {
    return max;
  }

  public void setMax(double max) {
    this.max = max;
  }

  public double getRandomness() {
    return randomness;
  }

  public void setRandomness(double randomness) {
    this.randomness = randomness;
  }

  public double getAverageOccurrence() {
    return averageOccurrence;
  }

  public void setAverageOccurrence(double averageOccurrence) {
    this.averageOccurrence = averageOccurrence;
  }

  //</editor-fold>
}
