package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;


public class JarChooserClickHandler implements ClickHandler {

	 public void onClick(ClickEvent event) {
	    	JarChooser jarChooser = (JarChooser) ((Button) event.getSource()).getParent();	    		
	    	jarChooser.submit();
	 }
}
