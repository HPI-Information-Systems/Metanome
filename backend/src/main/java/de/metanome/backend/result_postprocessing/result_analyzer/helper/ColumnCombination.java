package de.metanome.backend.result_postprocessing.result_analyzer.helper;

import de.metanome.algorithm_integration.ColumnIdentifier;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexander Spivak and Daniel Roeder on 30.10.2014.
 *
 * The column combination class provides efficient operations like merging and containing tests.
 */
public class ColumnCombination extends BitSet implements Comparable<ColumnCombination>, Iterable<Integer>,
                                                         Serializable {

  // Used for serialization
  private static final long serialVersionUID = -3597181934080479166L;

  //<editor-fold desc="Constructors">

  /**
   * Creates an empty or full column combination as bit sequence
   * @param columnCount Total number of columns inside the table
   * @param flipped Are all columns set (true) or not set (false)
   */
  public ColumnCombination(int columnCount, boolean flipped) {
    super(columnCount);
    if(flipped)
      this.flip(0, columnCount);
  }

  /**
   * Creates an empty column combination as bit sequence
   * @param columnCount Total number of columns inside the table
   */
  public ColumnCombination(int columnCount) {
    super(columnCount);
  }

  /**
   * Creates a column combination with one column as bit sequence
   * @param columnIndex Column number of the given column.
   * @param columnCount Total number of columns inside the table
   */
  public ColumnCombination(int columnIndex, int columnCount) {
    super(columnCount);
    this.set(columnIndex);
  }

  /**
   * Creates a column combination as bit sequence using given column indices
   * @param columnIndices List of column numbers
   * @param columnCount Total number of columns inside the table
   */
  public ColumnCombination(List<Integer> columnIndices, int columnCount) {
    super(columnCount);
    for(Integer columnIndex : columnIndices){
      this.set(columnIndex);
    }
  }
  //</editor-fold>

  //<editor-fold desc="Copy methods">
  /**
   * Provides an exact copy of the object
   * @return Returns an exact copy of the object
   */
  public ColumnCombination copy(){
    return (ColumnCombination)this.clone();
  }

  /**
   * Provides a flipped copy of the column combination
   * @return Returns a flipped copy of the column combination
   */
  public ColumnCombination flippedCopy() {
    ColumnCombination flippedCopy = this.copy();
    flippedCopy.flip(0, flippedCopy.getTableColumnCount());
    return flippedCopy;
  }
  //</editor-fold>

  //<editor-fold desc="Combinational operations">
  /**
   * Merges two combinations into one
   * @param mergeCandidate A column combination to merge with the current one
   * @return Returns a new column combination created from merging both CCs
   */
  public ColumnCombination merge(ColumnCombination mergeCandidate) {
    ColumnCombination result = this.copy();
    result.or(mergeCandidate);
    return result;
  }

  /**
   * Subtracts the columns of the subtrahend from the calling column combination
   * @param subtrahend Columns which should be removed from the calling column combination
   * @return Returns a copy of calling column combination without the columns of the subtrahend
   */
  public ColumnCombination subtract(ColumnCombination subtrahend){
    ColumnCombination result = this.copy();
    result.andNot(subtrahend);
    return result;
  }

  /**
   * Check if the column combination contains the provided one
   * @param containedCC Column combination which should be tested for being contained
   * @return Returns true if the calling CC contains the parameter CC, false otherwise
   */
  public boolean contains(ColumnCombination containedCC) {
    ColumnCombination intermediate = this.copy();
    intermediate.or(containedCC);
    return intermediate.equals(this);
  }

  private Set<ColumnCombination> recursivePowerSet(ColumnCombination cc){
    Set<ColumnCombination> result = new HashSet<>();
    for(Integer column : cc){
      ColumnCombination newColumnCombination = cc.copy();
      newColumnCombination.set(column, false);
      result.add(newColumnCombination);
      result.addAll(recursivePowerSet(newColumnCombination));
    }
    return result;
  }

  public Set<ColumnCombination> powerSet(){
    return recursivePowerSet(this);
  }
  //</editor-fold>

  //<editor-fold desc="Count getter">
  /**
   * Returns the number of columns used in the combination
   * @return Returns the number of columns used in the combination
   */
  public int getColumnCount(){
      return this.cardinality();
  }

  /**
   * Returns the number of columns in the table the combination is used for
   * @return Returns the number of columns in the table the combination is used for
   */
  public int getTableColumnCount() { return this.length();}
  //</editor-fold>

  //<editor-fold desc="Indices access">
  /**
   * Creates a column identifier array for returning results to Metanome
   * @param tableName Name of the table
   * @param columnNames List with names of columns ordered by their numbers
   * @return Returns an array of column identifiers which are used by Metanome
   */
  public ColumnIdentifier[] toColumnIdentifier(String tableName, List<String> columnNames) {
    // Get the first column number
    List<ColumnIdentifier> result = new ArrayList<>();
    // Iterate over all column ids of the combination
    for(int setBitIndex = this.nextSetBit(0); setBitIndex >= 0; setBitIndex = this.nextSetBit(setBitIndex + 1)){
      // Create a new column identifier and add it to the result
      result.add(new ColumnIdentifier(tableName, columnNames.get(setBitIndex)));
    }
    return result.toArray(new ColumnIdentifier[result.size()]);
  }

  /**
   * Returns a list of column indices of columns used in the combination
   * @return Returns a list of column indices of columns used in the combination
   */
  public List<Integer> getColumnIndices(){
    List<Integer> result = new ArrayList<>(this.getTableColumnCount());

    // Iterate over all columns and add all used to the result
    for(int i=0; i<=this.length(); i++){
      if(this.get(i)){
        result.add(i);
      }
    }

    return result;
  }

  /**
   * Creates a column combination for each single column in the combination and returns them as list
   * @return Returns a list of column combinations for each single column in the called combination
   */
  public List<ColumnCombination> getColumnsAsCombinations(){
    List<ColumnCombination> result = new ArrayList<>(this.getColumnCount());
    // Get all column indices
    List<Integer> columnIndices = this.getColumnIndices();
    // Create a column combination for each index and add to result
    for(Integer columnIndex : columnIndices){
      result.add(new ColumnCombination(columnIndex, this.getTableColumnCount()));
    }

    return result;
  }
  //</editor-fold>

  //<editor-fold desc="Comparing method">
  /**
   * Provides a method for comparing two column combinations (used for sorting algorithms)
   * If you need an other comparing way, define a Comparator-Class like ColumnCombinationSizeComparator
   * @param o ColumnCombination which should be compared
   * @return Returns 0 if both CCs have the same columns, 1 if the current CC has the a column with higher index than o, otherwise -1
   */
  @Override
  public int compareTo(ColumnCombination o) {
    ColumnCombination intermediate = this.copy();
    intermediate.xor(o);
    int firstUnequalBit = intermediate.previousSetBit(intermediate.length());
    if (firstUnequalBit == -1) {
      return 0;
    }
    return (this.get(firstUnequalBit) ? 1 : -1);
  }
  //</editor-fold>

  //<editor-fold desc="Iterator">

  @Override
  public Iterator<Integer> iterator() {
    return new ColumnIterator();

  }

  /**
   * Allows to iterate over column indices of the column combination.
   */
  class ColumnIterator implements Iterator<Integer>{

    private Integer currentIndex = 0;

    @Override
    public boolean hasNext() {
      return ColumnCombination.this.nextSetBit(currentIndex) != -1;
    }

    @Override
    public Integer next() {
      Integer resultIndex = ColumnCombination.this.nextSetBit(currentIndex);
      currentIndex = resultIndex + 1;
      return resultIndex;
    }

  }
  //</editor-fold>

}