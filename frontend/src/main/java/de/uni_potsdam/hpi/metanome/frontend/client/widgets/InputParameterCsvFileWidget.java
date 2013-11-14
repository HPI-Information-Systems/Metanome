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
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataServiceAsync;

public class InputParameterCsvFileWidget extends VerticalPanel implements InputParameterWidget {

	private InputParameterCsvFile inputParameter;
	/** Dropdown menu for choosing a CSV file */
	protected ListBox listbox;
	protected CheckBox advancedCheckbox;
	protected FlexTable advancedPanel;
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
		
		advancedPanel = new FlexTable();
		advancedPanel.setVisible(false);
		this.add(advancedPanel);
		
		separatorTextbox = getNewOneCharTextbox();
		advancedPanel.setText(0,0,"Separator Character");
		advancedPanel.setWidget(0,1,separatorTextbox);
		
		quoteTextbox = getNewOneCharTextbox();
		advancedPanel.setText(1,0,"Quote Character");
		advancedPanel.setWidget(1,1,quoteTextbox);
		
		escapeTextbox = getNewOneCharTextbox();
		advancedPanel.setText(2,0,"Escape Character");
		advancedPanel.setWidget(2,1,escapeTextbox);
		
		lineIntegerbox = new IntegerBox();
		lineIntegerbox.setWidth("5em");
		advancedPanel.setText(3,0,"Line");
		advancedPanel.setWidget(3,1,lineIntegerbox);
		
		strictQuotesCheckbox = new CheckBox();
		advancedPanel.setText(4,0,"Strict Quotes");
		advancedPanel.setWidget(4,1,strictQuotesCheckbox);
		
		ignoreLeadingWhiteSpaceCheckbox = new CheckBox();
		advancedPanel.setText(5,0,"Ignore Leading Whitespace");
		advancedPanel.setWidget(5,1,ignoreLeadingWhiteSpaceCheckbox);
	}
	
	private TextBox getNewOneCharTextbox(){
		TextBox textbox = new TextBox();
		textbox.setMaxLength(1);
		textbox.setWidth("2em");
		return textbox;
	}

	protected void setCurrentValues(InputParameterCsvFile inputParameter2) {
		inputParameter.setFileNameValue(this.listbox.getValue(this.listbox.getSelectedIndex()));
		if (this.advancedCheckbox.getValue()){
			inputParameter.setSeparatorChar(this.separatorTextbox.getValue().charAt(0));
			inputParameter.setQuoteChar(this.quoteTextbox.getValue().charAt(0));
			inputParameter.setEscapeChar(this.escapeTextbox.getValue().charAt(0));
			inputParameter.setLine(this.lineIntegerbox.getValue());
			inputParameter.setStrictQuotes(this.strictQuotesCheckbox.getValue());
			inputParameter.setIgnoreLeadingWhiteSpace(this.ignoreLeadingWhiteSpaceCheckbox.getValue());
		}
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
		this.advancedPanel.setVisible(this.advancedCheckbox.getValue());
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
		this.setCurrentValues(this.inputParameter);
		return this.inputParameter;
	}

}
