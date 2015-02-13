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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.metanome.backend.input.csv.FileIterator;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.ListBoxInput;
import de.metanome.frontend.client.services.FileInputRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Input field to configure a file input.
 */
public class FileInputEditForm extends Grid {

  /**
   * Dropdown menu for choosing a CSV file
   */
  protected ListBoxInput fileListBox;
  /**
   * Triggers whether advanced options are displayed and evaluated.
   */
  protected CheckBox advancedCheckbox;
  /**
   * Wraps all advanced options' UI elements
   */
  protected FlexTable advancedTable;
  protected TextBox separatorTextBox;
  protected TextBox quoteTextBox;
  protected TextBox escapeTextBox;
  protected IntegerBox skipLinesIntegerBox;
  protected CheckBox strictQuotesCheckbox;
  protected CheckBox ignoreLeadingWhiteSpaceCheckbox;
  protected CheckBox headerCheckbox;
  protected CheckBox skipDifferingLinesCheckbox;
  protected TextArea commentTextArea;
  private FileInputRestService fileInputService;
  private FileInputTab parent;
  private String path = "";
  private TabWrapper messageReceiver;
  private List<String> filesOnStorage;
  private List<String> filesInDatabase;
  private Button saveButton;
  private Button updateButton;
  private FileInput oldFileInput;

  /**
   * Constructor. Set up all UI elements.
   */
  public FileInputEditForm(FileInputTab parent) {
    super(3, 2);

    this.parent = parent;
    this.fileInputService = com.google.gwt.core.client.GWT.create(FileInputRestService.class);

    Grid standardPanel = new Grid(3, 3);
    this.setWidget(0, 0, standardPanel);

    Button refreshButton = new Button("Refresh");
    refreshButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        updateListBox();
      }
    });

    fileListBox = new ListBoxInput(false);
    updateListBox();
    standardPanel.setText(0, 0, "File Name");
    standardPanel.setWidget(0, 1, fileListBox);
    standardPanel.setWidget(0, 2, refreshButton);

    commentTextArea = new TextArea();
    commentTextArea.setVisibleLines(3);
    standardPanel.setText(1, 0, "Comment");
    standardPanel.setWidget(1, 1, commentTextArea);

    advancedCheckbox = createAdvancedCheckbox();
    standardPanel.setWidget(2, 1, advancedCheckbox);

    advancedTable = new FlexTable();
    advancedTable.setVisible(false);
    this.setWidget(1, 0, advancedTable);

    separatorTextBox = getNewOneCharTextBox();
    separatorTextBox.setName("Separator Character");
    addRow(advancedTable, separatorTextBox, "Separator Character");

    quoteTextBox = getNewOneCharTextBox();
    quoteTextBox.setName("Quote Character");
    addRow(advancedTable, quoteTextBox, "Quote Character");

    escapeTextBox = getNewOneCharTextBox();
    escapeTextBox.setName("Escape Character");
    addRow(advancedTable, escapeTextBox, "Escape Character");

    skipLinesIntegerBox = new IntegerBox();
    skipLinesIntegerBox.setWidth("5em");
    skipLinesIntegerBox.setName("Line");
    addRow(advancedTable, skipLinesIntegerBox, "Line");

    strictQuotesCheckbox = new CheckBox();
    strictQuotesCheckbox.setName("Strict Quotes");
    addRow(advancedTable, strictQuotesCheckbox, "Strict Quotes");

    ignoreLeadingWhiteSpaceCheckbox = new CheckBox();
    addRow(advancedTable, ignoreLeadingWhiteSpaceCheckbox, "Ignore Leading Whitespace");

    headerCheckbox = new CheckBox();
    addRow(advancedTable, headerCheckbox, "Has Header");

    skipDifferingLinesCheckbox = new CheckBox();
    addRow(advancedTable, skipDifferingLinesCheckbox, "Skip Lines With Differing Length");

    setDefaultAdvancedSettings();

    this.saveButton = new Button("Save", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        saveFileInput();
      }
    });
    this.updateButton = new Button("Update", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        submitUpdate();
      }
    });
    this.setWidget(2, 0, saveButton);
  }

  /**
   * Updates the current file input in the database.
   */
  private void submitUpdate() {
    messageReceiver.clearErrors();
    try {
      FileInput updatedFileInput = this.getValue();
      updatedFileInput.setId(oldFileInput.getId());
      this.fileInputService.updateFileInput(updatedFileInput, new MethodCallback<FileInput>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
          messageReceiver.addError("File Input could not be updated: " + method.getResponse().getText());
          reset();
          showSaveButton();
          advancedTable.setVisible(false);
          advancedCheckbox.setValue(false);
          setDefaultAdvancedSettings();
          updateListBox();
        }

        @Override
        public void onSuccess(Method method, FileInput input) {
          reset();
          showSaveButton();
          parent.updateFileInputInTable(input, oldFileInput);
          parent.updateDataSourcesOnRunConfiguration();
          advancedTable.setVisible(false);
          advancedCheckbox.setValue(false);
          setDefaultAdvancedSettings();
          updateListBox();
        }
      });
    } catch (InputValidationException e) {
      messageReceiver.addError("Invalid Input: " + e.getMessage());
    }
  }

  /**
   * Stores the current file input in the database.
   */
  private void saveFileInput() {
    messageReceiver.clearErrors();
    try {
      this.fileInputService.storeFileInput(this.getValue(), new MethodCallback<FileInput>() {
        @Override
        public void onFailure(Method method, Throwable throwable) {
          messageReceiver
              .addError("File Input could not be stored: " + method.getResponse().getText());
        }

        @Override
        public void onSuccess(Method method, FileInput input) {
          reset();
          parent.addFileInputToTable(input);
          parent.updateDataSourcesOnRunConfiguration();
          advancedTable.setVisible(false);
          advancedCheckbox.setValue(false);
          setDefaultAdvancedSettings();
          updateListBox();
        }
      });
    } catch (InputValidationException e) {
      messageReceiver.addError("Invalid Input: " + e.getMessage());
    }
  }

  /**
   * Sets the default values of the advanced settings
   */
  private void setDefaultAdvancedSettings() {
    setSeparator(String.valueOf(CSVParser.DEFAULT_SEPARATOR));
    setQuoteChar(String.valueOf(CSVParser.DEFAULT_QUOTE_CHARACTER));
    setEscapeChar(String.valueOf(CSVParser.DEFAULT_ESCAPE_CHARACTER));
    setSkipLines(CSVReader.DEFAULT_SKIP_LINES);
    setStrictQuotes(CSVParser.DEFAULT_STRICT_QUOTES);
    setIgnoreLeadingWhiteSpace(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
    setHasHeader(FileIterator.DEFAULT_HAS_HEADER);
    setSkipDifferingLines(FileIterator.DEFAULT_SKIP_DIFFERING_LINES);
  }

  /**
   * Add another user input row to the bottom of the given table
   *
   * @param table       the UI object on which to add the row
   * @param inputWidget the widget to be used for input
   * @param name        the name of the input property
   */
  protected void addRow(FlexTable table, Widget inputWidget, String name) {
    int row = table.getRowCount();
    table.setText(row, 0, name);
    table.setWidget(row, 1, inputWidget);
  }

  /**
   * Creates a UI element for one-character user input
   *
   * @return a TextBox with width and input length limited to 2 (>1 to allow for escape characters)
   */
  private TextBox getNewOneCharTextBox() {
    TextBox textbox = new TextBox();
    textbox.setMaxLength(2);
    textbox.setWidth("2em");
    return textbox;
  }

  /**
   * Create the CheckBox that triggers the display/hiding of advanced CSV configuration parameters
   *
   * @return the CheckBox with the mentioned behavior
   */
  protected CheckBox createAdvancedCheckbox() {
    CheckBox checkbox = new CheckBox("Use Advanced Configuration");
    checkbox.setValue(false);
    checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

      @Override
      public void onValueChange(ValueChangeEvent<Boolean> event) {
        advancedTable.setVisible(advancedCheckbox.getValue());
      }
    });

    return checkbox;
  }

  /**
   * Finds all available CSV files and adds them to a drop-down menu with an empty entry ("--"),
   * which is selected by default but cannot be selected (it is disabled because it does not
   * represent a valid input file).
   */
  private void updateListBox() {
    this.fileListBox.clear();
    this.fileListBox.addValue("--");
    this.fileListBox.disableFirstEntry();

    this.filesOnStorage = new ArrayList<>();
    this.filesInDatabase = new ArrayList<>();

    // Add available CSV files
    MethodCallback<List<String>> storageCallback = getStorageCallback();
    MethodCallback<List<FileInput>> databaseCallback = getDatabaseCallback();
    FileInputRestService service = com.google.gwt.core.client.GWT.create(FileInputRestService.class);
    service.listFileInputs(databaseCallback);
    service.listCsvFiles(storageCallback);
  }

  private MethodCallback<List<FileInput>> getDatabaseCallback() {
    return new MethodCallback<List<FileInput>>() {
      public void onFailure(Method method, Throwable caught) {
        messageReceiver.addError(method.getResponse().getText());
      }

      public void onSuccess(Method method, List<FileInput> result) {
        List<String> fileNames = new ArrayList<>();

        for (FileInput input : result)
          filesInDatabase.add(input.getIdentifier());

        for (String path : filesOnStorage) {
          if (!filesInDatabase.contains(path)) {
            fileNames.add(FilePathHelper.getFileName(path));
          }
        }

        fileListBox.setValues(fileNames);
      }
    };
  }

  /**
   * Creates the callback for getting all available csv files. On success the files are added to the
   * according list box.
   *
   * @return the callback
   */
  protected MethodCallback<List<String>> getStorageCallback() {
    return new MethodCallback<List<String>>() {
      public void onFailure(Method method, Throwable caught) {
        messageReceiver
            .addError("Could not find CSV files! Please add them to the input folder.");
      }

      @Override
      public void onSuccess(Method method, List<String> result) {
        List<String> fileNames = new ArrayList<>();

        if (result.size() == 0) {
          messageReceiver
              .addError("Could not find CSV files! Please add them to the input folder.");
          return;
        }

        path = FilePathHelper.getFilePath(result.get(0));

        for (String path : result) {
          if (filesInDatabase.size() > 0 && !filesInDatabase.contains(path)) {
            fileNames.add(FilePathHelper.getFileName(path));
          }

          filesOnStorage.add(path);
        }

        fileListBox.setValues(fileNames);
      }
    };
  }

  /**
   * @return a FileInput with the selected File and the advanced settings, if selected
   * @throws InputValidationException if the file name is invalid
   */
  public FileInput getValue() throws InputValidationException {
    FileInput fileInput = new FileInput();

    String fileName = this.fileListBox.getSelectedValue();
    String comment = this.commentTextArea.getValue();

    if (fileName.isEmpty() || fileName.equals("--")) {
      throw new InputValidationException("The file name is invalid.");
    }

    fileInput.setFileName(this.path + fileName);
    fileInput.setComment(comment);

    if (this.advancedCheckbox.getValue()) {
      return setAdvancedSettings(fileInput);
    }

    return fileInput;
  }

  /**
   * Setting the advanced settings at the given file input.
   *
   * @param fileInput the file input at which the advanced settings should be set
   * @return the file input with set advanced settings
   */
  private FileInput setAdvancedSettings(FileInput fileInput) throws InputValidationException {
    fileInput.setEscapeChar(getChar(this.escapeTextBox));
    fileInput.setHasHeader(this.headerCheckbox.getValue());
    fileInput.setIgnoreLeadingWhiteSpace(this.ignoreLeadingWhiteSpaceCheckbox.getValue());
    fileInput.setQuoteChar(getChar(this.quoteTextBox));
    fileInput.setSeparator(getChar(this.separatorTextBox));
    fileInput.setSkipDifferingLines(this.skipDifferingLinesCheckbox.getValue());
    fileInput.setSkipLines(getInteger(this.skipLinesIntegerBox));
    fileInput.setStrictQuotes(this.strictQuotesCheckbox.getValue());

    return fileInput;
  }

  /**
   * Checks, if the given text box contains only a character. If yes, the character is returned.
   * Otherwise an exception is thrown.
   *
   * @return the character of the text box
   */
  protected String getChar(TextBox textBox) throws InputValidationException {
    String value = textBox.getValue();

    if ((value.length() == 2 && !value.startsWith("\\")) || value.length() > 2) {
      throw new InputValidationException(textBox.getName() + " should only contain one character!");
    }

    return value;
  }

  /**
   * Checks, if the value of the integer box is an integer. If yes, the integer is returned.
   * Otherwise an exception is thrown.
   *
   * @return the integer of the integer box
   */
  private int getInteger(IntegerBox integerBox) throws InputValidationException {
    try {
      return integerBox.getValueOrThrow();
    } catch (ParseException e) {
      throw new InputValidationException(integerBox.getName() + " should only contain numbers!");
    }
  }

  protected void setFileName(String fileName) {
    this.fileListBox.addValue(fileName);
    this.fileListBox.setSelectedValue(fileName);
  }

  protected void setSeparator(String separator) {
    this.separatorTextBox.setValue(separator);
  }

  protected void setEscapeChar(String escapeChar) {
    this.escapeTextBox.setValue(escapeChar);
  }

  protected void setQuoteChar(String quoteChar) {
    this.quoteTextBox.setValue(quoteChar);
  }

  protected void setSkipLines(int skipLines) {
    this.skipLinesIntegerBox.setValue(skipLines);
  }

  protected void setStrictQuotes(boolean strictQuotes) {
    this.strictQuotesCheckbox.setValue(strictQuotes);
  }

  protected void setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace) {
    this.ignoreLeadingWhiteSpaceCheckbox.setValue(ignoreLeadingWhiteSpace);
  }

  protected void setHasHeader(boolean hasHeader) {
    this.headerCheckbox.setValue(hasHeader);
  }

  protected void setSkipDifferingLines(boolean skipDifferingLines) {
    this.skipDifferingLinesCheckbox.setValue(skipDifferingLines);
  }

  /**
   * Reset the file name to the default entry "--" in the list box.
   */
  public void reset() {
    this.commentTextArea.setText("");
    this.fileListBox.reset();
    this.setDefaultAdvancedSettings();
  }

  /**
   * Fills the form with the values of the file input, which should be updated.
   * @param fileInput the file input
   */
  public void updateFileInput(FileInput fileInput) {
    this.updateListBox();

    setFileName(FilePathHelper.getFileName(fileInput.getFileName()));

    commentTextArea.setText(fileInput.getComment());

    setSeparator(fileInput.getSeparator());
    setQuoteChar(fileInput.getQuoteChar());
    setEscapeChar(fileInput.getEscapeChar());
    setSkipLines(fileInput.getSkipLines());
    setStrictQuotes(fileInput.isStrictQuotes());
    setIgnoreLeadingWhiteSpace(fileInput.isIgnoreLeadingWhiteSpace());
    setHasHeader(fileInput.isHasHeader());
    setSkipDifferingLines(fileInput.isSkipDifferingLines());

    this.setWidget(2, 0, updateButton);
    this.oldFileInput = fileInput;
  }

  /**
   * Shows the save button.
   */
  public void showSaveButton() {
    this.setWidget(2, 0, saveButton);
  }

  /**
   * Set the message receiver.
   *
   * @param tab the message receiver tab wrapper
   */
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}
