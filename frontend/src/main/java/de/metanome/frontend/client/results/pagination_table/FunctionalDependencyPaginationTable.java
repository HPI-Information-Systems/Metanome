/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.frontend.client.results.pagination_table;

import com.google.gwt.user.cellview.client.TextColumn;

import de.metanome.backend.result_postprocessing.result_comparator.FunctionalDependencyResultComparator;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;
import de.metanome.backend.results_db.ResultType;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination table for functional dependency results.
 */
public class FunctionalDependencyPaginationTable
    extends AbstractPaginationTable<FunctionalDependencyResult> {

  /**
   * Constructs the table for given result type
   *
   * @param resultType the result type
   */
  public FunctionalDependencyPaginationTable(ResultType resultType) {
    super(resultType);
  }

  /**
   * Initializes the table columns
   *
   * @return Returns the list of sort properties for sortable columns
   */
  @Override
  protected List<String> initializeColumns() {
    List<String> columnNames = new ArrayList<>();

    // Determinant column
    TextColumn<FunctionalDependencyResult> determinantColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return functionalDependency.getDeterminant().toString();
      }

    };
    this.table.addColumn(determinantColumn, "Determinant");
    columnNames.add(FunctionalDependencyResultComparator.DETERMINANT_COLUMN);

    // Dependant column
    TextColumn<FunctionalDependencyResult> dependantColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return functionalDependency.getDependant().toString();
      }
    };
    this.table.addColumn(dependantColumn, "Dependant");
    columnNames.add(FunctionalDependencyResultComparator.DEPENDANT_COLUMN);

    // Extended dependant column
    TextColumn<FunctionalDependencyResult> extendedDependantColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return functionalDependency.getExtendedDependant().toString();
      }
    };
    this.table.addColumn(extendedDependantColumn, "Extended Dependant");
    columnNames.add(FunctionalDependencyResultComparator.EXTENDED_DEPENDANT_COLUMN);

    // Determinant column ratio column
    TextColumn<FunctionalDependencyResult> determinantColumnRatioColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return String.valueOf(functionalDependency.getDeterminantColumnRatio());
      }

    };
    this.table.addColumn(determinantColumnRatioColumn, "Determinant Column Ratio");
    columnNames.add(FunctionalDependencyResultComparator.DETERMINANT_COLUMN_RATIO);

    // Dependant column ratio column
    TextColumn<FunctionalDependencyResult> dependantColumnRatioColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return String.valueOf(functionalDependency.getDependantColumnRatio());
      }
    };
    this.table.addColumn(dependantColumnRatioColumn, "Dependant Column Ratio");
    columnNames.add(FunctionalDependencyResultComparator.DEPENDANT_COLUMN_RATIO);

    // Determinant occurrence ratio column
    TextColumn<FunctionalDependencyResult> determinantOccurrenceRatioColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return String.valueOf(functionalDependency.getDeterminantOccurrenceRatio());
      }
    };
    this.table.addColumn(determinantOccurrenceRatioColumn, "Determinant Occurrence Ratio");
    columnNames.add(FunctionalDependencyResultComparator.DETERMINANT_OCCURRENCE_RATIO);

    // Dependant occurrence ratio column
    TextColumn<FunctionalDependencyResult> dependantOccurrenceRatioColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return String.valueOf(functionalDependency.getDependantOccurrenceRatio());
      }
    };
    this.table.addColumn(dependantOccurrenceRatioColumn, "Dependant Occurrence Ratio");
    columnNames.add(FunctionalDependencyResultComparator.DEPENDANT_OCCURRENCE_RATIO);

    // General coverage column
    TextColumn<FunctionalDependencyResult> coverageColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return String.valueOf(functionalDependency.getGeneralCoverage());
      }
    };
    this.table.addColumn(coverageColumn, "General Coverage");
    columnNames.add(FunctionalDependencyResultComparator.GENERAL_COVERAGE);

    // Determinant uniqueness ratio column
    TextColumn<FunctionalDependencyResult> determinantUniquenessRatioColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return String.valueOf(functionalDependency.getDeterminantUniquenessRatio());
      }
    };
    this.table.addColumn(determinantUniquenessRatioColumn, "Determinant Uniqueness Ratio*");
    columnNames.add(FunctionalDependencyResultComparator.DETERMINANT_UNIQUENESS_RATIO);

    // Dependant uniqueness ratio column
    TextColumn<FunctionalDependencyResult> dependantUniquenessRatioColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return String.valueOf(functionalDependency.getDependantUniquenessRatio());
      }
    };
    this.table.addColumn(dependantUniquenessRatioColumn, "Dependant Uniqueness Ratio*");
    columnNames.add(FunctionalDependencyResultComparator.DEPENDANT_UNIQUENESS_RATIO);


    // Pollution column
    TextColumn<FunctionalDependencyResult> pollutionColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return String.valueOf(functionalDependency.getPollution());
      }
    };
    this.table.addColumn(pollutionColumn, "Pollution*");
    columnNames.add(FunctionalDependencyResultComparator.POLLUTION);

    // Pollution column column
    TextColumn<FunctionalDependencyResult> pollutionColumnColumn = new TextColumn<FunctionalDependencyResult>() {
      @Override
      public String getValue(FunctionalDependencyResult functionalDependency) {
        return functionalDependency.getPollutionColumn();
      }
    };
    this.table.addColumn(pollutionColumnColumn, "Pollution Column*");
    columnNames.add(FunctionalDependencyResultComparator.POLLUTION_COLUMN);


    // Set all columns as sortable
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumn(i).setSortable(true);
      table.getColumn(i).setDefaultSortAscending(true);
    }

    return columnNames;
  }

}
