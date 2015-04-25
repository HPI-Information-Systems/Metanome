package de.metanome.backend.result_postprocessing.result_store;

import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.result_postprocessing.result_comparator.ResultComparator;

import java.util.Collections;
import java.util.List;

/**
 * Abstract class providing basic functionality for storing ranking results of one execution
 */
public abstract class ResultsStore<ResultType extends Result> {

  // List of results
  protected List<ResultType> results = null;
  // Last sort property, to reduce resorting needs
  protected String lastSortProperty = null;
  // Last sort direction, to reduce resorting needs
  protected boolean lastSortAscending = true;

  /**
   * Stores the given results for given execution
   *
   * @param results     List of results to be persisted
   */
  public void store(List<ResultType> results) {
    this.results = results;
  }

  /**
   * Clears the store content
   */
  public void clear() {
    this.results.clear();
  }

  /**
   * Returns the count of results
   *
   * @return Returns the count of results
   */
  public Integer count() {
    return results.size();
  }

  /**
   * Returns the persisted results
   *
   * @return Returns the results
   */
  public List<ResultType> list() {
    return results;
  }

  /**
   * Returns a part of persisted results for given execution following given properties
   *
   * @param sortProperty Sort property the list should be sorted on
   * @param ascending    Sort direction
   * @param start        Inclusive start index
   * @param end          Exclusive end index
   * @return Returns a part of persisted results for given execution following given properties
   */
  public List<ResultType> subList(String sortProperty, boolean ascending,
                                  int start, int end) {
    start = Math.max(0, start);
    end = Math.min(count(), end);

    // Do not resort if it is not needed
    if (sortProperty.equals(lastSortProperty) && lastSortAscending == ascending) {
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
   * @param ascending    Sort direction
   */
  private void sort(String sortProperty, boolean ascending) {
    this.lastSortAscending = ascending;
    this.lastSortProperty = sortProperty;
    Collections.sort(this.results, getResultComparator(sortProperty, ascending));
  }

  /**
   * Template method to define a result comparator of appropriate type
   *
   * @param sortProperty Sort property
   * @param ascending    Sort direction
   * @return Returns a new result comparator of appropriate type
   */
  protected abstract ResultComparator<ResultType> getResultComparator(String sortProperty,
                                                                      boolean ascending);
}
