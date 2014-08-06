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

package de.uni_potsdam.hpi.metanome.frontend.client.datasources;

import com.google.gwt.junit.client.GWTTestCase;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.input.csv.CsvFile;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;

import org.junit.Test;


public class GwtTestFileInputField extends GWTTestCase {

  /**
   * Test method for {@link FileInputEditForm#getValue()}
   */
  @Test
  public void testGetValue() throws InputValidationException, AlgorithmConfigurationException {
    // Set up
    // Expected
    FileInputEditForm field = new FileInputEditForm();

    String expectedFileName = "file name";
    field.setFileName(expectedFileName);

    // Execute
    FileInput input = field.getValue();

    // Check
    assertEquals(expectedFileName, input.getFileName());
  }

  /**
   * Test method for {@link FileInputEditForm#getValue()}
   */
  @Test
  public void testGetValueWithInvalidValues() {
    // Set up
    // Expected
    FileInputEditForm field = new FileInputEditForm();
    // Execute
    // Check
    try {
      field.getValue();
    } catch (InputValidationException e) {
      assertTrue(true);
    }
  }

  /**
   * Test method for {@link FileInputEditForm#getValue()}
   */
  @Test
  public void testGetValueWithCustomAdvancedSettings() throws InputValidationException, AlgorithmConfigurationException {
    // Set up
    // Expected
    FileInputEditForm field = new FileInputEditForm();

    String expectedFileName = "file name";
    char separator = ';';
    char quotechar = '"';
    char escapechar = '\\';
    int skipLines = 0;
    boolean strictQuotes = true;
    boolean ignoreLeadingWhiteSpace = false;
    boolean hasHeader = true;
    boolean skipDifferingLines = false;

    field.setFileName(expectedFileName);
    field.setSeparator(separator);
    field.setQuotechar(quotechar);
    field.setEscapechar(escapechar);
    field.setSkipLines(skipLines);
    field.setStrictQuotes(strictQuotes);
    field.setIgnoreLeadingWhiteSpace(ignoreLeadingWhiteSpace);
    field.setHasHeader(hasHeader);
    field.setSkipDifferingLines(skipDifferingLines);

    field.advancedCheckbox.setValue(true);

    // Execute
    FileInput input = field.getValue();

    // Check
    assertEquals(expectedFileName, input.getFileName());
    assertEquals(separator, input.getSeparator());
    assertEquals(quotechar, input.getQuotechar());
    assertEquals(escapechar, input.getEscapechar());
    assertEquals(skipLines, input.getSkipLines());
    assertEquals(strictQuotes, input.isStrictQuotes());
    assertEquals(ignoreLeadingWhiteSpace, input.isIgnoreLeadingWhiteSpace());
    assertEquals(hasHeader, input.isHasHeader());
    assertEquals(skipDifferingLines, input.isSkipDifferingLines());
  }

  /**
   * Test method for {@link FileInputEditForm#getValue()}
   */
  @Test
  public void testGetValueWithDefaultAdvancedSettings() throws InputValidationException, AlgorithmConfigurationException {
    // Set up
    FileInputEditForm field = new FileInputEditForm();
    field.advancedCheckbox.setValue(true);

    // Expected
    String expectedFileName = "file name";
    field.setFileName(expectedFileName);

    // Execute
    FileInput input = field.getValue();

    // Check
    assertEquals(expectedFileName, input.getFileName());
    assertEquals(CSVParser.DEFAULT_SEPARATOR, input.getSeparator());
    assertEquals(CSVParser.DEFAULT_QUOTE_CHARACTER, input.getQuotechar());
    assertEquals(CSVParser.DEFAULT_ESCAPE_CHARACTER, input.getEscapechar());
    assertEquals(CSVReader.DEFAULT_SKIP_LINES, input.getSkipLines());
    assertEquals(CSVParser.DEFAULT_STRICT_QUOTES, input.isStrictQuotes());
    assertEquals(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE, input.isIgnoreLeadingWhiteSpace());
    assertEquals(CsvFile.DEFAULT_HAS_HEADER, input.isHasHeader());
    assertEquals(CsvFile.DEFAULT_SKIP_DIFFERING_LINES, input.isSkipDifferingLines());
  }


  /**
   * When selecting the "Advanced" checkbox, additional input fields become visible, containing the
   * default values that will be used if none are specified.
   */
  @Test
  public void testVisibilityOfAdvancedSettings() {
    // Set up
    FileInputEditForm widget = new FileInputEditForm();

    // Check default visibility
    assertFalse(widget.advancedTable.isVisible());
    assertFalse(widget.escapeTextbox.isAttached() && widget.escapeTextbox.isVisible());
    assertFalse(widget.skiplinesIntegerbox.isAttached() && widget.skiplinesIntegerbox.isVisible());
    assertFalse(widget.separatorTextbox.isAttached() && widget.separatorTextbox.isVisible());
    assertFalse(widget.quoteTextbox.isAttached() && widget.quoteTextbox.isVisible());
    assertFalse(widget.strictQuotesCheckbox.isAttached() && widget.strictQuotesCheckbox.isVisible());
    assertFalse(widget.ignoreLeadingWhiteSpaceCheckbox.isAttached() && widget.ignoreLeadingWhiteSpaceCheckbox.isVisible());
    assertFalse(widget.headerCheckbox.isAttached() && widget.headerCheckbox.isVisible());
    assertFalse(widget.skipDifferingLinesCheckbox.isAttached() && widget.skipDifferingLinesCheckbox.isVisible());

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
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.datasources.FileInputEditForm#reset()}
   */
  @Test
  public void testResetValues() {
    //Setup
    FileInputEditForm input = new FileInputEditForm();
    input.fileListBox.addValue("file1");
    input.fileListBox.addValue("file2");
    input.fileListBox.setSelectedValue("file2");

    // Execute
    input.reset();

    String actualFile = input.fileListBox.getSelectedValue();

    //Check
    assertEquals("--", actualFile);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }

}
