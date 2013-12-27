package de.uni_potsdam.hpi.metanome.frontend.client.datasources;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Data Sources page is the Tab that lists all previously defined data sources (CSV files, DB connections) 
 * and links to actions on them. It also allows to define new data sources.
 * 
 * @author Claudia
 */
public class DataSourcesPage extends VerticalPanel {

	private FlexTable dbConnectionsList;
	private FlexTable csvFilesList;

	public DataSourcesPage() {		
		createDataSourcesList();
		
		Label temporaryContent = new Label();
		temporaryContent.setText("As data sources, you can configure any database connection in the Run Configurations,"
				+ "or choose from any CSV-files in the designated folder.");
		this.add(temporaryContent);
	}

	/**
	 * This method triggers retrieval of all available data sources and returns a widget with a list of them.
	 * @return
	 */
	private void createDataSourcesList() {
		this.add(new HTML("<b>CSV Files</b>"));
		csvFilesList = new FlexTable();
		this.add(csvFilesList);
		listCsvFiles();
		
		this.add(new HTML("<hr><b>Database Connections</b>"));
		dbConnectionsList = new FlexTable();
		this.add(dbConnectionsList);
		listDbConnections();

		this.add(new HTML("<hr>"));
	}

	private void listCsvFiles() {
		// TODO Auto-generated method stub
		
	}

	private void listDbConnections() {
		// TODO Auto-generated method stub
		
	}
}
