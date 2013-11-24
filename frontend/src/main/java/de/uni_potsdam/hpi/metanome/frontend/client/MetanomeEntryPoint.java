package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class MetanomeEntryPoint implements EntryPoint {

	@Override
	public void onModuleLoad() {
		RootPanel.get().setHeight("100%");
		RootPanel.get().add(new BasePage());		
	}

}
