package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import java.util.LinkedList;
import java.util.List;

public class ColumnCombinationBitsetFixture {

	public ColumnCombinationBitset getTestData() {
		ColumnCombinationBitset columnCombination = new ColumnCombinationBitset();
		
		for (int columnIndex : getSetBitList()) {
			columnCombination.addColumn(columnIndex);
		}
		
		return columnCombination;
	}
	
	public Integer[] getExpectedBits() {
		List<Integer> setBitList = getSetBitList();	
		
		return getSetBitList().toArray(new Integer[setBitList.size()]);
	}
	
	protected List<Integer> getSetBitList() {
		List<Integer> setBits = new LinkedList<Integer>();
		
		setBits.add(0);
		setBits.add(2);
		setBits.add(3);
		setBits.add(4);
		
		return setBits;
	}
	
	public ColumnCombinationBitset[] getExpectedOneColumnCombinations() {
		List<ColumnCombinationBitset> oneColumnCombinations = new LinkedList<ColumnCombinationBitset>();
		
		for (int columnIndex : getSetBitList()) {
			oneColumnCombinations.add(new ColumnCombinationBitset().setColumns(columnIndex));
		}
		
		return oneColumnCombinations.toArray(new ColumnCombinationBitset[oneColumnCombinations.size()]);
	}
	
	public int getMaxNumberOfColumns() {
		return 7;
	}
	
	public ColumnCombinationBitset[] getExpectedDirectSupersets() {
		ColumnCombinationBitset[] directSupersets = {
				new ColumnCombinationBitset().setColumns(0, 2, 3, 4, 1),
				new ColumnCombinationBitset().setColumns(0, 2, 3, 4, 5),
				new ColumnCombinationBitset().setColumns(0, 2, 3, 4, 6)};
		
		return directSupersets;
	}
	
	public ColumnCombinationBitset[] getExpectedDirectSubsets() {
		ColumnCombinationBitset[] directSubsets = {
				new ColumnCombinationBitset().setColumns(2, 3, 4),
				new ColumnCombinationBitset().setColumns(0, 3, 4),
				new ColumnCombinationBitset().setColumns(0, 2, 4),
				new ColumnCombinationBitset().setColumns(0, 2, 3)};
		
		
		return directSubsets;
	}
	
	public int getExpectedSize() {
		return getSetBitList().size();
	}
}
