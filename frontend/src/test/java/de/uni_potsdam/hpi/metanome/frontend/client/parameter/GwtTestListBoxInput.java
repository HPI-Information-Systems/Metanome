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

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.junit.client.GWTTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.ListBoxInput}
 *
 * @author Jakob Zwiener
 */
public class GwtTestListBoxInput extends GWTTestCase {

  protected ListBoxInput listBox;
  protected List<String> expectedValues;

  @Override
  public void gwtSetUp() throws Exception {
    listBox = new ListBoxInput(true);

    expectedValues = new ArrayList<>();
    expectedValues.add("value1");
    expectedValues.add("value2");
    expectedValues.add("value3");
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.ListBoxInput#getValues()}
   * and {@link ListBoxInput#setValues(java.util.List)}
   */
  public void testGetSetValues() {
    // Execute functionality
    listBox.setValues(expectedValues);
    List<String> actualValues = listBox.getValues();

    // Check result
    assertEquals(expectedValues, actualValues);
  }

  /**
   * Test method for {@link ListBoxInput#setSelectedValue(String)}
   *
   * When setting the selected value to a value that is not in the list box false should be
   * returned.
   */
  public void testSetSelectedValueNotExistent() {
    // Execute functionality
    boolean actualSuccess = listBox.setSelectedValue("not existing value");

    // Check result
    assertFalse(actualSuccess);
  }

  /**
   * Test method for {@link ListBoxInput#getSelectedValue()}
   *
   * On an empty list box the getSelectedValue method should return null.
   */
  public void testGetSelectedValueEmpty() {
    // Execute functionality
    String actualSelectedValue = listBox.getSelectedValue();

    // Check result
    assertNull(actualSelectedValue);
  }

  /**
   * Test method for {@link ListBoxInput#getSelectedValue()}
   *
   * On a list box with values but no selected value, the getSelectedValue method should return the
   * first value.
   */
  public void testGetSelectedValueNonSelected() {
    // Setup
    listBox.setValues(expectedValues);
    // Expected values
    String expectedSelectedValue = expectedValues.get(0);

    // Execute functionality
    String actualSelectedValue = listBox.getSelectedValue();

    // Check result
    assertEquals(expectedSelectedValue, actualSelectedValue);
  }

  /**
   * Test method for {@link ListBoxInput#getSelectedValue()} and {@link
   * de.uni_potsdam.hpi.metanome.frontend.client.parameter.ListBoxInput#setSelectedValue(String)}
   */
  public void testGetSetSelectedValue() {
    // Setup
    // Expected values
    String expectedSelectedValue = expectedValues.get(1);
    listBox.setValues(expectedValues);

    // Execute functionality
    assertTrue(listBox.setSelectedValue(expectedSelectedValue));
    String actualSelectedValue = listBox.getSelectedValue();

    // Check result
    assertEquals(expectedSelectedValue, actualSelectedValue);

  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.Metanome";
  }
}
