package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PositionListIndexTest {

	protected PositionListIndexFixture fixture;
	
	@Before
	public void setUp() throws Exception {
		fixture = new PositionListIndexFixture();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Two {@link PositionListIndex} should be correctly intersected.
	 */
	@Test
	public void testIntersect() {
		// Setup
		PositionListIndex firstPLI = fixture.getFirstPLI();
		PositionListIndex secondPLI = fixture.getSecondPLI();
		// Expected values
		PositionListIndex expectedPLI = fixture.getExpectedIntersectedPLI();
		
		// Execute functionality
		PositionListIndex actualIntersectedPLI = firstPLI.intersect(secondPLI);
		
		// Check result
		assertEquals(expectedPLI, actualIntersectedPLI);
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testHashCode() {
		// Setup
		PositionListIndex firstPLI = fixture.getFirstPLI();
		PositionListIndex permutatedfirstPLI = fixture.getPermutatedFirstPLI();
		
		// Execute functionality
		// Check result
		assertEquals(firstPLI, firstPLI);
		assertEquals(firstPLI.hashCode(), firstPLI.hashCode());
		assertEquals(firstPLI, permutatedfirstPLI);
		assertEquals(firstPLI.hashCode(), permutatedfirstPLI.hashCode());		
	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testEquals() {
		// Setup
		PositionListIndex firstPLI = fixture.getFirstPLI();
		PositionListIndex permutatedfirstPLI = fixture.getPermutatedFirstPLI();
		PositionListIndex secondPLI = fixture.getSecondPLI();
		PositionListIndex supersetOfFirstPLI = fixture.getSupersetOfFirstPLI();
		
		// Execute functionality
		// Check result
		assertEquals(firstPLI, firstPLI);
		assertEquals(firstPLI, permutatedfirstPLI);
		assertNotEquals(firstPLI, secondPLI);
		assertNotEquals(firstPLI, supersetOfFirstPLI);
	}
	
	/**
	 * A {@link PositionListIndex} should return a valid and correct HashMap.
	 */
	@Test
	public void testAsHashMap() {
		// Setup
		PositionListIndex firstPLI = fixture.getFirstPLI();
		
		//expected Values
		Long2LongOpenHashMap expectedHashMap = fixture.getFirstPLIAsHashMap();
		
		assertEquals(expectedHashMap, firstPLI.asHashMap());
	}
	
	/**
	 * Test method for {@link PositionListIndex#size()}
	 * 
	 * Size should return the correct number of noon unary clusters of the {@link PositionListIndex}.
	 */
	@Test
	public void testSize() {
		// Setup
		PositionListIndex pli = fixture.getFirstPLI();
		
		// Execute functionality
		// Check result
		assertEquals(fixture.getFirstPLISize(), pli.size());
	}
	
	/**
	 * Test method for {@link PositionListIndex#isEmpty()}, {@link PositionListIndex#isUnique()}
	 * 
	 * Empty plis should return true on isEmpty and is Unique.
	 */
	@Test
	public void testIsEmptyUnique() {
		// Setup 
		List<LongOpenHashSet> clusters = new LinkedList<LongOpenHashSet>();
		PositionListIndex emptyPli = new PositionListIndex(clusters);
		PositionListIndex nonEmptyPli = fixture.getFirstPLI();
		
		// Execute functionality
		// Check result
		assertTrue(emptyPli.isEmpty());
		assertTrue(emptyPli.isUnique());
		assertFalse(nonEmptyPli.isEmpty());
		assertFalse(nonEmptyPli.isUnique());		
	}
	
	/**
	 * Test method for {@link PositionListIndex#getRawKeyError()}
	 * 
	 * The key error should be calculated correctly.
	 */
	@Test
	public void testGetRawKeyError() {
		// Setup
		PositionListIndex firstPli = fixture.getFirstPLI();
		PositionListIndex secondPli = fixture.getSecondPLI();
		
		// Execute functionality
		// Check result
		assertEquals(fixture.getExpectedFirstPLIRawKeyError(), firstPli.getRawKeyError());
		assertEquals(fixture.getExpectedSecondPLIRawKeyError(), secondPli.getRawKeyError());
	}
}
