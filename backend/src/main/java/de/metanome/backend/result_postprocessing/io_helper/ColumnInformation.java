package de.metanome.backend.result_postprocessing.io_helper;

import com.github.slugify.Slugify;

import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provides metadata and statistics about a table column
 *
 * Created by Alexander Spivak on 15.11.2014.
 */
public class ColumnInformation {

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

  //<editor-fold desc="Constants">

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
    sizes.put(ColumnInformation.ColumnType.STRING_COLUMN, 1);

    contentSizes = Collections.unmodifiableMap(sizes);
  }

  //</editor-fold>

  //<editor-fold desc="Private attributes">

  // Name of the column
  private String columnName = null;
  // Index of the column inside the table (starting with 0)
  private int columnIndex = 0;
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

  //</editor-fold>

  //<editor-fold desc="Constructor">
  /**
   * Creates a new column information for given column name and index
   * @param columnName Name of the column
   * @param columnIndex Index of the column
   * @param relationalInput Relational input used to provide the column information
   * @param useRowData Is it allowed to access row data or only table header data?
   */
  public ColumnInformation(String columnName, int columnIndex, RelationalInput relationalInput, boolean useRowData){
    super();
    this.columnName = columnName;
    this.columnIndex = columnIndex;
    if(useRowData) {
      this.compute(relationalInput);
    }
  }
  //</editor-fold>

  //<editor-fold desc="Getter">
  public String getColumnName() {
    return columnName;
  }

  public String getSlugifiedColumnName() throws IOException {
    return (new Slugify()).slugify(columnName);
  }

  public int getColumnIndex() {
    return columnIndex;
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

  //</editor-fold>

  //<editor-fold desc="Private column type checker">
  /**
   * Checks the column for being a boolean column
   * @return Returns true if the column contains maximal 2 values which are usually used for boolean values.
   */
  private boolean isPossibleBooleanColumn(){
    // Get the value set from histogram
    Set<String> valueSet = this.histogram.getHistogramData().keySet();
    // A boolean column can contain maximal two values
    if (valueSet.size() > 2) {
      return false;
    }
    // Check both values for a common boolean descriptor (lower case)
    String[] commonNegativeDescriptorsArray = {"0", "false", "f", "no", "n"};
    String[] commonPositiveDescriptorsArray = {"1", "true", "t", "yes", "y"};
    Set<String> commonNegativeDescriptors = new HashSet<>(Arrays.asList(commonNegativeDescriptorsArray));
    Set<String> commonPositiveDescriptors = new HashSet<>(Arrays.asList(commonPositiveDescriptorsArray));
    String negativeDescriptorInData = "", positiveDescriptorInData = "";
    for(String value : valueSet){
      if(commonNegativeDescriptors.contains(value)) {
        if ("".equals(negativeDescriptorInData)) {
          negativeDescriptorInData = value;
        } else
          if (!value.equals(negativeDescriptorInData)) {
            return false;
          }
      }
      if(commonPositiveDescriptors.contains(value)) {
        if ("".equals(positiveDescriptorInData)) {
          positiveDescriptorInData = value;
        } else
        if (!value.equals(positiveDescriptorInData)) {
          return false;
        }
      }

    }
    if (!"".equals(negativeDescriptorInData) && !"".equals(positiveDescriptorInData))
      return true;
    return false;
  }

  /**
   * Checks a value for being a possible integer
   * @param value String value which should be checked
   * @return Returns true if the value have integer format, false otherwise
   */
  private boolean isPossibleIntegerValue(String value){
    return value.matches("\\d+");
  }

  /**
   * Checks a value for being a possible float
   * @param value String value which should be checked
   * @return Returns true if the value have float format, false otherwise
   */
  private boolean isPossibleFloatValue(String value){
    return value.matches("[+-]?(?=\\d*[.eE])(?=\\.?\\d)\\d*\\.?\\d*(?:[eE][+-]?\\d+)?");
  }

  /**
   * Checks if the provided value can be parsed to a date with provided format
   * @param dateToValidate String which should be a date
   * @param dateFormat Format of the possible date
   * @return Returns true if the string can be parsed to a legal date using given format, false otherwise
   */
  private boolean isDateValid(String dateToValidate, String dateFormat){
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
    // Set it to lenient, otherwise illegal days in month and leap years will not be checked
    simpleDateFormat.setLenient(false);

    try {
      //If the date is not valid the exception will be thrown
      simpleDateFormat.parse(dateToValidate);
    } catch (ParseException e) {
      return false;
    }

    return true;
  }

  /**
   * Checks a value for being a possible date
   * @param value String value which should be checked
   * @return Returns true if the value have date format, false otherwise
   */
  private boolean isPossibleDateValue(String value){
    // TODO optimize data formats and add further ones
    String[] dateFormats = {"M/dd/yyyy",
                            "dd.M.yyyy",
                            "M/dd/yyyy hh:mm:ss a",
                            "dd.M.yyyy hh:mm:ss a",
                            "dd.MMM.yyyy",
                            "dd-MMM-yyyy"};
    // Check all formats
    for(String dateFormat : dateFormats){
      // If one format fits then the value is a possible date
      if(this.isDateValid(value, dateFormat)){
        return true;
      }
    }
    return false;
  }
  //</editor-fold>

  //<editor-fold desc="Private metadata computation">
  /**
   * Computes a histogram for the column values based on relational input
   * @param relationalInput Relational input describing the input data
   */
  private void computeHistogram(RelationalInput relationalInput){

    // Create new histogram and a new row counter
    this.histogram = new Histogram();
    this.rowCount = 0L;

    // Iterate over data and add the cell value to the histogram
    try {
      while (relationalInput.hasNext()) {
        this.rowCount++;
        List<String> row = relationalInput.next();
        this.histogram.putNewValue(row.get(this.getColumnIndex()));
      }
    }
    catch (InputIterationException e) {
      e.printStackTrace();
    }
  }

  /**
   * Computes the count of distinct values based on the histogram data
   * Ensure that the histogram already exists!
   */
  private void computeDistinctValuesCount(){
    this.distinctValuesCount = this.histogram.getHistogramData().keySet().size();
  }

  /**
   * Computes the count of null values based on the histogram data
   * Ensure that the histogram already exists!
   */
  private void computeNullValuesCount(){
    this.nullValuesCount = this.histogram.getNullCount();
  }

  /**
   * Computes the type of the column
   * Ensure that the histogram already exists!
   * TODO Check the implementation for better solutions
   */
  private void computeType(){
    // Get all distinct values
    Set<String> distinctValues = this.histogram.getHistogramData().keySet();
    // Remove the null value, otherwise all value calls will fail
    distinctValues.remove(null);
    // Prepare checking structures
    boolean isInteger, isFloat, isDate;
    isInteger = true;
    isFloat = true;
    isDate = false;

    if(this.isPossibleBooleanColumn()){
      this.columnType = ColumnType.BOOLEAN_COLUMN;
      return;
    }

    // Check all types for each distinct value (dont perform the type check if it is already failed once)
    for(String value : distinctValues){
      // Check for being integer
      if(isInteger && !this.isPossibleIntegerValue(value)){
        isInteger = false;
      }
      // Check for being float
      if(isFloat && !this.isPossibleFloatValue(value)){
        isFloat = false;
      }
      // Check for being date
      if(isDate && !this.isPossibleDateValue(value)){
        isDate = false;
      }
      // If all data types are not fit to column, abort the check
      if(!isDate && !isFloat && !isInteger){
        break;
      }
    }

    // Check which type should be used (Order is important, because each integer matches also as float)
    if(isInteger){
      this.columnType = ColumnType.INTEGER_COLUMN;
      return;
    }
    if(isFloat){
      this.columnType = ColumnType.FLOAT_COLUMN;
      return;
    }
    if(isDate){
      this.columnType = ColumnType.DATE_COLUMN;
      return;
    }

    // If nothing match for given column, use string as type
    this.columnType = ColumnType.STRING_COLUMN;
  }

  /**
   * Computes the average value length for string columns
   */
  private void computeAverageLength(){

    // Ignore non-string columns
    if(this.columnType != ColumnType.STRING_COLUMN){
      this.averageValueLength = 0.0f;
      return;
    }

    // Sum up the length of cell values
    long lengthSum = 0l;
    for(Map.Entry<String, Long> entry : this.histogram.getHistogramData().entrySet()){
      lengthSum += entry.getKey().length() * entry.getValue();
    }

    this.averageValueLength = (float)lengthSum/this.rowCount;
  }

  /**
   * Computes all column metadata based on provided input iterator
   * @param relationalInput Iterator on input data
   */
  private void compute(RelationalInput relationalInput){
    // Compute histogram
    this.computeHistogram(relationalInput);
    // Compute number of distinct values
    this.computeDistinctValuesCount();
    // Compute number of null values
    this.computeNullValuesCount();
    // Compute the probable data type of the column
    this.computeType();
    // Compute the average length of a string column values
    this.computeAverageLength();
  }
  //</editor-fold>

  //<editor-fold desc="Public rates and information content computations">
  /**
   * Check if the column is unique
   * @return Returns true if the count of distinct values is equal to the number of table rows, false otherwise
   */
  public boolean isUniqueColumn(){
    return this.getDistinctValuesCount() == this.getRowCount();
  }

  /**
   * Provides the uniqueness rate of the column scaled from 0 to 1: 0 is constant and 1 unique
   * @return Returns a float uniqueness measure in interval [0,1], 0 for constant and 1 for unique columns
   */
  public double getUniquenessRate(){
    return this.rowCount == 0 ? 1.0 : (double)this.distinctValuesCount/(double)this.rowCount;
  }

  public double getNullRate(){
    return this.rowCount == 0 ? 0.0 : (double)this.nullValuesCount/(double)this.rowCount;
  }

  public long getInformationContent(long rows){
    float factor = contentSizes.get(this.columnType);
    if(this.columnType == ColumnType.STRING_COLUMN)
      factor *= averageValueLength;
    return (long)(factor * rows);
  }
  //</editor-fold>
}
