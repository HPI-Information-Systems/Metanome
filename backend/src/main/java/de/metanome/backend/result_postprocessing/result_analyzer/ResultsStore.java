package de.metanome.backend.result_postprocessing.result_analyzer;

import java.util.Collections;
import java.util.List;

/**
 * Abstract class providing basic functionality for storing ranking results of one execution
 *
 * Created by Alexander Spivak on 04.03.2015.
 */
public abstract class ResultsStore<ResultType> {

  //<editor-fold desc="Protected attributes">

  // Execution ID
  protected long executionID = 0l;
  // List of results
  protected List<ResultType> results = null;
  // Last sort property, to reduce resorting needs
  protected String lastSortProperty = null;
  // Last sort direction, to reduce resorting needs
  protected boolean lastSortAscending = true;

  //</editor-fold>

  /**
   * Stores the given results for given execution
   *
   * @param results List of results to be persisted
   * @param executionID ID of the execution which leaded to the results
   */
  public void store(List<ResultType> results, long executionID){
    this.results = results;
    this.executionID = executionID;
  }

  /**
   * Clears the store content
   */
  public void clear(){
    this.results.clear();
    this.executionID = 0l;
  }

  /**
   * Returns the count of results for the given execution
   *
   * @param executionID ID of the execution
   * @return Returns the count of results, if the execution results are persisted, 0 otherwise
   */
  public long count(long executionID){
    if(executionID == this.executionID) {
      return results.size();
    }
    return 0l;
  }

  /**
   * Returns the persisted results for given execution
   *
   * @param executionID ID of the execution
   * @return Returns the results, if the execution results are persisted, null otherwise
   */
  public List<ResultType> list(long executionID){
    if(executionID == this.executionID) {
      return results;
    }
    return null;
  }

  /**
   * Returns a part of persisted results for given execution following given properties
   *
   * @param executionID ID of the execution
   * @param sortProperty Sort property the list should be sorted on
   * @param ascending Sort direction
   * @param start Inclusive start index
   * @param end Exclusive end index
   * @return Returns a part of persisted results for given execution following given properties
   */
  public List<ResultType> subList(long executionID, String sortProperty, boolean ascending, int start, int end){

    if(executionID != this.executionID){
      return null;
    }

    start = Math.max(0, start);
    end = Math.min((int)count(executionID), end);

    // Do not resort if it is not needed
    if(sortProperty.equals(lastSortProperty) && lastSortAscending == ascending){
      return results.subList(start, end);
    }

    // Sort the list
    sort(sortProperty, ascending);

    // Return the requested part
    return results.subList(start, end);
  }

  /**
   * Sorts the persisted results based on given properties
   *
   * @param sortProperty Sort property
   * @param ascending Sort direction
   */
  private void sort(String sortProperty, boolean ascending){
    Collections.sort(this.results, getResultComparator(sortProperty, ascending));
  }

  /**
   * Template method to define a result comparator of appropriate type
   *
   * @param sortProperty Sort property
   * @param ascending Sort direction
   * @return Returns a new result comparator of appropriate type
   */
  protected abstract ResultComparator<ResultType> getResultComparator(String sortProperty, boolean ascending);
}
