/*
 * Copyright 2014 by the Metanome project
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

package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;

import org.apache.lucene.util.OpenBitSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * A representation for column combinations (attribute sets) using {@link
 * org.apache.lucene.util.OpenBitSet}s.
 *
 * @author Jakob Zwiener
 * @author Jens Hildebrandt
 * @author Lukas Schulze
 * @author Mandy Roick
 */
public class ColumnCombinationBitset {

  protected OpenBitSet bitset;
  protected long size = 0;

  public ColumnCombinationBitset(int... columnIndeces) {
    bitset = new OpenBitSet();

    for (int columnIndex : columnIndeces) {
      // If the bit was not yet set, increase the size.
      addColumn(columnIndex);
    }
  }

  /**
   * Creates a copy of the current instance.
   *
   * @param columnCombination that is cloned to the new instance
   */
  public ColumnCombinationBitset(ColumnCombinationBitset columnCombination) {
    setColumns(columnCombination.bitset.clone());
  }

  /**
   * Sets the given {@link OpenBitSet}, the previous state is overwritten!
   *
   * @param bitset set on the existing ColumnCombinationBitset
   * @return the instance
   */
  protected ColumnCombinationBitset setColumns(OpenBitSet bitset) {
    this.bitset = bitset;
    size = bitset.cardinality();

    return this;
  }

  /**
   * Adds a column to the bit set.
   *
   * @param columnIndex of column to add
   * @return the column combination
   */
  public ColumnCombinationBitset addColumn(int columnIndex) {
    if (!bitset.get(columnIndex)) {
      size++;
    }

    bitset.set(columnIndex);

    return this;
  }

  /**
   * Removes a column from the bit set.
   *
   * @param columnIndex of column to remove
   * @return the column combination
   */
  public ColumnCombinationBitset removeColumn(int columnIndex) {
    if (bitset.get(columnIndex)) {
      size--;
    }

    bitset.clear(columnIndex);

    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bitset == null) ? 0 : bitset.hashCode());
    result = prime * result + (int) (size ^ (size >>> 32));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ColumnCombinationBitset other = (ColumnCombinationBitset) obj;
    if (bitset == null) {
      if (other.bitset != null) {
        return false;
      }
    } else if (!bitset.equals(other.bitset)) {
      return false;
    }
    return size == other.size;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("ColumnCombinationBitset ");

    int lastSetBitIndex = bitset.prevSetBit(bitset.length());

    for (int i = 0; i <= lastSetBitIndex; i++) {
      stringBuilder.append(bitset.get(i) ? 1 : 0);
    }

    return stringBuilder.toString();
  }

  /**
   * Returns true if the potentialSuperset contains all columns of this column combination.
   *
   * @param potentialSuperset that this column could be a subset of
   * @return potentialSuperset is a super set
   */
  public boolean isSubsetOf(ColumnCombinationBitset potentialSuperset) {
    return potentialSuperset.containsSubset(this);
  }

  /**
   * Returns true iff the potentialProperSuperSet contains all columns of this column combination
   * and the potentialProperSuperSet is not equal to this column combination (e.g. proper
   * superset).
   *
   * @param potentialProperSuperSet that this column combination could be a proper subset of
   * @return potentialProperSuperSet is a real superset
   */
  public boolean isProperSubsetOf(ColumnCombinationBitset potentialProperSuperSet) {
    return (this.isSubsetOf(potentialProperSuperSet)) && (!this.equals(potentialProperSuperSet));
  }

  /**
   * Returns true iff the potentialSubset contains no columns that are not in this column
   * combination.
   *
   * @param potentialSubset that this column could be a superset of
   * @return potentialSubset is a sub set
   */
  public boolean containsSubset(ColumnCombinationBitset potentialSubset) {
    OpenBitSet ored = potentialSubset.bitset.clone();
    ored.or(bitset);
    return bitset.equals(ored);
  }

  /**
   * Returns true iff the potentialProperSubset contains no columns that are not in this column
   * combination and the potentialProperSubset is not equal to this column combination (e.g. real
   * subset).
   *
   * @param potentialProperSubset that this column could be a proper superset of
   * @return potentialProperSubset is a real subset
   */
  public boolean containsProperSubset(ColumnCombinationBitset potentialProperSubset) {
    return (this.containsSubset(potentialProperSubset) && (!this.equals(potentialProperSubset)));
  }

  /**
   * Returns all subset of the column combination (including the empty {@link
   * ColumnCombinationBitset}). The subsets include the original column combination (not proper
   * subsets).
   *
   * @return subsets
   */
  public List<ColumnCombinationBitset> getAllSubsets() {
    List<ColumnCombinationBitset> subsets = new LinkedList<>();

    Queue<ColumnCombinationBitset> currentLevel = new LinkedList<>();
    currentLevel.add(this);
    subsets.add(this);
    Set<ColumnCombinationBitset> nextLevel = new HashSet<>();
    for (int level = size(); level > 0; level--) {
      while (!currentLevel.isEmpty()) {
        ColumnCombinationBitset currentColumnCombination = currentLevel.remove();
        nextLevel.addAll(currentColumnCombination.getDirectSubsets());
      }
      currentLevel.addAll(nextLevel);
      subsets.addAll(nextLevel);
      nextLevel.clear();
    }

    return subsets;
  }

  /**
   * Returns all subset column combinations of size n.
   *
   * @param n cardinality of subsets
   * @return all subsets of the column combinations with n columns
   */
  public List<ColumnCombinationBitset> getNSubsetColumnCombinations(int n) {
    return getNSubsetColumnCombinationsSupersetOf(new ColumnCombinationBitset(), n);
  }

  /**
   * Returns all subset column combinations of size n that are superset of the subset (first
   * parameter).
   *
   * @param subSet that column combinations are superset of
   * @param n      cardinality of subsets
   * @return the n-subsets
   */
  public List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOf(
      ColumnCombinationBitset subSet, int n) {
    //If n is closer to the super (this column combination) set the top down strategy should be chosen
    //otherwise bottom up generation is used.
    // If n is closer to the super set go top down.
    if ((this.size() - n) < (n - subSet.size())) {
      return getNSubsetColumnCombinationsSupersetOfTopDown(subSet, n);
    } else {
      return getNSubsetColumnCombinationsSupersetOfBottomUp(subSet, n);
    }

  }

  /**
   * @param subSet that column combinations are superset of
   * @param n      cardinality of subsets
   * @return the n-subsets
   */
  protected List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOfBottomUp(
      ColumnCombinationBitset subSet, int n) {

    if ((n > this.size()) || (n < subSet.size())) {
      return new LinkedList<>();
    }

    List<ColumnCombinationBitset> currentLevel = new LinkedList<>();
    currentLevel.add(subSet);
    Set<ColumnCombinationBitset> nextLevel = new HashSet<>();

    for (int currentLevelIndex = subSet.size(); currentLevelIndex < n; currentLevelIndex++) {
      while (!currentLevel.isEmpty()) {
        ColumnCombinationBitset currentColumnCombination = currentLevel.remove(0);
        nextLevel.addAll(currentColumnCombination.getDirectSupersets(this));
      }
      currentLevel.addAll(nextLevel);
      nextLevel.clear();
    }

    return currentLevel;
  }

  /**
   * @param subSet that column combinations are superset of
   * @param n      cardinality of subsets
   * @return the n-subsets
   */
  protected List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOfTopDown(
      ColumnCombinationBitset subSet, int n) {

    if ((n > this.size()) || (n < subSet.size())) {
      return new LinkedList<>();
    }

    List<ColumnCombinationBitset> currentLevel = new LinkedList<>();
    currentLevel.add(this);
    Set<ColumnCombinationBitset> nextLevel = new HashSet<>();

    for (int currentLevelIndex = this.size(); currentLevelIndex > n; currentLevelIndex--) {
      while (!currentLevel.isEmpty()) {
        ColumnCombinationBitset currentColumnCombination = currentLevel.remove(0);
        for (ColumnCombinationBitset currentSubset : currentColumnCombination
            .getDirectSubsetsSupersetOf(subSet)) {
          if (currentSubset.containsSubset(subSet)) {
            nextLevel.add(currentSubset);
          }
        }
      }
      currentLevel.addAll(nextLevel);
      nextLevel.clear();
    }

    return currentLevel;
  }

  /**
   * Returns a list of all set column indices.
   *
   * @return the list of indices with set bits
   */
  public List<Integer> getSetBits() {
    List<Integer> setBits = new ArrayList<>(size());

    int setBitIndex = 0;
    while (true) {
      setBitIndex = bitset.nextSetBit(setBitIndex);

      if (setBitIndex == -1) {
        break;
      } else {
        setBits.add(setBitIndex);
      }

      setBitIndex++;
    }

    return setBits;
  }

  /**
   * Returns all the cleared bits within the maximum of all the number of columns.
   *
   * @param numberOfColumns the maximum number of columns
   * @return all the cleared bits in the number of columns
   */
  public List<Integer> getClearedBits(int numberOfColumns) {
    List<Integer> clearedBits = new LinkedList<>();

    for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
      if (!bitset.get(columnIndex)) {
        clearedBits.add(columnIndex);
      }
    }

    return clearedBits;
  }


  /**
   * Returns the difference between the two sets.
   *
   * @param otherColumnCombination column combination to be subtracted
   * @return The difference {@link ColumnCombinationBitset}
   */
  public ColumnCombinationBitset minus(
      ColumnCombinationBitset otherColumnCombination) {

    OpenBitSet temporaryBitset = bitset.clone();
    temporaryBitset.andNot(otherColumnCombination.bitset);

    return new ColumnCombinationBitset().setColumns(temporaryBitset);
  }

  /**
   * Returns the subset column combinations of size one.
   *
   * @return the contained 1 column combinations
   */
  public List<ColumnCombinationBitset> getContainedOneColumnCombinations() {
    List<ColumnCombinationBitset> oneColumnCombinations = new LinkedList<>();

    for (int columnIndex : getSetBits()) {
      oneColumnCombinations.add(new ColumnCombinationBitset(columnIndex));
    }

    return oneColumnCombinations;
  }

  /**
   * Union should return a {@link ColumnCombinationBitset} with all the columns from both column
   * combinations and no other columns. The original {@link ColumnCombinationBitset}s should remain
   * unchanged.
   *
   * @param other column combination to be unioned
   * @return the union of the two column combinations
   */
  public ColumnCombinationBitset union(ColumnCombinationBitset other) {
    OpenBitSet unionBitSet = bitset.clone();
    unionBitSet.or(other.bitset);
    return new ColumnCombinationBitset().setColumns(unionBitSet);
  }

  /**
   * Intersect should return a {@link ColumnCombinationBitset} with only the columns that are
   * contained in both combinations. The original {@link ColumnCombinationBitset}s should remain
   * unchanged.
   *
   * @param other column combination to be intersected
   * @return the intersection of the two column combinations
   */
  public ColumnCombinationBitset intersect(ColumnCombinationBitset other) {
    OpenBitSet intersectionBitSet = bitset.clone();
    intersectionBitSet.and(other.bitset);
    return new ColumnCombinationBitset().setColumns(intersectionBitSet);
  }

  /**
   * Generates the direct super sets. Supersets are bounded by the maximum number of columns.
   *
   * @param numberOfColumns maximum number of columns
   * @return the direct super sets
   */
  public List<ColumnCombinationBitset> getDirectSupersets(int numberOfColumns) {
    return getDirectSupersets(new ColumnCombinationBitset().setAllBits(numberOfColumns));
  }

  /**
   * Generates the direct super sets. Supersets are bounded by the maximum superset.
   *
   * @param maximalSuperset maximum superset column combination
   * @return the direct super sets
   */
  public List<ColumnCombinationBitset> getDirectSupersets(ColumnCombinationBitset maximalSuperset) {
    List<ColumnCombinationBitset> supersets = new ArrayList<>();

    ColumnCombinationBitset superset;
    for (int columnIndex : maximalSuperset.getSetBits()) {
      if (!bitset.get(columnIndex)) {
        superset = new ColumnCombinationBitset(this);
        superset.addColumn(columnIndex);
        supersets.add(superset);
      }
    }

    return supersets;
  }

  /**
   * Generates the direct subset column combinations.
   *
   * @return the direct sub sets
   */
  public List<ColumnCombinationBitset> getDirectSubsets() {
    return getDirectSubsetsSupersetOfFast(null);
  }

  /**
   * Generates the direct subset column combinations that are superset of the given sub set.
   *
   * @param subSet that all generated subsets are super set of
   * @return the direct sub sets super set of the sub set
   */
  public List<ColumnCombinationBitset> getDirectSubsetsSupersetOf(ColumnCombinationBitset subSet) {
    if (!containsProperSubset(subSet)) {
      return new ArrayList<>();
    }

    return getDirectSubsetsSupersetOfFast(subSet);
  }

  /**
   * Generates the direct subset column combinations that are superset of the given sub set. If the
   * subSet is null all direct subsets are returned.
   *
   * @param subSet that all generated subsets are super set of
   * @return the direct sub sets super set of the sub set
   */
  protected List<ColumnCombinationBitset> getDirectSubsetsSupersetOfFast(
      ColumnCombinationBitset subSet) {

    ColumnCombinationBitset columnsToRemove;
    if (subSet == null) {
      columnsToRemove = this;
    } else {
      columnsToRemove = this.minus(subSet);
    }

    List<ColumnCombinationBitset> subsets = new ArrayList<>(size());

    ColumnCombinationBitset generatedSubset;
    for (int columnIndex : columnsToRemove.getSetBits()) {
      generatedSubset = new ColumnCombinationBitset(this);
      generatedSubset.removeColumn(columnIndex);
      subsets.add(generatedSubset);
    }

    return subsets;
  }

  /**
   * Returns the number of columns in the combination.
   *
   * @return the number of columns in the combination.
   */
  public int size() {
    return (int) size;
  }

  /**
   * @return if the column combination is empty
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns the {@link ColumnCombination} with the correct name of the relation and the column
   * names.
   *
   * @param relationName the relation name
   * @param columnNames  the name of the columns
   * @return a {@link ColumnCombination}
   */
  public ColumnCombination createColumnCombination(String relationName,
                                                   ImmutableList<String> columnNames) {
    ColumnIdentifier[] identifierList = new ColumnIdentifier[size()];
    int i = 0;
    for (Integer columnIndex : getSetBits()) {
      identifierList[i] = new ColumnIdentifier(relationName, columnNames.get(columnIndex));
      i++;
    }

    return new ColumnCombination(identifierList);
  }

  /**
   * Resets all bits and sets all bits with indeces smaller than dimension. E.g. dimension 4
   * generate 111100000...
   *
   * @param dimension maximum number of columns
   * @return the {@link ColumnCombinationBitset}
   */
  public ColumnCombinationBitset setAllBits(int dimension) {
    size = 0;
    bitset = new OpenBitSet();
    for (int i = 0; i < dimension; i++) {
      addColumn(i);
    }

    return this;
  }

  /**
   * @param columnIndex index of bit to test
   * @return true iff the bit at columnIndex is set
   */
  public boolean containsColumn(int columnIndex) {
    return bitset.get(columnIndex);
  }
}

