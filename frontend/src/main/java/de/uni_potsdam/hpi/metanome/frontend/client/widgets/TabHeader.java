package de.uni_potsdam.hpi.metanome.frontend.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.tabs.ResultsTab;

public class TabHeader extends HorizontalPanel {
	
	private final Panel tab;
	private final TabLayoutPanel tabPanel;

	public TabHeader(String algorithmName, ResultsTab resultsTab, TabLayoutPanel parent) {
		this.tab = resultsTab;
		this.tabPanel = parent;
		
		this.setSpacing(3);
		this.add(new Label(algorithmName));
		
		Button button = new Button("x");
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				tabPanel.remove(tab);
				
			}
		});
		this.add(button);
	}
}
