package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.util.OpenBitSet;

public class ColumnCombinationBitset {

	protected OpenBitSet bitset = new OpenBitSet();;
	
	/**
	 * TODO docs
	 * 
	 * @param containedColumns
	 * @return return the instance
	 */
	public ColumnCombinationBitset setColumns(int... containedColumns) {
		// TODO optimisation implement clear
		bitset = new OpenBitSet();
		for (int columnIndex : containedColumns) {
			bitset.set(columnIndex);
		}
		
		return this;		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bitset == null) ? 0 : bitset.hashCode());
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
	 * TODO docs
	 * 
	 * @param bitset
	 * @return the instance
	 */
	public ColumnCombinationBitset setColumns(OpenBitSet bitset) {
		this.bitset = bitset;
		return this;
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
	 * @param superSet
	 * @param subSet
	 * @param n
	 * @return
	 */
	// TODO bottom up
	protected List<ColumnCombinationBitset> getNSubsetColumnCombinationsSupersetOf(
			ColumnCombinationBitset superSet, ColumnCombinationBitset subSet, int n) {

		List<ColumnCombinationBitset> nSubsets = new LinkedList<ColumnCombinationBitset>();
		
		int setBitIndex = 0;
		int numberOfSetBits = 0;
		ColumnCombinationBitset nSubset;
		while (true) {
			setBitIndex = superSet.bitset.nextSetBit(setBitIndex);
			
			if (setBitIndex == -1) {
				break;
			} else {
				numberOfSetBits++;
				nSubset = new ColumnCombinationBitset();
				nSubset.setColumns(superSet.bitset.clone());
				nSubset.bitset.clear(setBitIndex);
				if (subSet.isSubsetOf(nSubset)) {
					nSubsets.add(nSubset);
				}
			}
			
			setBitIndex++;
		}
		
		// If n is actually the number of set bits in the superset (the unreal subset is wanted), return the superSet.
		if (numberOfSetBits == n) {
			nSubsets.clear();
			nSubsets.add(superSet);
			return nSubsets;
		}
		// The correct size n of subsets was reached.
		if (numberOfSetBits - 1 == n) {
			return nSubsets;
		} 
		// Further subsets need to be generated recursively.
		else {
			Set<ColumnCombinationBitset> nCombinations = new HashSet<ColumnCombinationBitset>();
			for (ColumnCombinationBitset nPlusOneCombination : nSubsets) {
				nCombinations.addAll(getNSubsetColumnCombinationsSupersetOf(nPlusOneCombination, subSet, n));
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

	public List<ColumnCombinationBitset> getSetBits() {
		// TODO Auto-generated method stub
		return null;
	}

	public ColumnCombinationBitset union(ColumnCombinationBitset a) {
		// TODO Auto-generated method stub
		return null;
	}

	public void or(ColumnCombinationBitset ored) {
		// TODO Auto-generated method stub
		
	}
}

