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

package de.metanome.frontend.client.datasources;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.Button;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.backend.input.csv.FileIterator;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TestHelper;
import de.metanome.frontend.client.helpers.InputValidationException;


public class GwtTestFileInputEditForm extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.FileInputEditForm#getValue()}
   */
  public void testGetValue() throws InputValidationException, AlgorithmConfigurationException {
    // Set up
    // Expected
    FileInputEditForm
        field =
        new FileInputEditForm(new FileInputTab(new DataSourcePage(new BasePage())));

    String expectedFileName = "file name";
    String expectedComment = "comment";
    field.commentTextArea.setText(expectedComment);
    field.setFileName(expectedFileName);

    // Execute
    FileInput input = field.getValue();

    // Check
    assertEquals(expectedFileName, input.getFileName());
    assertEquals(expectedComment, input.getComment());
  }

  /**
   * Test method for {@link FileInputEditForm#getValue()}
   */
  public void testGetValueWithInvalidValues() {
    // Set up
    // Expected
    FileInputEditForm
        field =
        new FileInputEditForm(new FileInputTab(new DataSourcePage(new BasePage())));
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
  public void testGetValueWithCustomAdvancedSettings()
      throws InputValidationException, AlgorithmConfigurationException {
    // Set up
    // Expected
    FileInputEditForm
        field =
        new FileInputEditForm(new FileInputTab(new DataSourcePage(new BasePage())));

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
    assertEquals(Integer.valueOf(skipLines), input.getSkipLines());
    assertEquals(strictQuotes, input.isStrictQuotes());
    assertEquals(ignoreLeadingWhiteSpace, input.isIgnoreLeadingWhiteSpace());
    assertEquals(hasHeader, input.isHasHeader());
    assertEquals(skipDifferingLines, input.isSkipDifferingLines());
  }

  /**
   * Test method for {@link FileInputEditForm#getValue()}
   */
  public void testGetValueWithDefaultAdvancedSettings()
      throws InputValidationException, AlgorithmConfigurationException {
    // Set up
    FileInputEditForm
        field =
        new FileInputEditForm(new FileInputTab(new DataSourcePage(new BasePage())));
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
    assertEquals(Integer.valueOf(CSVReader.DEFAULT_SKIP_LINES), input.getSkipLines());
    assertEquals(CSVParser.DEFAULT_STRICT_QUOTES, input.isStrictQuotes());
    assertEquals(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE, input.isIgnoreLeadingWhiteSpace());
    assertEquals(FileIterator.DEFAULT_HAS_HEADER, input.isHasHeader());
    assertEquals(FileIterator.DEFAULT_SKIP_DIFFERING_LINES, input.isSkipDifferingLines());
  }


  /**
   * When selecting the "Advanced" checkbox, additional input fields become visible, containing the
   * default values that will be used if none are specified.
   */
  public void testVisibilityOfAdvancedSettings() {
    // Set up
    FileInputEditForm
        widget =
        new FileInputEditForm(new FileInputTab(new DataSourcePage(new BasePage())));

    // Check default visibility
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
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.FileInputEditForm#reset()}
   */
  public void testResetValues() {
    //Setup
    FileInputEditForm
        input =
        new FileInputEditForm(new FileInputTab(new DataSourcePage(new BasePage())));
    input.fileListBox.addValue("file1");
    input.fileListBox.addValue("file2");
    input.fileListBox.setSelectedValue("file2");
    input.commentTextArea.setText("comment");

    // Execute
    input.reset();

    String actualFile = input.fileListBox.getSelectedValue();
    String actualComment = input.commentTextArea.getText();

    //Check
    assertEquals("--", actualFile);
    assertEquals("", actualComment);
  }

  /**
   * Test method for {@link de.metanome.frontend.client.datasources.FileInputEditForm#updateFileInput(de.metanome.backend.results_db.FileInput)}
   * and test method for {@link FileInputEditForm#showSaveButton()}
   *
   * If the edit button for a file input is clicked, the edit form should contain the values
   * of that file input and the edit form should show a update button instead of an save button.
   * If the method 'show save button' is called, the save button should be visible again.
   */
  public void testEditButtonClicked() throws InputValidationException {
    // Setup
    TestHelper.resetDatabaseSync();

    FileInputEditForm
        editForm =
        new FileInputEditForm(new FileInputTab(new DataSourcePage(new BasePage())));

    // Expected Values
    String expectedFileName = "file name";
    char separator = ';';
    char quotechar = '"';
    char escapechar = '\\';
    int skipLines = 0;
    boolean strictQuotes = true;
    boolean ignoreLeadingWhiteSpace = false;
    boolean hasHeader = true;
    boolean skipDifferingLines = false;

    FileInput fileInput = new FileInput(expectedFileName)
        .setSeparator(separator)
        .setQuotechar(quotechar)
        .setEscapechar(escapechar)
        .setSkipLines(skipLines)
        .setStrictQuotes(strictQuotes)
        .setIgnoreLeadingWhiteSpace(ignoreLeadingWhiteSpace)
        .setHasHeader(hasHeader)
        .setSkipDifferingLines(skipDifferingLines);

    // Execute
    editForm.updateFileInput(fileInput);

    // Check results
    assertEquals(expectedFileName, editForm.fileListBox.getSelectedValue());
    assertEquals(separator, editForm.getChar(editForm.separatorTextbox));
    assertEquals(quotechar, editForm.getChar(editForm.quoteTextbox));
    assertEquals(escapechar, editForm.getChar(editForm.escapeTextbox));
    assertEquals(skipLines, (int) editForm.skiplinesIntegerbox.getValue());
    assertEquals(strictQuotes, (boolean) editForm.strictQuotesCheckbox.getValue());
    assertEquals(hasHeader, (boolean) editForm.headerCheckbox.getValue());
    assertEquals(ignoreLeadingWhiteSpace, (boolean) editForm.ignoreLeadingWhiteSpaceCheckbox.getValue());
    assertEquals(skipDifferingLines, (boolean) editForm.skipDifferingLinesCheckbox.getValue());

    assertEquals(((Button) editForm.getWidget(2, 0)).getText(), "Update");

    // Execute
    editForm.showSaveButton();

    // Check results
    assertEquals(((Button) editForm.getWidget(2, 0)).getText(), "Save");
  }


  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }

}
