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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.TableInputService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.TableInputServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;
import de.uni_potsdam.hpi.metanome.results_db.TableInput;

import java.util.List;

/**
 * Page to configure data inputs.
 */
public class InputConfigurationPage extends VerticalPanel implements TabContent {

  private static final String DATABASE_CONNECTION = "Database Connection";
  private static final String FILE_INPUT = "File Input";
  private static final String TABLE_INPUT = "Table Input";

  private TabWrapper messageReceiver;
  private BasePage basePage;

  protected VerticalPanel content;
  protected VerticalPanel editForm;
  protected DatabaseConnectionField dbField;
  protected FileInputField fileInputField;
  protected TableInputField tableInputField;

  protected Boolean dbFieldSelected;
  protected Boolean tableInputFieldSelected;
  protected Boolean fileInputFieldSelected;

  protected Button saveButton;

  /**
   * Constructor.
   */
  public InputConfigurationPage(BasePage basePage) {
    this.setWidth("100%");
    this.basePage = basePage;

    // Initialize all input fields
    this.dbField = new DatabaseConnectionField();
    this.fileInputField = new FileInputField();
    this.tableInputField = new TableInputField();

    // Initialize all buttons
    Button dbButton = new Button(DATABASE_CONNECTION);
    dbButton.addClickHandler(getClickHandlerForDbButton());
    Button fileButton = new Button(FILE_INPUT);
    fileButton.addClickHandler(getClickHandlerForFileButton());
    Button tableButton = new Button(TABLE_INPUT);
    tableButton.addClickHandler(getClickHandlerForTableButton());
    this.saveButton = new Button("Save", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        saveObject();
      }
    });

    // initialize all panels
    VerticalPanel wrapper = new VerticalPanel();
    HorizontalPanel buttonWrapper = new HorizontalPanel();
    SimplePanel contentWrapper = new SimplePanel();
    this.editForm = new VerticalPanel();

    // add buttons
    buttonWrapper.add(dbButton);
    buttonWrapper.add(tableButton);
    buttonWrapper.add(fileButton);

    // set up content
    this.content = new VerticalPanel();
    contentWrapper.add(this.content);
    fillContent(DATABASE_CONNECTION);

    // add wrapper to page
    wrapper.add(buttonWrapper);
    wrapper.add(contentWrapper);
    this.add(wrapper);

    // set selected field to database connection
    this.dbFieldSelected = true;
    this.tableInputFieldSelected = false;
    this.fileInputFieldSelected = false;
  }

  private ClickHandler getClickHandlerForFileButton() {
    return new ClickHandler() {
      public void onClick(ClickEvent event) {
        messageReceiver.clearErrors();
        messageReceiver.clearInfos();

        content.clear();
        fillContent(FILE_INPUT);

        dbFieldSelected = false;
        tableInputFieldSelected = false;
        fileInputFieldSelected = true;
      }
    };
  }

  /**
   * Creates the click handler for the database connection button.
   * @return the callback
   */
  private ClickHandler getClickHandlerForDbButton() {
    return new ClickHandler() {
      public void onClick(ClickEvent event) {
        messageReceiver.clearErrors();
        messageReceiver.clearInfos();

        content.clear();
        fillContent(DATABASE_CONNECTION);

        dbFieldSelected = true;
        tableInputFieldSelected = false;
        fileInputFieldSelected = false;
      }
    };
  }

  /**
   * Creates the click handler for the table input button.
   * @return the callback
   */
  private ClickHandler getClickHandlerForTableButton() {
    return new ClickHandler() {
      public void onClick(ClickEvent event) {
        messageReceiver.clearErrors();
        messageReceiver.clearInfos();

        tableInputField.updateDatabaseConnectionListBox();
        fillContent(TABLE_INPUT);

        dbFieldSelected = false;
        tableInputFieldSelected = true;
        fileInputFieldSelected = false;
      }
    };
  }

  /**
   * Gets and stores the current selected input into the database.
   */
  protected void saveObject() {
    this.messageReceiver.clearErrors();
    this.messageReceiver.clearInfos();

    if (dbFieldSelected) {
      DatabaseConnectionField w = (DatabaseConnectionField) editForm.getWidget(0);
      try {
        DatabaseConnectionServiceAsync databaseConnectionService = GWT.create(DatabaseConnectionService.class);
        databaseConnectionService.storeDatabaseConnection(w.getValue(), new AsyncCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            messageReceiver.addError("Database Connection could not be stored!");
          }

          @Override
          public void onSuccess(Void aVoid) {
            dbField.reset();
            fillContent(DATABASE_CONNECTION);
            messageReceiver.addInfo("Database Connection successfully saved.");
          }
        });
      } catch (InputValidationException e) {
        messageReceiver.addError("Database Connection could not be stored: " + e.getMessage());
      }

    } else if (tableInputFieldSelected) {
      TableInputField w = (TableInputField) editForm.getWidget(0);
      try {
        TableInputServiceAsync tableInputService = GWT.create(TableInputService.class);
        tableInputService.storeTableInput(w.getValue(), new AsyncCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            messageReceiver.addError("Database Connection could not be stored!");
          }

          @Override
          public void onSuccess(Void aVoid) {
            tableInputField.reset();
            fillContent(TABLE_INPUT);
            messageReceiver.addInfo("Table Input successfully saved.");
          }
        });
      } catch (InputValidationException | EntityStorageException e) {
        messageReceiver.addError("Table Input could not be stored: " + e.getMessage());
      }

    } else if (fileInputFieldSelected) {
      FileInputField w = (FileInputField) editForm.getWidget(0);
      try {
        FileInputServiceAsync fileInputService = GWT.create(FileInputService.class);
        fileInputService.storeFileInput(w.getValue(), new AsyncCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            messageReceiver.addError("Database Connection could not be stored!");
          }

          @Override
          public void onSuccess(Void aVoid) {
            fileInputField.reset();
            fillContent(FILE_INPUT);
            messageReceiver.addInfo("File Input successfully saved.");
          }
        });
      } catch (InputValidationException e) {
        messageReceiver.addError("File Input could not be stored: " + e.getMessage());
      }
    }
  }

  /**
   * Fills the content widget according to the selected input type.
   * If there already exists some inputs of the type, the inputs are listed.
   * Additionally, a form to add a new input of that type is added.
   * @param type the selected input type
   */
  private void fillContent(String type) {
    this.content.clear();
    switch (type) {
      case DATABASE_CONNECTION:
        DatabaseConnectionServiceAsync databaseConnectionService = GWT.create(DatabaseConnectionService.class);
        databaseConnectionService.listDatabaseConnections(
          new AsyncCallback<List<DatabaseConnection>>() {
            @Override
            public void onFailure(Throwable throwable) {
              content.add(new Label("There are no Database Connections yet."));
              addEditForm(DATABASE_CONNECTION);
            }

            @Override
            public void onSuccess(List<DatabaseConnection> connections) {
              listDatabaseConnections(connections);
              addEditForm(DATABASE_CONNECTION);
            }
          });
        break;
      case TABLE_INPUT:
        TableInputServiceAsync tableInputService = GWT.create(TableInputService.class);
        tableInputService.listTableInputs(new AsyncCallback<List<TableInput>>() {
          @Override
          public void onFailure(Throwable throwable) {
            content.add(new Label("There are no Table Inputs yet."));
            addEditForm(TABLE_INPUT);
          }

          @Override
          public void onSuccess(List<TableInput> tableInputs) {
            listTableInputs(tableInputs);
            addEditForm(TABLE_INPUT);
          }
        });
        break;
      case FILE_INPUT:
        FileInputServiceAsync fileInputService = GWT.create(FileInputService.class);
        fileInputService.listFileInputs(
          new AsyncCallback<List<FileInput>>() {
            @Override
            public void onFailure(Throwable throwable) {
              content.add(new Label("There are no File Inputs yet."));
              addEditForm(FILE_INPUT);
            }

            @Override
            public void onSuccess(List<FileInput> fileInputs) {
              listFileInputs(fileInputs);
              addEditForm(FILE_INPUT);
            }
          });
        break;
      default:
        this.messageReceiver.addError("Type not supported!");
        break;
    }
  }

  /**
   * Adds a edit form to add a new input of the given type.
   * @param type the input type
   */
  private void addEditForm(String type) {
    switch (type) {
      case DATABASE_CONNECTION:
        this.content.add(new HTML("<hr>"));
        this.content.add(new HTML("<h3>Add a new Database Connection</h3>"));
        this.editForm.clear();
        this.editForm.add(dbField);
        this.content.add(this.editForm);
        this.content.add(saveButton);
        break;
      case FILE_INPUT:
        this.content.add(new HTML("<hr>"));
        this.content.add(new HTML("<h3>Add a new File Input</h3>"));
        this.editForm.clear();
        this.editForm.add(fileInputField);
        this.content.add(this.editForm);
        this.content.add(saveButton);
        break;
      case TABLE_INPUT:
        this.content.add(new HTML("<hr>"));
        this.content.add(new HTML("<h3>Add a new Table Input</h3>"));
        this.editForm.clear();
        this.editForm.add(tableInputField);
        this.content.add(this.editForm);
        this.content.add(saveButton);
        break;
      default:
        this.messageReceiver.addError("Type not supported!");
        break;
    }

  }

  /**
   * Lists all given table inputs in a table and adds the table to the content.
   * @param inputs the table inputs
   */
  private void listTableInputs(List<TableInput> inputs) {
    FlexTable table = new FlexTable();
    int row = 1;

    table.setHTML(0, 0, "<b>Database Connection</b>");
    table.setHTML(0, 1, "<b>Table Name</b>");

    for (TableInput input : inputs) {
      Button deleteButton = new Button("Delete");
      // TODO: add click handler

      table.setText(row, 0, input.getDatabaseConnection().getUrl());
      table.setText(row, 1, input.getTableName());
      table.setWidget(row, 2, deleteButton);
      row++;
    }

    this.content.add(new HTML("<h3>List of all Table Inputs</h3>"));
    this.content.add(table);
  }

  /**
   * Lists all given file inputs in a table and adds the table to the content.
   * @param inputs the file inputs
   */
  private void listFileInputs(List<FileInput> inputs) {
    FlexTable table = new FlexTable();
    int row = 1;

    table.setHTML(0, 0, "<b>File Name</b>");

    for (FileInput input : inputs) {
      Button deleteButton = new Button("Delete");
      // TODO: add click handler

      table.setText(row, 0, input.getFileName());
      table.setWidget(row, 1, deleteButton);
      row++;
    }

    this.content.add(new HTML("<h3>List of all File Inputs</h3>"));
    this.content.add(table);
  }

  /**
   * Lists all given database connections in a table and adds the table to the content.
   * @param inputs
   */
  private void listDatabaseConnections(List<DatabaseConnection> inputs) {
    FlexTable table = new FlexTable();
    int row = 1;

    table.setHTML(0, 0, "<b>Url</b>");
    table.setHTML(0, 1, "<b>Username</b>");
    table.setHTML(0, 2, "<b>Password</b>");

    for (DatabaseConnection input : inputs) {
      Button deleteButton = new Button("Delete");
      // TODO: add click handler

      table.setText(row, 0, input.getUrl());
      table.setText(row, 1, input.getUsername());
      table.setText(row, 2, input.getPassword());
      table.setWidget(row, 3, deleteButton);
      row++;
    }

    this.content.add(new HTML("<h3>List of all Database Connections</h3>"));
    this.content.add(table);
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.tableInputField.setMessageReceiver(tab);
  }

}
