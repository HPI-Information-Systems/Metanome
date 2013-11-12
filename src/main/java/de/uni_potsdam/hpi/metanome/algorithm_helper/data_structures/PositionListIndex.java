package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PositionListIndex {

	protected List<LongOpenHashSet> clusters;

	public PositionListIndex(List<LongOpenHashSet> clusters) {
		this.clusters = clusters;
	}

	/**
	 * @param otherPLI
	 * @return {@link PositionListIndex}
	 * 
	 *         Intersects the given PositionListIndex with this
	 *         PositionListIndex returning a new PositionListIndex. For the
	 *         intersection the smaller PositionListIndex is converted into a
	 *         HashMap.
	 */
	public PositionListIndex intersect(PositionListIndex otherPLI) {
		//TODO Optimize Smaller PLI as Hashmap?
		return calculateIntersection(otherPLI);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		Collections.sort(clusters, new Comparator<LongOpenHashSet>() {

	        @Override
	        public int compare(LongOpenHashSet o1, LongOpenHashSet o2) {
	            return o1.hashCode() - o2.hashCode();
	        }
	    });
		result = prime * result
				+ ((clusters == null) ? 0 : clusters.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PositionListIndex other = (PositionListIndex) obj;
		if (clusters == null) {
			if (other.clusters != null)
				return false;
		} else {
			for (LongOpenHashSet cluster : clusters) {
				if (!other.clusters.contains(cluster)) {
					return false;
				}
			}
			for (LongOpenHashSet cluster : other.clusters) {
				if (!clusters.contains(cluster)) {
					return false;
				}
			}
		}
			
		return true;
	}

	/**
	 * @param otherPLI
	 * @return {@link PositionListIndex}
	 * 
	 *         Intersects the two given {@link PositionListIndex} and returns
	 *         the outcome as new PositionListIndex.
	 */
	protected PositionListIndex calculateIntersection(PositionListIndex otherPLI) {
		Long2LongOpenHashMap hashedPLI = this.asHashMap();
		Map<LongPair, LongOpenHashSet> map = new HashMap<LongPair, LongOpenHashSet>();
		buildMap(otherPLI, hashedPLI, map);
		
		List<LongOpenHashSet> clusters = new ArrayList<LongOpenHashSet>();
		for (LongOpenHashSet cluster : map.values()) {
			if (cluster.size() < 2) {
				continue;
			} 
			clusters.add(cluster);
		}
		return new PositionListIndex(clusters);
	}

	protected void buildMap(PositionListIndex otherPLI, Long2LongOpenHashMap hashedPLI, Map<LongPair, LongOpenHashSet> map) {
		long uniqueValueCount = 0;
		for (LongOpenHashSet sameValues : otherPLI.clusters) {
			for (long rowCount : sameValues) {
				if (hashedPLI.containsKey(rowCount)) {
					LongPair pair = new LongPair(uniqueValueCount, hashedPLI.get(rowCount));
					updateMap(map, rowCount, pair);
				}
			}
			uniqueValueCount++;
		}
	}
	
	protected void updateMap(Map<LongPair, LongOpenHashSet> map, long rowCount, LongPair pair) {
		if (map.containsKey(pair)) {
			LongOpenHashSet currentList = map.get(pair);
			currentList.add(rowCount);
		} else {
			LongOpenHashSet newList = new LongOpenHashSet();
			newList.add(rowCount);
			map.put(pair, newList);
		}
	}

	/**
	 * TODO fix docs
	 * 
	 * @return {@link Long2LongOpenHashMap} Creates
	 */
	public Long2LongOpenHashMap asHashMap() {
		Long2LongOpenHashMap hashedPLI = new Long2LongOpenHashMap();
		long uniqueValueCount = 0;
		for (LongOpenHashSet sameValues : clusters) {
			for (long rowIndex : sameValues) {
				hashedPLI.put(rowIndex, uniqueValueCount);
			}
			uniqueValueCount++;
		}
		return hashedPLI;
	}

	/**
	 * Returns the number of non unary clusters.
	 * 
	 * @return the number of clusters in the {@link PositionListIndex}
	 */
	public long size() {
		return clusters.size();
	}

	/**
	 * @return the {@link PositionListIndex} contains only unary clusters.
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	/**
	 * @return the column represented by the {@link PositionListIndex} is unique.
	 */
	public boolean isUnique() {
		return isEmpty();
	}

}
