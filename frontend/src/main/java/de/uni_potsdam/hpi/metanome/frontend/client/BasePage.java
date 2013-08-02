package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.user.client.ui.TabPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.tabs.FunctionalDependencyTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.InclusionDependencyTab;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.UniqueColumnCombinationTab;

/**
 * Overall Application page that has one tab for each algorithm type.
 * Should be added to RootPanel
 */
public class BasePage extends TabPanel {
  
  public BasePage() {
	  this.add(new UniqueColumnCombinationTab(), "Unique Column Combinations");
	  this.add(new InclusionDependencyTab(), "Inclusion Dependencies");
	  this.add(new FunctionalDependencyTab(), "Functional Dependencies");
  }

}
