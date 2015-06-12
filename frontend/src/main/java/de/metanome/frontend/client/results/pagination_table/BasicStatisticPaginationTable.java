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

import de.metanome.backend.result_postprocessing.result_comparator.BasicStatisticResultComparator;
import de.metanome.backend.result_postprocessing.results.BasicStatisticResult;
import de.metanome.backend.results_db.ResultType;

import java.util.ArrayList;
import java.util.List;

/**
 * Pagination table for basic statistic results.
 */
public class BasicStatisticPaginationTable
    extends AbstractPaginationTable<BasicStatisticResult> {

  /**
   * Constructs the table for given result type
   *
   * @param resultType result type
   */
  public BasicStatisticPaginationTable(ResultType resultType) {
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

    // name column
    TextColumn<BasicStatisticResult> nameColumn = new TextColumn<BasicStatisticResult>() {
      @Override
      public String getValue(BasicStatisticResult basicStatistic) {
        return basicStatistic.getStatisticName();
      }
    };
    this.table.addColumn(nameColumn, "Statistic Name");
    columnNames.add(BasicStatisticResultComparator.NAME_COLUMN);

    // unique column combination column
    TextColumn<BasicStatisticResult> columnCombinationColumn = new TextColumn<BasicStatisticResult>() {
      @Override
      public String getValue(BasicStatisticResult basicStatistic) {
        return basicStatistic.getColumnCombination().toString();
      }
    };
    this.table.addColumn(columnCombinationColumn, "Column Combination");
    columnNames.add(BasicStatisticResultComparator.COLUMN_COMBINATION_COLUMN);

    // value column
    TextColumn<BasicStatisticResult> valueColumn = new TextColumn<BasicStatisticResult>() {
      @Override
      public String getValue(BasicStatisticResult basicStatistic) {
        return basicStatistic.getStatisticValue().toString();
      }
    };
    this.table.addColumn(valueColumn, "Value");
    columnNames.add(BasicStatisticResultComparator.VALUE_COLUMN);

    // column ratio
    TextColumn<BasicStatisticResult> columnRatioColumn = new TextColumn<BasicStatisticResult>() {
      @Override
      public String getValue(BasicStatisticResult basicStatistic) {
        return String.valueOf(basicStatistic.getColumnRatio());
      }
    };
    this.table.addColumn(columnRatioColumn, "Column Ratio");
    columnNames.add(BasicStatisticResultComparator.COLUMN_RATIO);

    // occurrence ratio
    TextColumn<BasicStatisticResult> occurrenceRatioColumn = new TextColumn<BasicStatisticResult>() {
      @Override
      public String getValue(BasicStatisticResult basicStatistic) {
        return String.valueOf(basicStatistic.getOccurrenceRatio());
      }
    };
    this.table.addColumn(occurrenceRatioColumn, "Occurrence Ratio");
    columnNames.add(BasicStatisticResultComparator.OCCURRENCE_RATIO);

    // uniqueness ratio
    TextColumn<BasicStatisticResult> uniquenessRatioColumn = new TextColumn<BasicStatisticResult>() {
      @Override
      public String getValue(BasicStatisticResult basicStatistic) {
        return String.valueOf(basicStatistic.getUniquenessRatio());
      }
    };
    this.table.addColumn(uniquenessRatioColumn, "Uniqueness Ratio");
    columnNames.add(BasicStatisticResultComparator.UNIQUENESS_RATIO);

    // Set all columns as sortable
    for (int i = 0; i < table.getColumnCount(); i++) {
      table.getColumn(i).setSortable(true);
      table.getColumn(i).setDefaultSortAscending(true);
    }

    return columnNames;
  }

}
