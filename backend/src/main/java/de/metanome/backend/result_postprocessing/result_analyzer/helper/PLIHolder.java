package de.metanome.backend.result_postprocessing.result_analyzer.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander Spivak and Daniel Roeder on 30.10.2014.
 *
 * Holds the position list indices referred to the column combination they are used for.
 */
public class PLIHolder {

  // Map for storing the PLIs
  private Map<ColumnCombination, PLIList> pliMap = new HashMap<>();

  //<editor-fold desc="Getter">

  public Map<ColumnCombination, PLIList> getPliMap() {
    return pliMap;
  }

  //</editor-fold>

  //<editor-fold desc="Drop methods">

  /**
   * Drops all PLIs for column combinations consisting of "size" columns
   * @param size Size of column combinations for which the PLIs should be dropped
   */
  public void dropPLIs(int size) {
    for (Iterator<Map.Entry<ColumnCombination, PLIList>> it = this.pliMap.entrySet().iterator();
         it.hasNext(); ) {
      Map.Entry<ColumnCombination, PLIList> entry = it.next();
      if (entry.getKey().cardinality() <= size) {
        it.remove();
      }
    }
  }

  /**
   * Drops PLI for a given column combination
   * @param columnCombination Column combination for which the PLI should be dropped
   */
  public void dropPLI(ColumnCombination columnCombination) {
    if (this.pliMap.containsKey(columnCombination)) {
      this.pliMap.remove(columnCombination);
    }
  }

  //</editor-fold>

  //<editor-fold desc="Map access methods">

  /**
   * Adds all entries of the provided PLI holder to the own PLI map
   * @param otherHolder Holder of PLIs which should be added to the calling holder
   */
  public void addAll(PLIHolder otherHolder){
    if(otherHolder != null) {
      pliMap.putAll(otherHolder.getPliMap());
    }
  }

  //</editor-fold>

  //<editor-fold desc="Intersection method">

  /**
   * Calculates the intersection of PLIs of each column in the combination
   * @param columnsToIntersect Columns for which the PLIs should be intersected
   * @return Returns a PLI containing the intersection result of each column in the combination
   */
  public PLIList intersectLists(ColumnCombination columnsToIntersect){

    List<ColumnCombination> columns = columnsToIntersect.getColumnsAsCombinations();

    // If no columns are provided, return null
    if(columns.isEmpty())
      return null;

    // If a desired column is not available, return null
    for(ColumnCombination column : columns){
      if(!pliMap.containsKey(column)){
        return null;
      }
    }

    // Get the first PLI and intersect everything with it
    PLIList resultPLI = pliMap.get(columns.get(0));
    // Intersect all other PLIs
    for(int columnIndex = 1; columnIndex < columns.size(); columnIndex++){
      resultPLI = PLIList.intersect(resultPLI, pliMap.get(columns.get(columnIndex)));
    }

    return resultPLI;
  }

  //</editor-fold>
}
