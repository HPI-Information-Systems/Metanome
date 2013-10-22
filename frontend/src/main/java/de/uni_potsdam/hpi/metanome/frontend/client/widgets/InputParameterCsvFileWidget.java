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
	
	private ListBox listbox;

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


	protected void setCurrentFileNameValue() {
		inputParameter.setFileNameValue(listbox.getValue(listbox.getSelectedIndex()));
	}

	private ListBox createListbox() {
		ListBox listbox = new ListBox();
		
		//unselectable default entry
		listbox.addItem("--");
		listbox.getElement().getFirstChildElement().setAttribute("disabled", "disabled");
		//other entries
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


	@Override
	public InputParameter getInputParameter() {
		return inputParameter;
	}
}
