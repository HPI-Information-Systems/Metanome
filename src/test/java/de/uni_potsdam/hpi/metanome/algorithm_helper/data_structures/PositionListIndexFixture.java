package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import java.util.ArrayList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PositionListIndex;

public class PositionListIndexFixture {

	PositionListIndex getFirstPLI() {
		List<LongOpenHashSet> clusters = new ArrayList<LongOpenHashSet>();
		
		long[] cluster1 = {2, 4, 8};
		clusters.add(new LongOpenHashSet(cluster1));
		long[] cluster2 = {5, 6, 7};
		clusters.add(new LongOpenHashSet(cluster2));
		
		return new PositionListIndex(clusters);
	}
	
	PositionListIndex getPermutatedFirstPLI() {
		List<LongOpenHashSet> clusters = new ArrayList<LongOpenHashSet>();
		
		long[] cluster1 = {7, 6, 5};
		clusters.add(new LongOpenHashSet(cluster1));
		long[] cluster2 = {4, 2, 2, 8};
		clusters.add(new LongOpenHashSet(cluster2));
		
		return new PositionListIndex(clusters);
	}
	
	PositionListIndex getSupersetOfFirstPLI() {
		List<LongOpenHashSet> clusters = new ArrayList<LongOpenHashSet>();
		
		long[] cluster1 = {7, 6, 5};
		clusters.add(new LongOpenHashSet(cluster1));
		long[] cluster2 = {4, 2, 2, 8};
		clusters.add(new LongOpenHashSet(cluster2));
		long[] cluster3 = {10, 11};
		clusters.add(new LongOpenHashSet(cluster3));
		
		return new PositionListIndex(clusters);
	}
	
	Long2LongOpenHashMap getFirstPLIAsHashMap() {
		Long2LongOpenHashMap pliMap = new Long2LongOpenHashMap();
		
		pliMap.addTo(5, 1);
		pliMap.addTo(7, 1);
		pliMap.addTo(6, 1);
		
		pliMap.addTo(2, 0);
		pliMap.addTo(4, 0);
		pliMap.addTo(8, 0);
		
		return pliMap;
	}
	
	PositionListIndex getSecondPLI() {
		List<LongOpenHashSet> clusters = new ArrayList<LongOpenHashSet>();
		
		long[] cluster1 = {1, 2, 5, 8};
		clusters.add(new LongOpenHashSet(cluster1));
		long[] cluster2 = {4, 6, 7};
		clusters.add(new LongOpenHashSet(cluster2));
		
		return new PositionListIndex(clusters);
	}
	
	PositionListIndex getExpectedIntersectedPLI() {
		List<LongOpenHashSet> clusters = new ArrayList<LongOpenHashSet>();
		
		long[] cluster1 = {2, 8};
		clusters.add(new LongOpenHashSet(cluster1));
		long[] cluster2 = {6, 7};
		clusters.add(new LongOpenHashSet(cluster2));
		
		return new PositionListIndex(clusters);
	}
	
}
