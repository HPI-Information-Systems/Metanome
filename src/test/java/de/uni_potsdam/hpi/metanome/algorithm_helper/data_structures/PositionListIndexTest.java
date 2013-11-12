package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.PositionListIndex;

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
		
		assertEquals(expectedHashMap ,firstPLI.asHashMap());
	}
	

}
