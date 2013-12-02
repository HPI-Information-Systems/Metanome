package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;

public class PLIBuilderFixture {
	public List<ArrayList<String>> actualColumns = new LinkedList<ArrayList<String>>();
	
	PLIBuilderFixture() {
		ArrayList<String> column1 = new ArrayList<String>();
		column1.add("1");
		column1.add("2");
		column1.add("3");
		column1.add("4");
		column1.add("5");
		actualColumns.add(column1);
		ArrayList<String> column2 = new ArrayList<String>();
		column2.add("1");
		column2.add("1");
		column2.add("1");
		column2.add("1");
		column2.add("1");
		actualColumns.add(column2);
		ArrayList<String> column3 = new ArrayList<String>();
		column3.add("5");
		column3.add("5");
		column3.add("3");
		column3.add("3");
		column3.add("5");
		actualColumns.add(column3);
		ArrayList<String> column4 = new ArrayList<String>();
		column4.add("");
		column4.add("2");
		column4.add("");
		column4.add("4");
		column4.add("5");
		actualColumns.add(column4);
	}
	
	public RelationalInput getSimpleRelationalInput() throws InputIterationException {
		RelationalInput simpleRelationalInput = mock(RelationalInput.class);
		
		when(simpleRelationalInput.next())
			.thenReturn(ImmutableList.of(actualColumns.get(0).get(0), actualColumns.get(1).get(0), actualColumns.get(2).get(0), actualColumns.get(3).get(0)))
			.thenReturn(ImmutableList.of(actualColumns.get(0).get(1), actualColumns.get(1).get(1), actualColumns.get(2).get(1), actualColumns.get(3).get(1)))
			.thenReturn(ImmutableList.of(actualColumns.get(0).get(2), actualColumns.get(1).get(2), actualColumns.get(2).get(2), actualColumns.get(3).get(2)))
			.thenReturn(ImmutableList.of(actualColumns.get(0).get(3), actualColumns.get(1).get(3), actualColumns.get(2).get(3), actualColumns.get(3).get(3)))
			.thenReturn(ImmutableList.of(actualColumns.get(0).get(4), actualColumns.get(1).get(4), actualColumns.get(2).get(4), actualColumns.get(3).get(4)));
		
		when(simpleRelationalInput.hasNext())
			.thenReturn(true)
			.thenReturn(true)
			.thenReturn(true)
			.thenReturn(true)
			.thenReturn(true)
			.thenReturn(false);
		
		return simpleRelationalInput;
	}

	public List<PositionListIndex> getExpectedPLIList() {
		List<PositionListIndex> expectedPLIList = new LinkedList<PositionListIndex>();
		List<LongSet> list1 = new LinkedList<LongSet>();
		PositionListIndex PLI1 = new PositionListIndex(list1);
		expectedPLIList.add(PLI1);
		
		List<LongSet> list2 = new LinkedList<LongSet>();
		LongAVLTreeSet arrayList21 = new LongAVLTreeSet();
		arrayList21.add(0);
		arrayList21.add(1);
		arrayList21.add(2);
		arrayList21.add(3);
		arrayList21.add(4);
		list2.add(arrayList21);
		PositionListIndex PLI2 = new PositionListIndex(list2);
		expectedPLIList.add(PLI2);
		
		List<LongSet> list3 = new LinkedList<LongSet>();
		LongAVLTreeSet arrayList31 = new LongAVLTreeSet();
		LongAVLTreeSet arrayList32 = new LongAVLTreeSet();
		
		arrayList31.add(0);
		arrayList31.add(1);
		arrayList31.add(4);
		
		arrayList32.add(2);
		arrayList32.add(3);
		
		list3.add(arrayList31);
		list3.add(arrayList32);
		PositionListIndex PLI3 = new PositionListIndex(list3);
		expectedPLIList.add(PLI3);
		
		List<LongSet> list4 = new LinkedList<LongSet>();
		LongAVLTreeSet arrayList41 = new LongAVLTreeSet();
		
		arrayList41.add(0);
		arrayList41.add(2);
		
		list4.add(arrayList41);
		PositionListIndex PLI4 = new PositionListIndex(list4);
		expectedPLIList.add(PLI4);
		
		return expectedPLIList;
	}
}
