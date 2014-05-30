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

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.StringHelper;

public class CsvFileInput extends InputField {

    /**
     * Dropdown menu for choosing a CSV file
     */
    public ListBox listbox;
    protected CheckBox advancedCheckbox;
    protected FlexTable advancedTable;
    protected TextBox separatorTextbox;
    protected TextBox quoteTextbox;
    protected TextBox escapeTextbox;
    protected IntegerBox skiplinesIntegerbox;
    protected CheckBox strictQuotesCheckbox;
    protected CheckBox ignoreLeadingWhiteSpaceCheckbox;

    private String preselectedFilename;

    /**
     * Constructor.
     *
     * @param configSpec
     */
    public CsvFileInput(boolean optional) {
        super(optional);

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
        separatorTextbox.setValue(String.valueOf(CSVParser.DEFAULT_SEPARATOR));
        addRow(advancedTable, separatorTextbox, "Separator Character");

        quoteTextbox = getNewOneCharTextbox();
        quoteTextbox.setValue(String.valueOf(CSVParser.DEFAULT_QUOTE_CHARACTER));
        addRow(advancedTable, quoteTextbox, "Quote Character");

        escapeTextbox = getNewOneCharTextbox();
        escapeTextbox.setValue(String.valueOf(CSVParser.DEFAULT_ESCAPE_CHARACTER));
        addRow(advancedTable, escapeTextbox, "Escape Character");

        skiplinesIntegerbox = new IntegerBox();
        skiplinesIntegerbox.setWidth("5em");
        skiplinesIntegerbox.setValue(CSVReader.DEFAULT_SKIP_LINES);
        addRow(advancedTable, skiplinesIntegerbox, "Line");

        strictQuotesCheckbox = new CheckBox();
        strictQuotesCheckbox.setValue(CSVParser.DEFAULT_STRICT_QUOTES);
        addRow(advancedTable, strictQuotesCheckbox, "Strict Quotes");

        ignoreLeadingWhiteSpaceCheckbox = new CheckBox();
        ignoreLeadingWhiteSpaceCheckbox.setValue(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE);
        addRow(advancedTable, ignoreLeadingWhiteSpaceCheckbox, "Ignore Leading Whitespace");
    }

    /**
     * Constructor which presets values.
     *
     * @param csvSetting Setting object from which to copy the values.
     */
    public CsvFileInput(ConfigurationSettingCsvFile csvSetting, boolean optional) {
        this(optional);

        this.preselectedFilename = csvSetting.getFileName();

        this.advancedCheckbox.setValue(csvSetting.isAdvanced());
        this.separatorTextbox.setValue("" + csvSetting.getSeparatorChar());
        this.quoteTextbox.setValue("" + csvSetting.getQuoteChar());
        this.escapeTextbox.setValue("" + csvSetting.getEscapeChar());
        this.skiplinesIntegerbox.setValue(csvSetting.getLine());
        this.strictQuotesCheckbox.setValue(csvSetting.isStrictQuotes());
        this.ignoreLeadingWhiteSpaceCheckbox.setValue(csvSetting.isIgnoreLeadingWhiteSpace());
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
     * Fills the dropdown list with the given values
     *
     * @param fileNames the values to put into the dropdown
     * @throws AlgorithmConfigurationException
     */
    public void addToListbox(String[] fileNames) throws AlgorithmConfigurationException {
        int index = 1;                            //start at 1 because index 0 has default ("--") entry
        for (String value : fileNames) {
            String displayName = value.replaceAll(".*(/|\\\\)", "");
            listbox.addItem(displayName, value);
            if (isPreselected(value)) {
                listbox.setSelectedIndex(index);
            }
            index++;
        }
        if (preselectionUnavailable())
            throw new AlgorithmConfigurationException("The CSV file is not available.");
    }

    /**
     * @return true if a preselection was specified but the default item is still selected (usually because the preselected
     * item was not available)
     */
    private boolean preselectionUnavailable() {
        return this.preselectedFilename != null && this.preselectedFilename != "" && this.listbox.getSelectedIndex() == 0;
    }

    /**
     * Checks whether the value parameter corresponds the one that was preselected. The comparison is based on the substring
     * after the last "/", so that "/path/to/inputA.csv" is the same as "inputA.csv"
     * TODO: find a way to make separator system dependent
     *
     * @param value
     * @return true if the
     */
    private boolean isPreselected(String value) {
        if (value == null || this.preselectedFilename == null)
            return false;
        return value.replaceAll(".*(/|\\\\)", "").equals(
                preselectedFilename.replaceAll(".*(/|\\\\)", ""));
    }

    /**
     * Selects the given data source in the first widget. If the dropdowns have not yet been filled with the available
     * values, we save the value and set it when the dropdown is filled.
     *
     * @param csvSetting the data source
     * @throws AlgorithmConfigurationException If the dropdowns are filled but do not containt the desired data source.
     */
    public void selectDataSource(ConfigurationSettingDataSource csvSetting) throws AlgorithmConfigurationException {
        this.preselectedFilename = csvSetting.getValueAsString();

        //if the list  of available files has already been retrieved
        if (this.listbox.getItemCount() > 1) {
            for (int i = 0; i < this.listbox.getItemCount(); i++) {
                if (isPreselected(this.listbox.getItemText(i))) {
                    listbox.setSelectedIndex(i);
                    return;
                }
            }
            throw new AlgorithmConfigurationException("The CSV file is not available.");
        }
    }

    /**
     * Retrieves the current values from the UI and sets them on the given inputParameter
     *
     * @param configSetting the object on which to set the values
     * @return the inputParameter with updated values
     * @throws InputValidationException 
     */
    protected ConfigurationSettingCsvFile setCurrentValues(ConfigurationSettingCsvFile configSetting) throws InputValidationException {
        configSetting.setFileName(this.listbox.getValue(this.listbox.getSelectedIndex()));
        if (configSetting.getFileName().equals("--"))
        	throw new InputValidationException("You must choose a CSV file from the list.");
        
        configSetting.setAdvanced(this.advancedCheckbox.getValue());

        if (configSetting.isAdvanced()) {
            configSetting.setSeparatorChar(StringHelper.getValidatedInput(this.separatorTextbox.getValue()));
            configSetting.setQuoteChar(StringHelper.getValidatedInput(this.quoteTextbox.getValue()));
            configSetting.setEscapeChar(StringHelper.getValidatedInput(this.escapeTextbox.getValue()));
            configSetting.setStrictQuotes(this.strictQuotesCheckbox.getValue());
            configSetting.setIgnoreLeadingWhiteSpace(this.ignoreLeadingWhiteSpaceCheckbox.getValue());
            if (this.skiplinesIntegerbox.getValue() != null)
                configSetting.setLine(this.skiplinesIntegerbox.getValue());
            else
                configSetting.setLine(0);
        }

        return configSetting;
    }

    /**
     * Create the CheckBox that triggers the display/hiding of advanced CSV configuration parameters
     *
     * @return the CheckBox with the mentioned behavior
     */
    protected CheckBox createAdvancedCheckbox() {
        CheckBox checkbox = new CheckBox("Use Advanced Configuration");
        checkbox.setValue(false); //TODO ok to always assume not-advanced on creation?
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
        return listbox;
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

    /**
     * @return a new ConfigurationSetting object with the current user input
     * @throws InputValidationException 
     */
    public ConfigurationSettingCsvFile getValuesAsSettings() throws InputValidationException {
        ConfigurationSettingCsvFile setting = new ConfigurationSettingCsvFile();
        return setCurrentValues(setting);
    }


}
