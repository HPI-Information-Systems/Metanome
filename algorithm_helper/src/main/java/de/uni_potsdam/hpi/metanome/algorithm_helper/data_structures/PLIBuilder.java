package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import com.google.common.collect.ImmutableList;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;
import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.*;


public class PLIBuilder {
	
	protected List<HashMap<String, LongArrayList>> columns = null;
	protected RelationalInput input;
	
	public PLIBuilder(RelationalInput input) {
		this.input = input;
	}
	
	/**
	 * TODO docs
	 * 
	 * @return list of plis for all columns
	 * @throws InputIterationException
	 */
	public List<PositionListIndex> getPLIList() throws InputIterationException {
		if (columns == null) {
			columns = new ArrayList<HashMap<String, LongArrayList>>();
			calculateRawPLI();
		}

        return purgePLIEntries();
	}
	
	/**
	 * TODO docs
	 * 
	 * @return
	 * 
	 * @throws InputIterationException 
	 */
	public List<TreeSet<String>> getDistinctSortedColumns() throws InputIterationException {
		if (columns == null) {
			columns = new ArrayList<HashMap<String, LongArrayList>>();
			calculateRawPLI();
		}
		
		List<TreeSet<String>> distinctSortedColumns = new LinkedList<TreeSet<String>>();
		
		for (HashMap<String, LongArrayList> columnMap : columns) {
			distinctSortedColumns.add(new TreeSet<String>(columnMap.keySet()));
		}
		
		return distinctSortedColumns;
	}
	
	protected void calculateRawPLI() throws InputIterationException {
		long rowCount = 0;
		while(input.hasNext()) {
			ImmutableList<String> row = input.next();
			int columnCount = 0;
			for(String cellValue : row) {
				addValue(rowCount, columnCount, cellValue);
				columnCount++;
			}
			rowCount++;
		}
	}

	protected void addValue(long rowCount, int columnCount, String attributeCell) {
		if (columns.size() <= columnCount) {
			columns.add(new HashMap<String, LongArrayList>());
		}
		if (columns.get(columnCount).containsKey(attributeCell)) {
			columns.get(columnCount).get(attributeCell).add(rowCount);
		} else {
			LongArrayList newList = new LongArrayList();
			newList.add(rowCount);
			columns.get(columnCount).put(attributeCell, newList);
		}
	}

	protected List<PositionListIndex> purgePLIEntries() {
		List<PositionListIndex> pliList = new ArrayList<PositionListIndex>();
		Iterator<HashMap<String, LongArrayList>> columnsIterator = columns.iterator();
		while(columnsIterator.hasNext()) {
			List<LongArrayList> clusters = new ArrayList<LongArrayList>();
			for (LongArrayList cluster : columnsIterator.next().values()) {
				if (cluster.size() < 2) {
					continue;
				}
				clusters.add(cluster);
			}
			pliList.add(new PositionListIndex(clusters));
			// Free value Maps.
			columnsIterator.remove();
		}
		return pliList;
	}
}
