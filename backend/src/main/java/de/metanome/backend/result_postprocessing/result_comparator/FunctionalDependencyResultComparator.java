/**
 * Copyright 2015-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.metanome.backend.result_postprocessing.result_comparator;

import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;

/**
 * Defines an functional dependency comparator based on a predefined sort property and sort
 * direction order.
 */
public class FunctionalDependencyResultComparator
  extends ResultComparator<FunctionalDependencyResult> {

  public static final String DEPENDANT_COLUMN = "dependant";
  public static final String DETERMINANT_COLUMN = "determinant";
  public static final String EXTENDED_DEPENDANT_COLUMN = "extended_dependant";
  public static final String DEPENDANT_COLUMN_RATIO = "dependant_column_ratio";
  public static final String DETERMINANT_COLUMN_RATIO = "determinant_column_ratio";
  public static final String GENERAL_COVERAGE = "coverage";
  public static final String DEPENDANT_OCCURRENCE_RATIO = "dependant_occurrence_ratio";
  public static final String DETERMINANT_OCCURRENCE_RATIO = "determinant_occurrence_ratio";
  public static final String DEPENDANT_UNIQUENESS_RATIO = "dependant_uniqueness_ratio";
  public static final String DETERMINANT_UNIQUENESS_RATIO = "determinant_uniqueness_ratio";
  public static final String POLLUTION = "pollution";
  public static final String POLLUTION_COLUMN = "pollution_column";
  public static final String INFORMATION_GAIN_CELL = "information_gain_cell";
  public static final String INFORMATION_GAIN_BYTE = "information_gain_byte";

  /**
   * Creates a functional dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public FunctionalDependencyResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given functional dependency results depending on given sort property
   *
   * @param fd1          functional dependency result
   * @param fd2          other functional dependency result
   * @param sortProperty Sort property
   * @return Returns 1 if fd1 is greater than fd2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(FunctionalDependencyResult fd1, FunctionalDependencyResult fd2,
                        String sortProperty) {
    if (DEPENDANT_COLUMN.equals(sortProperty)) {
      return fd1.getDependant().toString().compareTo(fd2.getDependant().toString());
    }
    if (DETERMINANT_COLUMN.equals(sortProperty)) {
      return fd1.getDeterminant().toString().compareTo(fd2.getDeterminant().toString());
    }
    if (EXTENDED_DEPENDANT_COLUMN.equals(sortProperty)) {
      return fd1.getExtendedDependant().toString().compareTo(fd2.getExtendedDependant().toString());
    }
    if (DEPENDANT_COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(fd1.getDependantColumnRatio(), fd2.getDependantColumnRatio());
    }
    if (DETERMINANT_COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(fd1.getDeterminantColumnRatio(), fd2.getDeterminantColumnRatio());
    }
    if (GENERAL_COVERAGE.equals(sortProperty)) {
      return Float.compare(fd1.getGeneralCoverage(), fd2.getGeneralCoverage());
    }
    if (DEPENDANT_OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(fd1.getDependantOccurrenceRatio(), fd2.getDependantOccurrenceRatio());
    }
    if (DETERMINANT_OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float
        .compare(fd1.getDeterminantOccurrenceRatio(), fd2.getDeterminantOccurrenceRatio());
    }
    if (DEPENDANT_UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(fd1.getDependantUniquenessRatio(), fd2.getDependantUniquenessRatio());
    }
    if (DETERMINANT_UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float
        .compare(fd1.getDeterminantUniquenessRatio(), fd2.getDeterminantUniquenessRatio());
    }
    if (POLLUTION.equals(sortProperty)) {
      return Float.compare(fd1.getPollution(), fd2.getPollution());
    }
    if (POLLUTION_COLUMN.equals(sortProperty)) {
      return fd1.getPollutionColumn().compareTo(fd2.getPollutionColumn());
    }
    if (INFORMATION_GAIN_BYTE.equals(sortProperty)) {
      return Float.compare(fd1.getInformationGainBytes(), fd2.getInformationGainBytes());
    }
    if (INFORMATION_GAIN_CELL.equals(sortProperty)) {
      return Float.compare(fd1.getInformationGainCells(), fd2.getInformationGainCells());
    }

    return 0;
  }


}
