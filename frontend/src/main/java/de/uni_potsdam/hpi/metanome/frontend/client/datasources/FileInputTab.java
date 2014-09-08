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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.FilePathHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;

import java.util.List;


public class FileInputTab extends FlowPanel implements TabContent {

  private FileInputServiceAsync fileInputService;
  private DataSourcePage parent;

  protected FlexTable fileInputList;

  private TabWrapper messageReceiver;

  protected FileInputEditForm editForm;

  /**
   * @param parent the parent page
   */
  public FileInputTab(DataSourcePage parent) {
    this.fileInputService = com.google.gwt.core.shared.GWT.create(FileInputService.class);
    this.parent = parent;

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
   * @param panel the parent widget of the table
   */
  public void addFileInputsToTable(final FlowPanel panel) {
    fileInputService.listFileInputs(
      new AsyncCallback<List<FileInput>>() {
        @Override
        public void onFailure(Throwable throwable) {
          panel.add(new Label("There are no File Inputs yet."));
          addEditForm();
        }

        @Override
        public void onSuccess(List<FileInput> fileInputs) {
          listFileInputs(fileInputs);
          addEditForm();
        }
      });
  }

  /**
   * Lists all given file inputs in a table.
   * @param inputs the file inputs
   */
  protected void listFileInputs(List<FileInput> inputs) {
    if (inputs.isEmpty()) {
      this.add(new HTML("<h4>There are no File Inputs yet.</h4>"));
      return;
    }

    this.fileInputList.setHTML(0, 0, "<b>File Name</b>");

    for (final FileInput input : inputs) {
      this.addFileInputToTable(input);
    }

    this.add(new HTML("<h3>List of all File Inputs</h3>"));
    this.add(this.fileInputList);
  }

  /**
   * Adds the given file input to the table.
   * @param input the file input, which should be added.
   */
  public void addFileInputToTable(final FileInput input) {
    int row = this.fileInputList.getRowCount();

    Button deleteButton = new Button("Delete");
    // TODO: add click handler

    Button runButton = new Button("Run");
    runButton.setTitle(String.valueOf(input.getId()));
    runButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        parent.callRunConfiguration(convertFileInputToDataSource(input));
      }
    });

    this.fileInputList.setText(row, 0, FilePathHelper.getFileName(input.getFileName()));
    this.fileInputList.setWidget(row, 1, runButton);
    this.fileInputList.setWidget(row, 2, deleteButton);
  }

  /**
   * Converts a file input into a ConfigurationSettingDataSource
   * @param input the file input
   * @return      the ConfigurationSettingDataSource from the given file input
   */
  private ConfigurationSettingDataSource convertFileInputToDataSource(FileInput input) {
    ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();

    setting.setFileName(input.getFileName());
    setting.setEscapeChar(input.getEscapechar());
    setting.setHeader(input.isHasHeader());
    setting.setIgnoreLeadingWhiteSpace(input.isIgnoreLeadingWhiteSpace());
    setting.setQuoteChar(input.getQuotechar());
    setting.setSeparatorChar(input.getSeparator());
    setting.setSkipDifferingLines(input.isSkipDifferingLines());
    setting.setSkipLines(input.getSkipLines());
    setting.setStrictQuotes(input.isStrictQuotes());

    return setting;
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.editForm.setMessageReceiver(tab);
  }
}
