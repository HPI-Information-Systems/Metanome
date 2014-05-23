/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link ConfigurationSpecificationEnum}
 */
public class ConfigurationSpecificationEnumTest {

	/**
	 * Test method for {@link ConfigurationSpecificationEnum#ConfigurationSpecificationEnum(String)}
	 * <p/>
	 * The identifier should be set in the constructor and be retrievable through getIdentifier.
	 * The numberOfValues should be set to 1.
	 */
	@Test
	public void testConstructorGetOne() {
		// Setup
		// Expected values
		String expectedIdentifier = "parameter1";
		int expectedNumberOfValues = 1;
		ConfigurationSpecificationEnum configSpec = new ConfigurationSpecificationEnum(expectedIdentifier);

		// Execute functionality
		String actualIdentifier = configSpec.getIdentifier();
		int actualNumberOfValues = configSpec.getNumberOfValues();

		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);
		assertEquals(expectedNumberOfValues, actualNumberOfValues);
	}

	/**
	 * Test method for {@link ConfigurationSpecificationEnum#ConfigurationSpecificationEnum(String, int)}
	 * <p/>
	 * The identifier should be set in the constructor and be retrievable through getIdentifier.
	 * The numberOfValues should be set to 2.
	 */
	@Test
	public void testConstructorGetTwo() {
		// Setup
		// Expected values
		String expectedIdentifier = "parameter1";
		int expectedNumberOfValues = 2;
		ConfigurationSpecificationEnum configSpec = new ConfigurationSpecificationEnum(expectedIdentifier, expectedNumberOfValues);

		// Execute functionality
		String actualIdentifier = configSpec.getIdentifier();
		int actualNumberOfValues = configSpec.getNumberOfValues();

		// Check result
		assertEquals(expectedIdentifier, actualIdentifier);
		assertEquals(expectedNumberOfValues, actualNumberOfValues);
	}

	/**
	 * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationEnum#getSettings()} and {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationEnum#setSettings(ConfigurationSettingEnum...)}
	 */
	@Test
	public void testGetSetSpecification() {
		// Setup
		ConfigurationSpecificationEnum specificationEnum = new ConfigurationSpecificationEnum("parameter1");
		// Expected values
		ConfigurationSettingEnum expectedSetting1 = new ConfigurationSettingEnum();
		ConfigurationSettingEnum expectedSetting2 = new ConfigurationSettingEnum();

		// Execute functionality
		specificationEnum.setSettings(expectedSetting1, expectedSetting2);
		List<ConfigurationSettingEnum> actualSettings = Arrays.asList(specificationEnum.getSettings());

		// Check results
		assertThat(actualSettings, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedSetting1, expectedSetting2));
	}
}