package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;


public class PLIBuilder {
	
	protected List<HashMap<String, LongSet>> columns = new ArrayList<HashMap<String, LongSet>>();
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
			columns.add(new HashMap<String, LongSet>());
		}
		if (columns.get(columnCount).containsKey(attributeCell)) {
			columns.get(columnCount).get(attributeCell).add(rowCount);
		} else {
			LongSet newList = new LongAVLTreeSet();
			newList.add(rowCount);
			columns.get(columnCount).put(attributeCell, newList);
		}
	}

	protected List<PositionListIndex> purgePLIEntries() {
		List<PositionListIndex> pliList = new ArrayList<PositionListIndex>();
		Iterator<HashMap<String, LongSet>> columnsIterator = columns.iterator();
		while(columnsIterator.hasNext()) {
			List<LongSet> clusters = new ArrayList<LongSet>();
			for (LongSet cluster : columnsIterator.next().values()) {
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
