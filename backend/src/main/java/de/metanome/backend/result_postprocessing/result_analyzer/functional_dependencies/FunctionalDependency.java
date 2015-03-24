package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;

import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;

/**
 * Helper class to hold partial functional FDs
 *
 * Created by Alexander Spivak on 10.03.2015.
 */
public class FunctionalDependency {

  // Determinant
  private ColumnCombination determinant;
  // Dependant
  private ColumnCombination dependant;

  /**
   * Constructor
   * @param determinant Determinant
   * @param dependant Dependant
   */
  public FunctionalDependency(
      ColumnCombination determinant,
      ColumnCombination dependant) {
    this.determinant = determinant;
    this.dependant = dependant;
  }

  public ColumnCombination getDeterminant() {
    return determinant;
  }

  public void setDeterminant(ColumnCombination determinant) {
    this.determinant = determinant;
  }

  public ColumnCombination getDependant() {
    return dependant;
  }

  public void setDependant(ColumnCombination dependant) {
    this.dependant = dependant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FunctionalDependency that = (FunctionalDependency) o;

    if (!dependant.equals(that.dependant)) {
      return false;
    }
    if (!determinant.equals(that.determinant)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = determinant.hashCode();
    result = 31 * result + dependant.hashCode();
    return result;
  }
}
