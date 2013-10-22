package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;

import de.uni_potsdam.hpi.metanome.frontend.client.widgets.ParameterTable;

public class ParameterTableSubmitHandler implements ClickHandler {

	@Override
	public void onClick(ClickEvent event) {
		ParameterTable paramTable = (ParameterTable) ((Button) event.getSource()).getParent();	    		
    	paramTable.submit();	
	}

}
