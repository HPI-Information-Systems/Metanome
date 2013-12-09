package de.uni_potsdam.hpi.metanome.frontend.client.widgets;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ResultTable extends VerticalPanel {

	private FlexTable table;
	
	public ResultTable(String title) {
		this.add(new Label(title));
		
		this.table = new FlexTable();
		this.table.setBorderWidth(1);
		this.add(table);
		
		this.setSpacing(5);
	}
	
	public void setText(int row, int column, String text) {
		this.table.setText(row, column, text);
	}
	
	public int getRowCount() {
		return this.table.getRowCount();
	}
}
