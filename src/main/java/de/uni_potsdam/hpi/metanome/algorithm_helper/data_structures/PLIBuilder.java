package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;


public class PLIBuilder {
	
	protected List<HashMap<String, LongOpenHashSet>> columns = new ArrayList<HashMap<String, LongOpenHashSet>>();
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
		calculateRawPLI();
		List<PositionListIndex> pliList = purgePLIEntries();
		return pliList;
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
			columns.add(new HashMap<String, LongOpenHashSet>());
		}
		if (columns.get(columnCount).containsKey(attributeCell)) {
			columns.get(columnCount).get(attributeCell).add(rowCount);
		} else {
			LongOpenHashSet newList = new LongOpenHashSet();
			newList.add(rowCount);
			columns.get(columnCount).put(attributeCell, newList);
		}
	}

	protected List<PositionListIndex> purgePLIEntries() {
		List<PositionListIndex> pliList = new ArrayList<PositionListIndex>();
		for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
			List<LongOpenHashSet> clusters = new ArrayList<LongOpenHashSet>();
			for (LongOpenHashSet cluster : columns.get(columnIndex).values()) {
				if (cluster.size() < 2) {
					continue;
				}
				clusters.add(cluster);
			}
			pliList.add(new PositionListIndex(clusters));
		}
		return pliList;
	}
}
