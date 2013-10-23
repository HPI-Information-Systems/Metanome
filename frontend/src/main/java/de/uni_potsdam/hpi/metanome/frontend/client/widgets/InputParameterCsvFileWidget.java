package de.uni_potsdam.hpi.metanome.frontend.client.widgets;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataServiceAsync;

public class InputParameterCsvFileWidget extends Composite implements InputParameterWidget {

	private InputParameterCsvFile inputParameter;
	
	/** Dropdown menu for choosing a CSV file */
	protected ListBox listbox;

	public InputParameterCsvFileWidget(InputParameterCsvFile inputParameter) {
		super();
		this.inputParameter = inputParameter;
		
		listbox = createListbox();
		
		// Place the check above the text box using a vertical panel.
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(listbox);
		
		listbox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				setCurrentFileNameValue();
			}
		});

		// All composites must call initWidget() in their constructors.
		initWidget(panel);
	}

	/**
	 * Sets the inputParameter's fileName value to the item currently selected in listbox.
	 */
	protected void setCurrentFileNameValue() {
		inputParameter.setFileNameValue(listbox.getValue(listbox.getSelectedIndex()));
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


	@Override
	public InputParameter getInputParameter() {
		return inputParameter;
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
}
