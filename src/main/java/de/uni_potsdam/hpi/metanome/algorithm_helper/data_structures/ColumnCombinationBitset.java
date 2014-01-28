package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.util.OpenBitSet;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;

public class ColumnCombinationBitset {

	protected OpenBitSet bitset;
	protected long size = 0;
	
	public ColumnCombinationBitset() {
		bitset = new OpenBitSet();
	}
	
	/**
	 * Creates a copy of the current instance.
	 * 
	 * @param  a copy column combination
	 */
	public ColumnCombinationBitset(ColumnCombinationBitset columnCombination) {
		setColumns(columnCombination.bitset.clone());
	}

	/**
	 * TODO docs
	 * 
	 * @param containedColumns
	 * @return return the instance
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
	 * TODO docs
	 * 
	 * @param n
	 * @return all subsets of the column combinations with n columns
	 */
	public List<ColumnCombinationBitset> getNSubsetColumnCombinations(int n) {
		return getNSubsetColumnCombinationsSupersetOf(new ColumnCombinationBitset(), n);
	}
	
	/**
	 * TODO docs
	 * 
	 * @param subSet
	 * @param n
	 * @return
	 */
	public List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOf(ColumnCombinationBitset subSet, int n) {
		return getNSubsetColumnCombinationsSupersetOf(this, subSet, n);
	}

	/**
	 * If n is closer to the super set the top down strategy should be chosen otherwise bottom up generation is used.
	 * 
	 * @param superSet
	 * @param subSet
	 * @param n
	 * @return n column combinations that are super set of the sub set and sub set of the super set
	 */
	protected List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOf(
			ColumnCombinationBitset superSet, ColumnCombinationBitset subSet, int n) {
		// If n is closer to the super set go top down.
		if ((superSet.size() - n) < (n - subSet.size())) {
			return getNSubsetColumnCombinationsSupersetOfTopDown(superSet, subSet, n);
		} else {
			return getNSubsetColumnCombinationsSupersetOfBottomUp(superSet, subSet, n);
		}		
	}
	
	/**
	 * @param superSet
	 * @param subSet
	 * @param n
	 * @return
	 */
	protected List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOfBottomUp(
			ColumnCombinationBitset superSet, ColumnCombinationBitset subSet, int n) {
		
		List<ColumnCombinationBitset> nSupersets = new LinkedList<ColumnCombinationBitset>();
		
		if (subSet.size() > n) {
			return nSupersets;
		} else if (subSet.size() == n) {
			nSupersets.add(subSet);
			return nSupersets;
		}
		
		ColumnCombinationBitset nSuperset;
		for (int columnIndex : superSet.getSetBits()) {
			// The column is not contained in the subset.
			if (!subSet.bitset.get(columnIndex)) {
				nSuperset = new ColumnCombinationBitset().setColumns(subSet.bitset.clone());
				nSuperset.addColumn(columnIndex);
				nSupersets.add(nSuperset);
			}			
		}
		
		// The correct size n of subsets was reached.
		if (subSet.size() + 1 == n) {
			return nSupersets;
		}
		// Further supersets need to be generated recursively.
		else {
			Set<ColumnCombinationBitset> nCombinations = new  HashSet<ColumnCombinationBitset>();
			for (ColumnCombinationBitset nPlusOneCombination : nSupersets) {
				nCombinations.addAll(getNSubsetColumnCombinationsSupersetOfBottomUp(superSet, nPlusOneCombination, n));
			}
			List<ColumnCombinationBitset> nCombinationsList = new LinkedList<ColumnCombinationBitset>();
			nCombinationsList.addAll(nCombinations);
			return nCombinationsList;
		}
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
	 * @param superSet
	 * @param subSet
	 * @param n
	 * @return
	 */
	// TODO bottom up
	protected List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOfTopDown(
			ColumnCombinationBitset superSet, ColumnCombinationBitset subSet, int n) {

		List<ColumnCombinationBitset> nSubsets = new LinkedList<ColumnCombinationBitset>();
		
		// If n is actually the number of set bits in the superset (the unreal subset is wanted), return the superSet.
		if (superSet.size() < n) {
			return nSubsets;
		} else if (superSet.size() == n) {
			nSubsets.add(superSet);
			return nSubsets;
		}
		
		int setBitIndex = 0;
		ColumnCombinationBitset nSubset;
		OpenBitSet nSubsetBitSet;
		while (true) {
			setBitIndex = superSet.bitset.nextSetBit(setBitIndex);
			
			if (setBitIndex == -1) {
				break;
			} else {
				nSubset = new ColumnCombinationBitset();
				nSubsetBitSet = superSet.bitset.clone();
				nSubsetBitSet.clear(setBitIndex);
				nSubset.setColumns(nSubsetBitSet);
				if (subSet.isSubsetOf(nSubset)) {
					nSubsets.add(nSubset);
				}
			}
			
			setBitIndex++;
		}
		
		// The correct size n of subsets was reached.
		if (superSet.size() - 1 == n) {
			return nSubsets;
		}
		// Further subsets need to be generated recursively.
		else {
			Set<ColumnCombinationBitset> nCombinations = new HashSet<ColumnCombinationBitset>();
			for (ColumnCombinationBitset nMinusOneCombination : nSubsets) {
				nCombinations.addAll(getNSubsetColumnCombinationsSupersetOfTopDown(nMinusOneCombination, subSet, n));
			}
			List<ColumnCombinationBitset> nCombinationsList = new LinkedList<ColumnCombinationBitset>();
			nCombinationsList.addAll(nCombinations);
			return nCombinationsList;
		}
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
			oneColumnCombinations.add(new ColumnCombinationBitset().setColumns(columnIndex));
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
		List<ColumnCombinationBitset> supersets = new ArrayList<ColumnCombinationBitset>();
		
		ColumnCombinationBitset superset;
		for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
			// column is not in bitset
			if (!bitset.get(columnIndex)) {
				superset = new ColumnCombinationBitset().setColumns(bitset.clone());
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

