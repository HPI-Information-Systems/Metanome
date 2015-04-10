package de.metanome.backend.result_postprocessing.io_helper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides a simple single key value (no ranges) histogram
 *
 * Created by Alexander Spivak on 15.11.2014.
 */
public class Histogram {

  private Map<String, Long> histogramData;

  //<editor-fold desc="Constructor">

  public Histogram(){
    // Needs to be a hash map, because tree maps don't accept "null" as key
    this.histogramData = new HashMap<>();
  }

  //</editor-fold>

  //<editor-fold desc="Getter">

  public Map<String, Long> getHistogramData(){
    return this.histogramData;
  }

  /**
   * Provides the number of null values in the histogram
   * @return Returns the count of null values in the historgram if null appears in column, 0 otherwise
   */
  public Long getNullCount(){
    return this.histogramData.containsKey(null) ? this.histogramData.get(null) : 0L;
  }

  //</editor-fold>

  //<editor-fold desc="Histogram creation">

  /**
   * Adds a new walue to histogram, will increase the value count or store the value if it is new
   * @param cellValue A value of the cell which should be part of the histogram
   */
  public void putNewValue(String cellValue){
    if (this.histogramData.containsKey(cellValue)) {
      this.histogramData.put(cellValue, this.histogramData.get(cellValue) + 1L);
    } else {
      this.histogramData.put(cellValue, 1L);
    }
  }

  /**
   * Computes the histogram out of the column values given as list
   * @param columnValues A list of values of one column
   */
  public void computeHistogram(List<String> columnValues){
    for(String cellValue : columnValues){
        putNewValue(cellValue);
    }
  }

  //</editor-fold>
}
