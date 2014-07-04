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

package de.uni_potsdam.hpi.metanome.frontend.client.configuration;

import com.google.gwt.junit.client.GWTTestCase;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.StringHelper;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;

import org.junit.Test;


public class GwtTestFileInputField extends GWTTestCase {

  @Test
  public void testGetValue() throws InputValidationException, AlgorithmConfigurationException {
    // Set up
    // Expected
    FileInputField field = new FileInputField();

    String expectedFileName = "file name";
    field.setFileName(expectedFileName);

    // Execute
    FileInput input = field.getValue();

    // Check
    assertEquals(expectedFileName, input.getFileName());
  }

  /**
   * When selecting the "Advanced" checkbox, additional input fields become visible, containing the
   * default values that will be used if none are specified.
   */
  @Test
  public void testAdvancedDefaultEntries() {
    FileInputField widget = new FileInputField();

    assertFalse(widget.advancedTable.isVisible());
    assertFalse(widget.escapeTextbox.isAttached() && widget.escapeTextbox.isVisible());
    assertFalse(widget.skiplinesIntegerbox.isAttached() && widget.skiplinesIntegerbox.isVisible());
    assertFalse(widget.separatorTextbox.isAttached() && widget.separatorTextbox.isVisible());
    assertFalse(widget.quoteTextbox.isAttached() && widget.quoteTextbox.isVisible());
    assertFalse(
        widget.strictQuotesCheckbox.isAttached() && widget.strictQuotesCheckbox.isVisible());
    assertFalse(widget.ignoreLeadingWhiteSpaceCheckbox.isAttached()
                && widget.ignoreLeadingWhiteSpaceCheckbox.isVisible());
    assertFalse(widget.headerCheckbox.isAttached() && widget.headerCheckbox.isVisible());
    assertFalse(widget.skipDifferingLinesCheckbox.isAttached() && widget.skipDifferingLinesCheckbox
        .isVisible());

    // Execute
    widget.advancedCheckbox.setValue(true, true);

    // Check visibility
    assertTrue(widget.advancedTable.isVisible());
    assertTrue(widget.escapeTextbox.isVisible());
    assertTrue(widget.skiplinesIntegerbox.isVisible());
    assertTrue(widget.separatorTextbox.isVisible());
    assertTrue(widget.quoteTextbox.isVisible());
    assertTrue(widget.strictQuotesCheckbox.isVisible());
    assertTrue(widget.ignoreLeadingWhiteSpaceCheckbox.isVisible());
    assertTrue(widget.headerCheckbox.isVisible());
    assertTrue(widget.skipDifferingLinesCheckbox.isVisible());

    // Check values
    assertEquals(CSVParser.DEFAULT_ESCAPE_CHARACTER,
                 StringHelper.getFirstCharFromInput(widget.escapeTextbox.getValue()));
    assertEquals(CSVParser.DEFAULT_SEPARATOR,
                 StringHelper.getFirstCharFromInput(widget.separatorTextbox.getValue()));
    assertEquals(CSVParser.DEFAULT_QUOTE_CHARACTER,
                 StringHelper.getFirstCharFromInput(widget.quoteTextbox.getValue()));
    assertEquals(CSVReader.DEFAULT_SKIP_LINES,
                 widget.skiplinesIntegerbox.getValue().intValue());
    assertEquals(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE,
                 widget.ignoreLeadingWhiteSpaceCheckbox.getValue().booleanValue());
    assertEquals(CSVParser.DEFAULT_STRICT_QUOTES,
                 widget.strictQuotesCheckbox.getValue().booleanValue());
    assertEquals(true, widget.headerCheckbox.getValue().booleanValue());
    assertEquals(false, widget.skipDifferingLinesCheckbox.getValue().booleanValue());
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }

}
