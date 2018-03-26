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
package de.metanome.backend.result_postprocessing.helper;

import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Provides metadata and statistics about a table column
 */
public class ColumnInformation {

  // Stores the byte sizes for a cell of given column type
  private static final Map<ColumnType, Integer> contentSizes;

  /**
   * Static initializer to fill the final content sizes map
   */
  static {
    Map<ColumnInformation.ColumnType, Integer> sizes = new HashMap<>();

    sizes.put(ColumnInformation.ColumnType.BOOLEAN_COLUMN, 1);
    sizes.put(ColumnInformation.ColumnType.DATE_COLUMN, 4);
    sizes.put(ColumnInformation.ColumnType.FLOAT_COLUMN, 4);
    sizes.put(ColumnInformation.ColumnType.INTEGER_COLUMN, 4);
    sizes.put(ColumnInformation.ColumnType.STRING_COLUMN, 8);

    contentSizes = Collections.unmodifiableMap(sizes);
  }

  // Name of the column
  private String columnName = null;
  // Index of the column inside the table (starting with 0)
  private int columnIndex;
  // Unique bit set representing this column
  private BitSet bitSet;
  // Type of the column (heuristically determined)
  private ColumnType columnType = ColumnType.STRING_COLUMN;
  // Count of distinct values
  private long distinctValuesCount = 0L;
  // Count of null values
  private long nullValuesCount = 0L;
  // Count of rows
  private long rowCount = 0L;
  // Value histogram
  private Histogram histogram = null;
  // Average length of cell values in string columns
  private float averageValueLength = 0.0f;

  /**
   * Creates a new column information for given column name and index
   *
   * @param columnName  name of the column
   * @param columnIndex index of the column
   * @param bitSet      bit set representing this column
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the input is not iterable
   */
  public ColumnInformation(String columnName, int columnIndex, BitSet bitSet)
    throws InputIterationException {
    this(columnName, columnIndex, bitSet, null, false);
  }

  /**
   * Creates a new column information for given column name and index
   *
   * @param columnName                 name of the column
   * @param columnIndex                index of the column
   * @param bitSet                     bit set representing this column
   * @param relationalInput            relational input used to provide the column information
   * @param useDataDependentStatistics true, if data dependent statistics should be calculated,
   *                                   false otherwise
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the input is not iterable
   */
  public ColumnInformation(String columnName, int columnIndex, BitSet bitSet,
                           RelationalInput relationalInput, boolean useDataDependentStatistics)
    throws InputIterationException {
    this.columnName = columnName;
    this.columnIndex = columnIndex;
    this.bitSet = bitSet;
    if (useDataDependentStatistics) {
      this.computeDataDependentStatistics(relationalInput);
    }
  }

  /**
   * Computes all column metadata, which need access to the actual data
   *
   * @param relationalInput relational input
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the input is not iterable
   */
  protected void computeDataDependentStatistics(RelationalInput relationalInput)
    throws InputIterationException {
    // Create histogram and determine column type
    this.createHistogramAndDetermineType(relationalInput);
    // Compute number of distinct values
    this.computeDistinctValuesCount();
    // Compute number of null values
    this.computeNullValuesCount();
    // Compute the average length of a string column values
    this.computeAverageLength();
  }

  /**
   * Creates a histogram for the column values
   *
   * @param relationalInput relational input
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the input is not iterable
   */
  protected void createHistogramAndDetermineType(RelationalInput relationalInput)
    throws InputIterationException {
    this.histogram = new Histogram();
    this.rowCount = 0L;

    Map<ColumnType, Integer> columnTypeMap = new EnumMap<>(ColumnType.class);

    // iterate over data and add each cell value to the histogram
    while (relationalInput.hasNext()) {
      List<String> row = relationalInput.next();
      String cellValue = row.get(this.columnIndex);

      // determine type of cell and store it in a map
      if (cellValue != null) {
        ColumnType type = getCellType(cellValue);
        if (columnTypeMap.containsKey(type)) {
          columnTypeMap.put(type, columnTypeMap.get(type) + 1);
        } else {
          columnTypeMap.put(type, 1);
        }
      }
      // add value to histogram
      this.histogram.addValue(cellValue);

      this.rowCount++;
    }
    // determine column type
    this.columnType = determineColumnType(columnTypeMap);
  }

  /**
   * Determine the type of the given cell value.
   *
   * @param cellValue the cell value
   * @return the type of the cell value (boolean, integer, float, date or string)
   */
  protected ColumnType getCellType(String cellValue) {
    if (isBooleanValue(cellValue)) {
      return ColumnType.BOOLEAN_COLUMN;
    } else if (isIntegerValue(cellValue)) {
      return ColumnType.INTEGER_COLUMN;
    } else if (isFloatValue(cellValue)) {
      return ColumnType.FLOAT_COLUMN;
    } else if (isDateValue(cellValue)) {
      return ColumnType.DATE_COLUMN;
    }
    return ColumnType.STRING_COLUMN;
  }

  /**
   * Determines the type of the column.
   *
   * @param columnTypeMap map holding a count for each possible column type
   * @return the column type with the highest occurrence
   */
  private ColumnType determineColumnType(Map<ColumnType, Integer> columnTypeMap) {
    ColumnType columnType = ColumnType.STRING_COLUMN;
    Integer count = 0;
    for (Map.Entry<ColumnType, Integer> entry : columnTypeMap.entrySet()) {
      if (entry.getValue() > count) {
        columnType = entry.getKey();
        count = entry.getValue();
      }
    }
    return columnType;
  }

  /**
   * Computes the count of distinct values based on the histogram data
   */
  protected void computeDistinctValuesCount() {
    if (this.histogram != null) {
      this.distinctValuesCount = this.histogram.getHistogramData().keySet().size();
    }
  }

  /**
   * Computes the count of null values based on the histogram data
   */
  protected void computeNullValuesCount() {
    if (this.histogram != null) {
      this.nullValuesCount = this.histogram.getNullCount();
    }
  }

  /**
   * Checks if the given value is a boolean value
   *
   * @param value string value which should be checked
   * @return true if the value has boolean format, false otherwise
   */
  protected boolean isBooleanValue(String value) {
    // Check both values for a common boolean descriptor (lower case)
    String[] commonNegativeDescriptorsArray = {"0", "false", "f", "no", "n"};
    String[] commonPositiveDescriptorsArray = {"1", "true", "t", "yes", "y"};
    Set<String> commonNegativeDescriptors =
      new HashSet<>(Arrays.asList(commonNegativeDescriptorsArray));
    Set<String> commonPositiveDescriptors =
      new HashSet<>(Arrays.asList(commonPositiveDescriptorsArray));

    return commonNegativeDescriptors.contains(value.toLowerCase()) ||
      commonPositiveDescriptors.contains(value.toLowerCase());
  }

  /**
   * Checks if the given value is an integer value
   *
   * @param value string value which should be checked
   * @return true if the value has integer format, false otherwise
   */
  protected boolean isIntegerValue(String value) {
    return value.matches("\\d+");
  }

  /**
   * Checks if the given value is a float value
   *
   * @param value string value which should be checked
   * @return true if the value have float format, false otherwise
   */
  protected boolean isFloatValue(String value) {
    return value.matches("[+-]?(?=\\d*[.eE])(?=\\.?\\d)\\d*\\.?\\d*(?:[eE][+-]?\\d+)?");
  }

  /**
   * Checks if the given value is a date value
   *
   * @param value string value which should be checked
   * @return true if the value has date format, false otherwise
   */
  protected boolean isDateValue(String value) {
    // TODO optimize data formats and add further ones
    String[] dateFormats = {"M/dd/yyyy",
      "dd.M.yyyy",
      "M/dd/yyyy hh:mm:ss a",
      "dd.M.yyyy hh:mm:ss a",
      "dd.MMM.yyyy",
      "dd-MMM-yyyy"};

    for (String dateFormat : dateFormats) {
      // If one format fits then the value is a date
      if (this.isDateValid(value, dateFormat)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the provided value can be parsed to a date with provided format
   *
   * @param dateToValidate String which should be a date
   * @param dateFormat     Format of the possible date
   * @return true if the string can be parsed to a legal date using given format, false otherwise
   */
  protected boolean isDateValid(String dateToValidate, String dateFormat) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    // Set it to lenient, otherwise illegal days in month and leap years will not be checked
    simpleDateFormat.setLenient(false);

    try {
      simpleDateFormat.parse(dateToValidate);
    } catch (ParseException e) {
      return false;
    }
    return true;
  }

  /**
   * Computes the average value length for string columns
   */
  protected void computeAverageLength() {
    // Ignore non-string columns
    if (this.columnType != ColumnType.STRING_COLUMN) {
      this.averageValueLength = 0.0f;
      return;
    }

    // Sum up the length of cell values
    long lengthSum = 0l;
    for (Map.Entry<String, Long> entry : this.histogram.getHistogramData().entrySet()) {
      if (entry.getKey() != null) {
        lengthSum += entry.getKey().length() * entry.getValue();
      }
    }

    this.averageValueLength = (float) lengthSum / this.rowCount;
  }

  /**
   * Check if the column is unique
   *
   * @return Returns true if the count of distinct values is equal to the number of table rows,
   * false otherwise
   */
  public boolean isUniqueColumn() {
    return this.getDistinctValuesCount() == this.getRowCount();
  }

  /**
   * Provides the uniqueness rate of the column scaled from 0 to 1: 0 is constant and 1 unique
   *
   * @return float uniqueness measure in interval [0,1], 0 for constant and 1 for unique columns
   */
  public double getUniquenessRate() {
    return this.rowCount == 0 ? 1.0 : (double) this.distinctValuesCount / (double) this.rowCount;
  }

  /**
   * Provides the null rate of the column scaled from 0 to 1: 0 is none null values and 1 is all
   * null values
   *
   * @return float uniqueness measure in interval [0,1], 0 for constant and 1 for unique columns
   */
  public double getNullRate() {
    return this.rowCount == 0 ? 0.0 : (double) this.nullValuesCount / (double) this.rowCount;
  }

  /**
   * Calculates the bytes needed by the column for the given number of rows.
   *
   * @param rows the number of rows
   * @return the number of bytes needed
   */
  public long getInformationContent(long rows) {
    float factor = contentSizes.get(this.columnType);
    if (this.columnType == ColumnType.STRING_COLUMN) {
      factor *= this.averageValueLength;
    }
    return (long) (factor * rows);
  }


  public String getColumnName() {
    return columnName;
  }

  public long getDistinctValuesCount() {
    return distinctValuesCount;
  }

  public long getNullValuesCount() {
    return nullValuesCount;
  }

  public ColumnType getColumnType() {
    return columnType;
  }

  public Histogram getHistogram() {
    return histogram;
  }

  public long getRowCount() {
    return rowCount;
  }

  public float getAverageValueLength() {
    return averageValueLength;
  }

  public BitSet getBitSet() {
    return bitSet;
  }

  public int getColumnIndex() {
    return columnIndex;
  }

  /**
   * Defines different types of a column
   */
  public enum ColumnType {
    INTEGER_COLUMN,
    FLOAT_COLUMN,
    DATE_COLUMN,
    BOOLEAN_COLUMN,
    STRING_COLUMN
  }
}
