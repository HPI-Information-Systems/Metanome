package de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies;

import com.google.common.annotations.GwtCompatible;

import java.io.Serializable;

/**
 * Created by Daniel on 02.03.2015.
 */

@GwtCompatible
public class INDRank implements Serializable{

  private static final long serialVersionUID = -1480171719683120608L;

  //number of columns on lhs
  private float dependantSizeRatio;

  //number of columns on rhs
  private float referencedSizeRatio;

  //average number of appearances of lhs columns in other INDs
  private float dependantColumnOccurrence;

  //average number of appearances of rhs columns in other INDs
  private float referencedColumnOccurrence;

  //number of almost unique columns in lhs
  private float dependantConstancyRatio;

  //number of almost unique columns in rhs
  private float referencedConstancyRatio;

  public float getDependantSizeRatio() {
    return dependantSizeRatio;
  }

  public void setDependantSizeRatio(float dependantSizeRatio) {
    this.dependantSizeRatio = dependantSizeRatio;
  }

  public float getReferencedSizeRatio() {
    return referencedSizeRatio;
  }

  public void setReferencedSizeRatio(float referencedSizeRatio) {
    this.referencedSizeRatio = referencedSizeRatio;
  }

  public float getDependantColumnOccurrence() {
    return dependantColumnOccurrence;
  }

  public void setDependantColumnOccurrence(float dependantColumnOccurrence) {
    this.dependantColumnOccurrence = dependantColumnOccurrence;
  }

  public float getReferencedColumnOccurrence() {
    return referencedColumnOccurrence;
  }

  public void setReferencedColumnOccurrence(float referencedColumnOccurrence) {
    this.referencedColumnOccurrence = referencedColumnOccurrence;
  }

  public float getDependantConstancyRatio() {
    return dependantConstancyRatio;
  }

  public void setDependantConstancyRatio(float dependantConstancyRatio) {
    this.dependantConstancyRatio = dependantConstancyRatio;
  }

  public float getReferencedConstancyRatio() {
    return referencedConstancyRatio;
  }

  public void setReferencedConstancyRatio(float referencedConstancyRatio) {
    this.referencedConstancyRatio = referencedConstancyRatio;
  }
}
