package de.uni_potsdam.hpi.metanome.frontend.client.datasources;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Data Sources page is the Tab that lists all previously defined data sources (CSV files, DB connections) 
 * and links to actions on them. It also allows to define new data sources.
 * 
 * @author Claudia
 */
public class DataSourcesPage extends VerticalPanel {

	public DataSourcesPage() {
		Label temporaryContent = new Label();
		temporaryContent.setText("As data sources, you can configure any database connection in the Run Configurations,"
				+ "or choose from any CSV-files in the designated folder.");
		this.add(temporaryContent);
	}
}
