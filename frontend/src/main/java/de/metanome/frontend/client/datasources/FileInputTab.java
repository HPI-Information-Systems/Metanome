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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.services.FileInputRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;


public class FileInputTab extends FlowPanel implements TabContent {

  protected FlexTable fileInputList;
  protected FileInputEditForm editForm;
  private FileInputRestService fileInputService;
  private DataSourcePage parent;
  private TabWrapper messageReceiver;

  /**
   * @param parent the parent page
   */
  public FileInputTab(DataSourcePage parent) {
    this.fileInputService = com.google.gwt.core.client.GWT.create(FileInputRestService.class);
    this.parent = parent;

    this.fileInputList = new FlexTable();
    this.fileInputList = new FlexTable();
    this.editForm = new FileInputEditForm(this);

    this.addFileInputsToTable(this);
  }

  private void addEditForm() {
    this.add(new HTML("<hr>"));
    this.add(new HTML("<h3>Add a new File Input</h3>"));
    this.add(this.editForm);
  }

  /**
   * Gets all File Inputs available in the database and adds them to the table.
   *
   * @param panel the parent widget of the table
   */
  public void addFileInputsToTable(final FlowPanel panel) {
    fileInputService.listFileInputs(
        new MethodCallback<List<FileInput>>() {
          @Override
          public void onFailure(Method method, Throwable throwable) {
            messageReceiver.addError("There are no file inputs: " + method.getResponse().getText());
            addEditForm();
          }

          @Override
          public void onSuccess(Method method, List<FileInput> fileInputs) {
            listFileInputs(fileInputs);
            addEditForm();
          }

        });
  }

  /**
   * Lists all given file inputs in a table.
   *
   * @param inputs the file inputs
   */
  protected void listFileInputs(List<FileInput> inputs) {
    this.fileInputList.setHTML(0, 0, "<b>File Name</b>");
    this.fileInputList.setHTML(0, 1, "<b>Comment</b>");

    for (final FileInput input : inputs) {
      this.addFileInputToTable(input);
    }

    this.add(new HTML("<h3>List of all File Inputs</h3>"));
    this.add(this.fileInputList);
  }

  /**
   * Adds the given file input to the table.
   *
   * @param input the file input, which should be added.
   */
  public void addFileInputToTable(final FileInput input) {
    int row = this.fileInputList.getRowCount();

    Button deleteButton = new Button("Delete");
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        fileInputService.deleteFileInput(input.getId(),
                                         getDeleteCallback(input));
      }
    });

    Button editButton = new Button("Edit");
    editButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        editForm.updateFileInput(input);
      }
    });

    Button runButton = new Button("Analyze");
    runButton.setTitle(String.valueOf(input.getId()));
    runButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        parent.callRunConfiguration(convertFileInputToDataSource(input));
      }
    });

    this.fileInputList.setWidget(row, 0, new HTML(FilePathHelper.getFileName(input.getFileName())));
    this.fileInputList.setText(row, 1, input.getComment());
    this.fileInputList.setWidget(row, 2, runButton);
    this.fileInputList.setWidget(row, 3, deleteButton);
    this.fileInputList.setWidget(row, 4, editButton);
  }

  /**
   * Converts a file input into a ConfigurationSettingDataSource
   *
   * @param input the file input
   * @return the ConfigurationSettingDataSource from the given file input
   */
  private ConfigurationSettingDataSource convertFileInputToDataSource(FileInput input) {
    return new ConfigurationSettingFileInput()
        .setFileName(input.getFileName())
        .setEscapeChar(input.getEscapeChar())
        .setHeader(input.isHasHeader())
        .setIgnoreLeadingWhiteSpace(input.isIgnoreLeadingWhiteSpace())
        .setQuoteChar(input.getQuoteChar())
        .setSeparatorChar(input.getSeparator())
        .setSkipDifferingLines(input.isSkipDifferingLines())
        .setSkipLines(input.getSkipLines())
        .setStrictQuotes(input.isStrictQuotes())
        .setNullValue(input.getNullValue());
  }

  /**
   * Forwards the command to update the data sources on the run configuration page to the data
   * source page.
   */
  public void updateDataSourcesOnRunConfiguration() {
    this.parent.updateDataSourcesOnRunConfiguration();
  }

  /**
   * Creates the callback for the delete call.
   *
   * @param input The file input, which should be deleted.
   * @return The callback
   */
  protected MethodCallback<Void> getDeleteCallback(final FileInput input) {
    return new MethodCallback<Void>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError("Could not delete the file input: " + method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, Void aVoid) {
        fileInputList.removeRow(findRow(input));
      }
    };
  }

  /**
   * Find the row in the table, which contains the given file input.
   * @param input the file input
   * @return the row number
   */
  private int findRow(FileInput input) {
    int row = 0;
    String fileName = FilePathHelper.getFileName(input.getFileName());

    while (row < this.fileInputList.getRowCount()) {
      HTML fileWidget = (HTML) this.fileInputList.getWidget(row, 0);

      if (fileWidget != null && fileName.equals(fileWidget.getText())) {
        return row;
      }
      row++;
    }
    return -1;
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.editForm.setMessageReceiver(tab);
  }

  /**
   * Find the row of the old file input and updates the values.
   * @param updatedInput the updated file input
   * @param oldInput     the old file input
   */
  public void updateFileInputInTable(final FileInput updatedInput, FileInput oldInput) {
    int row = findRow(oldInput);

    this.fileInputList.setWidget(row, 0, new HTML(FilePathHelper.getFileName(updatedInput.getFileName())));
    this.fileInputList.setText(row, 1, updatedInput.getComment());

    Button editButton = new Button("Edit");
    editButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        editForm.updateFileInput(updatedInput);
      }
    });

    this.fileInputList.setWidget(row, 4, editButton);
  }
}
