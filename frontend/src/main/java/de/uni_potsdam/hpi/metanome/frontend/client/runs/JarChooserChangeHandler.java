package de.uni_potsdam.hpi.metanome.frontend.client.runs;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;


public class JarChooserChangeHandler implements ChangeHandler {

	@Override
	public void onChange(ChangeEvent event) {
		JarChooser jarChooser = (JarChooser) ((ListBox) event.getSource()).getParent();	    		
    	jarChooser.submit();		
	}
}
