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

import de.metanome.backend.result_postprocessing.result_comparator.InclusionDependencyResultComparator;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;
import de.metanome.backend.results_db.ResultType;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination table for inclusion dependency results.
 */
public class InclusionDependencyPaginationTable
    extends AbstractPaginationTable<InclusionDependencyResult> {

  /**
   * Constructs the table for given result type
   *
   * @param resultType the result type
   */
  public InclusionDependencyPaginationTable(ResultType resultType) {
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
    TextColumn<InclusionDependencyResult> dependantColumn = new TextColumn<InclusionDependencyResult>() {
      @Override
      public String getValue(InclusionDependencyResult inclusionDependency) {
        return inclusionDependency.getDependant().toString();
      }
    };
    this.table.addColumn(dependantColumn, "Dependant");
    columnNames.add(InclusionDependencyResultComparator.DEPENDANT_COLUMN);

    // Referenced column
    TextColumn<InclusionDependencyResult> referencedColumn = new TextColumn<InclusionDependencyResult>() {
      @Override
      public String getValue(InclusionDependencyResult inclusionDependency) {
        return inclusionDependency.getReferenced().toString();
      }

    };
    this.table.addColumn(referencedColumn, "Referenced");
    columnNames.add(InclusionDependencyResultComparator.REFERENCED_COLUMN);

    // Dependant column ratio column
    TextColumn<InclusionDependencyResult> dependantColumnRatioColumn = new TextColumn<InclusionDependencyResult>() {
      @Override
      public String getValue(InclusionDependencyResult inclusionDependency) {
        return String.valueOf(inclusionDependency.getDependantColumnRatio());
      }
    };
    this.table.addColumn(dependantColumnRatioColumn, "Dependant Column Ratio");
    columnNames.add(InclusionDependencyResultComparator.DEPENDANT_COLUMN_RATIO);

    // Referenced column ratio column
    TextColumn<InclusionDependencyResult> referencedColumnRatioColumn = new TextColumn<InclusionDependencyResult>() {
      @Override
      public String getValue(InclusionDependencyResult inclusionDependency) {
        return String.valueOf(inclusionDependency.getReferencedColumnRatio());
      }
    };
    this.table.addColumn(referencedColumnRatioColumn, "Referenced Column Ratio");
    columnNames.add(InclusionDependencyResultComparator.REFERENCED_COLUMN_RATIO);

    // Dependant occurrence ratio column
    TextColumn<InclusionDependencyResult> dependantOccurrenceRatioColumn = new TextColumn<InclusionDependencyResult>() {
      @Override
      public String getValue(InclusionDependencyResult inclusionDependency) {
        return String.valueOf(inclusionDependency.getDependantOccurrenceRatio());
      }
    };
    this.table.addColumn(dependantOccurrenceRatioColumn, "Dependant Occurrence Ratio");
    columnNames.add(InclusionDependencyResultComparator.DEPENDANT_OCCURRENCE_RATIO);

    // Reference occurrence ratio column
    TextColumn<InclusionDependencyResult> referencedOccurrenceRatioColumn = new TextColumn<InclusionDependencyResult>() {
      @Override
      public String getValue(InclusionDependencyResult inclusionDependency) {
        return String.valueOf(inclusionDependency.getReferencedOccurrenceRatio());
      }
    };
    this.table.addColumn(referencedOccurrenceRatioColumn, "Referenced Occurrence Ratio");
    columnNames.add(InclusionDependencyResultComparator.REFERENCED_OCCURRENCE_RATIO);

    // Dependant uniqueness ratio
    TextColumn<InclusionDependencyResult> dependantUniquenessRatioColumn = new TextColumn<InclusionDependencyResult>() {
      @Override
      public String getValue(InclusionDependencyResult inclusionDependency) {
        return String.valueOf(inclusionDependency.getDependantUniquenessRatio());
      }
    };
    this.table.addColumn(dependantUniquenessRatioColumn, "Dependant Uniqueness Ratio*");
    columnNames.add(InclusionDependencyResultComparator.DEPENDANT_UNIQUENESS_RATIO);

    // Reference occurrence ratio column
    TextColumn<InclusionDependencyResult> referencedUniquenessRatioColumn = new TextColumn<InclusionDependencyResult>() {
      @Override
      public String getValue(InclusionDependencyResult inclusionDependency) {
        return String.valueOf(inclusionDependency.getReferencedUniquenessRatio());
      }
    };
    this.table.addColumn(referencedUniquenessRatioColumn, "Referenced Uniqueness Ratio*");
    columnNames.add(InclusionDependencyResultComparator.REFERENCED_UNIQUENESS_RATIO);

    // Set all columns as sortable
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumn(i).setSortable(true);
      table.getColumn(i).setDefaultSortAscending(true);
    }

    return columnNames;
  }

}
