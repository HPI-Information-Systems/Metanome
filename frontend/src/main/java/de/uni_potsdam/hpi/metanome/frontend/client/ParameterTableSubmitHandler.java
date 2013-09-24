package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;

public class ParameterTableSubmitHandler implements ClickHandler {

	@Override
	public void onClick(ClickEvent event) {
		ParameterTable paramTable = (ParameterTable) ((Button) event.getSource()).getParent();	    		
    	paramTable.submit();	
	}

}
