package de.metanome.backend.result_postprocessing.result_analyzer.helper;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.List;
import java.util.Map;

/**
 * Created by Alexander Spivak on 29.12.2014.
 *
 * Implementation of a Position List Index structure.
 * Provides the possibility to create and intersect the PLIs and to compute the key error.
 */
public class PLIList {

  // Build the structure with "fastutils" collections
  public ObjectArrayList<IntSet> pliList;
  // Cache for the key error
  private int keyError = -1;

  //<editor-fold desc="Constructors">
  /**
   * Creates a PLI structure from a list of integer sets
   * @param pliList List of integer sets representing the duplicates
   */
  public PLIList(List<IntSet> pliList){
    this.pliList = new ObjectArrayList<>(pliList.size());
    for(IntSet duplicates : pliList){
      this.pliList.add(new IntOpenHashSet(duplicates));
    }
  }

  /**
   * Creates a PLI structure from a map of values to the tuple indices
   * @param columnValueToIndices Map values to the tuple indices with the same value
   */
  public PLIList(Map<String,List<Integer>> columnValueToIndices){

    pliList = new ObjectArrayList<>();

    // Iterate over the value sets and only regard them if they describe duplicates
    for(Map.Entry<String, List<Integer>> pliEntry : columnValueToIndices.entrySet()){
      if(pliEntry.getValue().size() > 1) {
        pliList.add(new IntOpenHashSet(pliEntry.getValue()));
      }
    }
  }
  //</editor-fold>

  //<editor-fold desc="PLI computations">
  /**
   * Computes the key error of the PLI
   * @return Returns the key error of the PLI
   */
  public int computeKeyError(){

    // Lazy initialization
    if(keyError == -1) {
      // Compute the size of the duplicate lists
      int sumDuplicateLists = 0;
      for (IntSet duplicateIndices : this.pliList) {
        sumDuplicateLists += duplicateIndices.size();
      }
      // The result is the previous sum subtracted by the number of duplicate lists
      keyError = sumDuplicateLists - this.pliList.size();
    }

    return keyError;
  }

  /**
   * Intersects two PLIs and create a new PLI as result
   * @param leftList Left PLI to be intersected with the right PLI
   * @param rightList Right PLI to be intersected with the left PLI
   * @return Returns a new PLI representing the intersection result of the provided lists
   */
  public static PLIList intersect(PLIList leftList, PLIList rightList){
    ObjectArrayList<IntSet> pliList = new ObjectArrayList<>();

    IntSet regardedIndices = new IntOpenHashSet();

    // Iterate over all duplicate sets of the left list
    for(IntSet duplicatedIndices : leftList.pliList){
      // Iterate over all duplicate indices in the duplicate set
      for(Integer index : duplicatedIndices){
        // Do not regard indices already used for computation
        if(regardedIndices.contains(index))
          continue;
        // Find the duplicate indices list on the right side containing the same index
        for(IntSet rightDuplicatedIndices : rightList.pliList){
          if(rightDuplicatedIndices.contains(index)){
            // Compute the intersection between the lists
            IntSet intersection = new IntOpenHashSet(duplicatedIndices);
            intersection.retainAll(rightDuplicatedIndices);
            // Only add to result if the intersection contains duplicates
            if(intersection.size() > 1){
              pliList.add(intersection);
            }
            // All found duplicates do not need to be regarded again!
            regardedIndices.addAll(intersection);
          }
        }
        // Mark index as regarded
        regardedIndices.add(index);
      }
    }

    return new PLIList(pliList);

  }
  //</editor-fold>

}
