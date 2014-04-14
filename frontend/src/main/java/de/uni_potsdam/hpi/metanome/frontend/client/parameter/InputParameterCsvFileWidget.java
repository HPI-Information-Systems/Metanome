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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.StringHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataServiceAsync;

public class InputParameterCsvFileWidget extends VerticalPanel implements InputParameterDataSourceWidget {

    /**
     * Corresponding inputParameter, where the value is going to be written
     */
    private InputParameterCsvFile inputParameter;

    /**
     * Dropdown menu for choosing a CSV file
     */
    public ListBox listbox;
    protected CheckBox advancedCheckbox;
    protected FlexTable advancedTable;
    protected TextBox separatorTextbox;
    protected TextBox quoteTextbox;
    protected TextBox escapeTextbox;
    protected IntegerBox lineIntegerbox;
    protected CheckBox strictQuotesCheckbox;
    protected CheckBox ignoreLeadingWhiteSpaceCheckbox;

    /**
     * Constructor.
     *
     * @param inputParameter
     */
    public InputParameterCsvFileWidget(InputParameterCsvFile inputParameter) {
        super();
        this.inputParameter = inputParameter;

        HorizontalPanel standardPanel = new HorizontalPanel();
        this.add(standardPanel);

        listbox = createListbox();
        standardPanel.add(listbox);

        advancedCheckbox = createAdvancedCheckbox();
        standardPanel.add(advancedCheckbox);

        advancedTable = new FlexTable();
        advancedTable.setVisible(false);
        this.add(advancedTable);

        separatorTextbox = getNewOneCharTextbox();
        addRow(advancedTable, separatorTextbox, "Separator Character");

        quoteTextbox = getNewOneCharTextbox();
        addRow(advancedTable, quoteTextbox, "Quote Character");

        escapeTextbox = getNewOneCharTextbox();
        addRow(advancedTable, escapeTextbox, "Escape Character");

        lineIntegerbox = new IntegerBox();
        lineIntegerbox.setWidth("5em");
        addRow(advancedTable, lineIntegerbox, "Line");

        strictQuotesCheckbox = new CheckBox();
        addRow(advancedTable, strictQuotesCheckbox, "Strict Quotes");

        ignoreLeadingWhiteSpaceCheckbox = new CheckBox();
        addRow(advancedTable, ignoreLeadingWhiteSpaceCheckbox, "Ignore Leading Whitespace");
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
    private TextBox getNewOneCharTextbox() {
        TextBox textbox = new TextBox();
        textbox.setMaxLength(2);
        textbox.setWidth("2em");
        return textbox;
    }

    /**
     * Retrieves the current values from the UI and sets them on the given inputParameter
     *
     * @param inputParameter the object on which to set the values
     * @return the inputParameter with updated values
     */
    protected InputParameterCsvFile setCurrentValues(InputParameterCsvFile inputParameter) {
        inputParameter.setFileNameValue(this.listbox.getValue(this.listbox.getSelectedIndex()));
        inputParameter.setAdvanced(this.advancedCheckbox.getValue());

        if (inputParameter.isAdvanced()) {
            inputParameter.setSeparatorChar(StringHelper.getFirstCharFromInput(this.separatorTextbox.getValue()));
            inputParameter.setQuoteChar(StringHelper.getFirstCharFromInput(this.quoteTextbox.getValue()));
            inputParameter.setEscapeChar(StringHelper.getFirstCharFromInput(this.escapeTextbox.getValue()));
            inputParameter.setStrictQuotes(this.strictQuotesCheckbox.getValue());
            inputParameter.setIgnoreLeadingWhiteSpace(this.ignoreLeadingWhiteSpaceCheckbox.getValue());
            if (this.lineIntegerbox.getValue() != null)
                inputParameter.setLine(this.lineIntegerbox.getValue());
            else
                inputParameter.setLine(0);
        }

        return inputParameter;
    }

    /**
     * Create the CheckBox that triggers the display/hiding of advanced CSV configuration parameters
     *
     * @return the CheckBox with the mentioned behavior
     */
    protected CheckBox createAdvancedCheckbox() {
        CheckBox checkbox = new CheckBox("Use Advanced Configuration");
        checkbox.setValue(this.inputParameter.isAdvanced());
        checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                advancedTable.setVisible(advancedCheckbox.getValue());
            }
        });

        return checkbox;
    }

    /**
     * Finds all available CSV files and adds them to a drop-down menu with an empty
     * entry ("--"), which is selected by default but cannot be selected (it is disabled
     * because it does not represent a valid input file).
     *
     * @return a GWT ListBox containing all currently available CSV files
     */
    private ListBox createListbox() {
        ListBox listbox = new ListBox();

        //unselectable default entry
        listbox.addItem("--");
        listbox.getElement().getFirstChildElement().setAttribute("disabled", "disabled");
        //other entries
        addAvailableCsvsToListbox(listbox);
        return listbox;
    }

    /**
     * Calls the InputDataService to retrieve available CSV files (specified by their
     * file paths) and adds them as entries to the given ListBox. Only the actual file
     * name (not the preceding directories) are displayed.
     *
     * @param listbox the ListBox to add the available files' names to
     */
    private void addAvailableCsvsToListbox(final ListBox listbox) {
        AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
                caught.printStackTrace();
            }

            public void onSuccess(String[] result) {
                int index = 1;                    //start at 1 because index 0 has default ("--") entry
                for (String value : result) {
                    String displayName = value.substring(value.lastIndexOf("/") + 1);
                    listbox.addItem(displayName, value);
                    if (value.equals(inputParameter.getFileNameValue()))
                        listbox.setSelectedIndex(index);
                    index++;
                }
            }
        };

        InputDataServiceAsync service = GWT.create(InputDataService.class);
        service.listCsvInputFiles(callback);
    }

    /**
     * @return a list of the values displayed in the listbox, one String per entry
     */
    public String[] getListboxItemTexts() {
        String[] itemTexts = new String[listbox.getItemCount()];
        for (int i = 0; i < listbox.getItemCount(); i++) {
            itemTexts[i] = listbox.getItemText(i);
        }
        return itemTexts;
    }

    @Override
    public InputParameterCsvFile getInputParameter() {
        return this.setCurrentValues(this.inputParameter);
    }

    @Override
    public void setInputParameter(InputParameterDataSource parameter) {
        parameter.setIdentifier(this.inputParameter.getIdentifier());
        this.inputParameter = (InputParameterCsvFile) parameter;
    }

}
