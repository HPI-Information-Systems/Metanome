package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.MetanomeListBox;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataServiceAsync;

public class InputParameterCsvFile extends InputParameter {
	private static final long serialVersionUID = -4018145396259206308L;

	private String filePath;
	
	public InputParameterCsvFile(){
		super();
	}

	public InputParameterCsvFile(String identifier){
		super(identifier);
	}
	
	@Override
	public void setValue(Object value) {
		this.setValue((String) value); 
	}
	
	public void setValue(String value) {
		this.filePath = value;
	}

	@Override
	public String getValue() {
		return filePath;
	}

	@Override
	public Widget getWidget() {
		ListBox listbox = new MetanomeListBox();
		
		//unselectable default entry
		listbox.addItem("--");
		listbox.getElement().getFirstChildElement().setAttribute("disabled", "disabled");
		addAvailableCsvsToListbox(listbox);
		
		return listbox;
	}

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

	public boolean isAdvanced() {
		// TODO Auto-generated method stub
		return false;
	}

}
