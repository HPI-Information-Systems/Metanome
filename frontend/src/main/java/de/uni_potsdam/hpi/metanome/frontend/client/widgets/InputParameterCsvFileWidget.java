package de.uni_potsdam.hpi.metanome.frontend.client.widgets;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.helpers.StringHelper;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataServiceAsync;

public class InputParameterCsvFileWidget extends VerticalPanel implements InputParameterWidget {

	/** Corresponding inputParameter, where the value is going to be written */
	private InputParameterCsvFile inputParameter;
	
	/** Dropdown menu for choosing a CSV file */
	protected ListBox listbox;
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
		addRow(advancedTable, separatorTextbox, "Separator Character", 0);
		
		quoteTextbox = getNewOneCharTextbox();
		addRow(advancedTable, quoteTextbox, "Quote Character", 1);
		
		escapeTextbox = getNewOneCharTextbox();
		addRow(advancedTable, escapeTextbox, "Escape Character", 2);
		
		lineIntegerbox = new IntegerBox();
		lineIntegerbox.setWidth("5em");
		addRow(advancedTable, lineIntegerbox, "Line", 3);
		
		strictQuotesCheckbox = new CheckBox();
		addRow(advancedTable, strictQuotesCheckbox, "Strict Quotes", 4);
		
		ignoreLeadingWhiteSpaceCheckbox = new CheckBox();
		addRow(advancedTable, ignoreLeadingWhiteSpaceCheckbox, "Ignore Leading Whitespace", 5);
	}

	protected void addRow(FlexTable table, Widget inputWidget, String name, int row) {
		table.setText(row, 0, name);
		table.setWidget(row, 1, inputWidget);
	}
	
	private TextBox getNewOneCharTextbox(){
		TextBox textbox = new TextBox();
		textbox.setMaxLength(2);
		textbox.setWidth("2em");
		return textbox;
	}

	protected InputParameterCsvFile setCurrentValues(InputParameterCsvFile inputParameter) {
		inputParameter.setFileNameValue(this.listbox.getValue(this.listbox.getSelectedIndex()));
		inputParameter.setAdvanced(this.advancedCheckbox.getValue());
		
		if (inputParameter.isAdvanced()){
			inputParameter.setSeparatorChar(StringHelper.getFirstCharFromInput(this.separatorTextbox.getValue()));
			inputParameter.setQuoteChar(StringHelper.getFirstCharFromInput(this.quoteTextbox.getValue()));
			inputParameter.setEscapeChar(StringHelper.getFirstCharFromInput(this.escapeTextbox.getValue())	);
			inputParameter.setStrictQuotes(this.strictQuotesCheckbox.getValue());
			inputParameter.setIgnoreLeadingWhiteSpace(this.ignoreLeadingWhiteSpaceCheckbox.getValue());			
			if (this.lineIntegerbox.getValue() != null)
				inputParameter.setLine(this.lineIntegerbox.getValue());
			else
				inputParameter.setLine(0);
		}
		
		return inputParameter;
	}

	protected CheckBox createAdvancedCheckbox() {
		CheckBox checkbox = new CheckBox("Use Advanced Configuration");
		checkbox.setValue(this.inputParameter.isAdvanced());
		checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				setInputParameterAdvanced();
			}
		});
		
		return checkbox;
	}
	
	/**
	 * Finds all available CSV files and adds them to a dropdown menu with an empty
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
				for (String value : result){
					String displayName = value.substring(value.lastIndexOf("/") + 1);
					listbox.addItem(displayName, value);
				}
			}
		};
		
		InputDataServiceAsync service = GWT.create(InputDataService.class);  
		service.listCsvInputFiles(callback);
	}
	
	protected void setInputParameterAdvanced() {
		this.advancedTable.setVisible(this.advancedCheckbox.getValue());
	}
	
	/** 
	 * @return a list of the values displayed in the listbox, one String per entry
	 */
	public String[] getListboxItemTexts() {
		String[] itemTexts = new String[listbox.getItemCount()];
		for(int i=0; i<listbox.getItemCount(); i++){
			itemTexts[i] = listbox.getItemText(i);
		}
		return itemTexts;
	}
	
	@Override
	public InputParameterCsvFile getInputParameter() {
		return this.setCurrentValues(this.inputParameter);
	}

}
