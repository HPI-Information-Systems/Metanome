package de.metanome.backend.result_postprocessing.helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a simple single key value (no ranges) histogram
 */
public class Histogram {

  protected Map<String, Long> histogramData;

  public Histogram() {
    // Needs to be a hash map, because tree maps don't accept "null" as key
    this.histogramData = new HashMap<>();
  }

  public Map<String, Long> getHistogramData() {
    return this.histogramData;
  }

  /**
   * Provides the number of null values in the histogram
   *
   * @return the count of null values in the histogram
   */
  public Long getNullCount() {
    return this.histogramData.containsKey(null) ? this.histogramData.get(null) : 0L;
  }

  /**
   * Adds a new value to the histogram: it increases the value count of the already stored key or
   * store the value as new key
   *
   * @param v value to add
   */
  public void addValue(String v) {
    if (this.histogramData.containsKey(v)) {
      this.histogramData.put(v, this.histogramData.get(v) + 1L);
    } else {
      this.histogramData.put(v, 1L);
    }
  }

  /**
   * Creates the histogram from the given column values
   *
   * @param columnValues list of all column values
   */
  public void computeHistogram(List<String> columnValues) {
    for (String v : columnValues) {
      addValue(v);
    }
  }
}
