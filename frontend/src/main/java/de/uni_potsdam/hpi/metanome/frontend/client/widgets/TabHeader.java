package de.uni_potsdam.hpi.metanome.frontend.client.widgets;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.tabs.ResultsTab;

/**
 * Widget to be put into the tab bars of result tabs. Includes the algorithm name as well
 * as a close button.
 * 
 * @author Claudia
 */
public class TabHeader extends HorizontalPanel {
	
	private final Panel tab;
	private final TabLayoutPanel tabPanel;

	/**
	 * Constructor.
	 * 
	 * @param algorithmName	the name of the algorithm or whatever should be displayed as tab title
	 * @param resultsTab	the panel holding the corresponding tab's contents
	 * @param parent		the TabLayoutPanel where resultsTab will be added to
	 */
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
