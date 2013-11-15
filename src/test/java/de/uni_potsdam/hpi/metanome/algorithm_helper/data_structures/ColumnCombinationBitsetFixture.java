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
	
	public int getExpectedSize() {
		return getSetBitList().size();
	}
}
