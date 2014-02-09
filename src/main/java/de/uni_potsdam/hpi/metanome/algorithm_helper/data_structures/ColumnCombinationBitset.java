package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import com.google.common.collect.ImmutableList;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import org.apache.lucene.util.OpenBitSet;

import java.util.*;

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
	 * @param columnCombination
	 */
	public ColumnCombinationBitset(ColumnCombinationBitset columnCombination) {
		setColumns(columnCombination.bitset.clone());
	}

	/**
	 * TODO docs
	 * 
	 * @param containedColumns
	 * @return return the instance
     *
     * @deprecated
	 */
	public ColumnCombinationBitset setColumns(int... containedColumns) {
		// TODO optimisation implement clear
		bitset = new OpenBitSet();
		// FIXME size not reset
		for (int columnIndex : containedColumns) {
			// If the bit was not yet set, increase the size.
			addColumn(columnIndex);
		}
		
		return this;
	}
	
	/**
	 * TODO docs
	 * 
	 * @param bitset
	 * @return the instance
	 */
	public ColumnCombinationBitset setColumns(OpenBitSet bitset) {
		this.bitset = bitset;
		size = bitset.cardinality();
		
		return this;
	} 
	
	/**
	 * Adds a column to the bit set.
	 * 
	 * @param columnIndex
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
	 * @param columnIndex
	 */
	public void removeColumn(int columnIndex) {
		if (bitset.get(columnIndex)) {
			size--;
		}
		
		bitset.clear(columnIndex);
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ColumnCombinationBitset other = (ColumnCombinationBitset) obj;
		if (bitset == null) {
			if (other.bitset != null)
				return false;
		} else if (!bitset.equals(other.bitset))
			return false;
		if (size != other.size)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("ColumnCombinationBitset ");
		
		int lastSetBitIndex = bitset.prevSetBit(bitset.length());
		
		for (int i = 0; i <= lastSetBitIndex; i++) {
			stringBuilder.append(bitset.get(i)? 1 : 0);
		}		
		
		return stringBuilder.toString();
	}

	/**
	 * Returns true if the potentialSuperset contains all columns of this column combination.
	 * 
	 * @param potentialSuperset
	 * @return potentialSuperset is a super set
	 */
	public boolean isSubsetOf(ColumnCombinationBitset potentialSuperset) {
		return potentialSuperset.containsSubset(this);
	}
	
	/**
	 * Returns true iff the potentialRealSuperSet contains all columns of this column combination
	 * and the potentialRealSuperSet is not equal to this column combination (e.g. real superset).
	 * 
	 * @param potentialRealSuperSet
	 * @return potentialRealSuperSet is a real superset
	 */
	public boolean isRealSubsetOf(ColumnCombinationBitset potentialRealSuperSet) {
		return (this.isSubsetOf(potentialRealSuperSet)) && (!this.equals(potentialRealSuperSet));
	}

	/**
	 * Returns true iff the potentialSubset contains no columns that are not in this column combination.
	 * 
	 * @param potentialSubset
	 * @return potentialSubset is a sub set
	 */
	public boolean containsSubset(ColumnCombinationBitset potentialSubset) {
		OpenBitSet ored = potentialSubset.bitset.clone();
		ored.or(bitset);
		return bitset.equals(ored);
	}

	/**
	 * Returns true iff the potentialRealSubset contains no columns that are not in this column combination
	 * and the potentialRealSubset is not equal to this column combination (e.g. real subset).
	 * 
	 * @param potentialRealSubset
	 * @return potentialRealSubset is a real subset
	 */
	public boolean containsRealSubset(ColumnCombinationBitset potentialRealSubset) {
		return (this.containsSubset(potentialRealSubset) && (!this.equals(potentialRealSubset)));
	}
	
	/**
	 * Returns all subset of the column combination (including the empty {@link ColumnCombinationBitset}).
	 * 
	 * @return subsets
	 */
	public List<ColumnCombinationBitset> getAllSubsets() {
		List<ColumnCombinationBitset> subsets = new LinkedList<ColumnCombinationBitset>();
		
		Queue<ColumnCombinationBitset> currentLevel = new LinkedList<ColumnCombinationBitset>();
		currentLevel.add(this);
		Set<ColumnCombinationBitset> nextLevel = new HashSet<ColumnCombinationBitset>();
		for (int level = size(); level > 0; level--) {
			while(!currentLevel.isEmpty()) {
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
	 * TODO docs
	 * 
	 * @param n
	 * @return all subsets of the column combinations with n columns
	 */
	public List<ColumnCombinationBitset> getNSubsetColumnCombinations(int n) {
		return getNSubsetColumnCombinationsSupersetOf(new ColumnCombinationBitset(), n);
	}
	
	/**
	 * If n is closer to the super (this column combination) set the top down strategy should be chosen
	 * otherwise bottom up generation is used.
	 * 
	 * @param subSet
	 * @param n
	 * @return
	 */
	public List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOf(ColumnCombinationBitset subSet, int n) {
		// If n is closer to the super set go top down.
		if ((this.size() - n) < (n - subSet.size())) {
			return getNSubsetColumnCombinationsSupersetOfTopDown(subSet, n);
		} else {
			return getNSubsetColumnCombinationsSupersetOfBottomUp(subSet, n);
		}	
		
	}
	
	/**
	 * @param subSet
	 * @param n
	 * @return
	 */
	protected List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOfBottomUp(ColumnCombinationBitset subSet, int n) {
		
		if ((n > this.size()) || (n < subSet.size())) {
			return new LinkedList<ColumnCombinationBitset>();
		}
		
		List<ColumnCombinationBitset> currentLevel = new LinkedList<ColumnCombinationBitset>();
		currentLevel.add(subSet);
		Set<ColumnCombinationBitset> nextLevel = new HashSet<ColumnCombinationBitset>();
		
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
	
	/**TODO docs
	 * @return
	 */
	public List<Integer> getSetBits() {
		//FIXME use array list here
		List<Integer> setBits = new LinkedList<Integer>();
		
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
	 * @return all the cleared bits in the number of columns
	 */
	public List<Integer> getClearedBits(int numberOfColumns) {
		List<Integer> clearedBits = new LinkedList<Integer>();
		
		for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
			if (!bitset.get(columnIndex)) {
				clearedBits.add(columnIndex);
			}
		}
		
		return clearedBits;
	}
	
	/**
	 * @param subSet
	 * @param n
	 * @return
	 */
	protected List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOfTopDown(ColumnCombinationBitset subSet, int n) {
		
		if ((n > this.size()) || (n < subSet.size())) {
			return new LinkedList<ColumnCombinationBitset>();
		}
		
		List<ColumnCombinationBitset> currentLevel = new LinkedList<ColumnCombinationBitset>();
		currentLevel.add(this);
		Set<ColumnCombinationBitset> nextLevel = new HashSet<ColumnCombinationBitset>();
		
		for (int currentLevelIndex = this.size(); currentLevelIndex > n; currentLevelIndex--) {
			while(!currentLevel.isEmpty()) {
				ColumnCombinationBitset currentColumnCombination = currentLevel.remove(0);
				// TODO optimize only generate subsets superset of
				for (ColumnCombinationBitset currentSubset : currentColumnCombination.getDirectSubsets()) {
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
	 * Returns the difference between the two sets.
	 * 
	 * @param otherColumnCombination
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
		List<ColumnCombinationBitset> oneColumnCombinations = new LinkedList<ColumnCombinationBitset>();
		
		for (int columnIndex : getSetBits()) {
			oneColumnCombinations.add(new ColumnCombinationBitset(columnIndex));
		}
		
		return oneColumnCombinations;
	}

	/**
	 * Union should return a {@link ColumnCombinationBitset} with all the columns from both column combinations
	 * and no other columns. The original {@link ColumnCombinationBitset}s should remain unchanged.
	 * 
	 * @param other
	 * @return the union of the two column combinations
	 */
	public ColumnCombinationBitset union(ColumnCombinationBitset other) {
		OpenBitSet unionBitSet = bitset.clone();
		unionBitSet.or(other.bitset);
		return new ColumnCombinationBitset().setColumns(unionBitSet);
	}
	
	/**
	 * Intersect should return a {@link ColumnCombinationBitset} with only the columns that are contained in both
	 * combinations. The original {@link ColumnCombinationBitset}s should remain unchanged. 
	 * 
	 * @param other
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
	 * @param numberOfColumns
	 * @return the direct super sets
	 */
	public List<ColumnCombinationBitset> getDirectSupersets(int numberOfColumns) {
		return getDirectSupersets(new ColumnCombinationBitset().setAllBits(numberOfColumns));
	}
	
	/**
	 * Generates the direct super sets. Supersets are bounded by the maximum superset.
	 * 
	 * @param maximalSuperset
	 * @return the direct super sets
	 */
	public List<ColumnCombinationBitset> getDirectSupersets(ColumnCombinationBitset maximalSuperset) {
		List<ColumnCombinationBitset> supersets = new ArrayList<ColumnCombinationBitset>();
		
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
		List<ColumnCombinationBitset> subsets = new ArrayList<ColumnCombinationBitset>();
		
		ColumnCombinationBitset subset;
		for (int columnIndex : getSetBits()) {
			subset = new ColumnCombinationBitset().setColumns(bitset.clone());
			subset.removeColumn(columnIndex);
			subsets.add(subset);
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
	 * Returns the {@link ColumnCombination} with the correct name of the relation and the column names.
	 * 
	 * @return a {@link ColumnCombination}
	 */
	public ColumnCombination createColumnCombination(String relationName, ImmutableList<String> columnNames) {
		ColumnIdentifier[] identifierList = new ColumnIdentifier[size()];
		int i = 0;
		for (Integer columnIndex : getSetBits()) {
			identifierList[i] = new ColumnIdentifier(relationName, columnNames.get(columnIndex));
			i++;
		}
		
		return new ColumnCombination(identifierList);
	}

	/**
	 * Resets all bits and sets all bits with indeces smaller than dimension.
	 * E.g. dimension 4 generate 111100000...
	 * 
	 * @param dimension
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
	 * @param bitIndex
	 * @return true iff the bit at bitIndex is set
	 */
	public boolean testBit(int bitIndex) {
		return bitset.get(bitIndex);
	}
}

