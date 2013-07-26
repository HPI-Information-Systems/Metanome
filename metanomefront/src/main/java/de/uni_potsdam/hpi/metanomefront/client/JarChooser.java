package de.uni_potsdam.hpi.metanomefront.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

public class JarChooser extends HorizontalPanel {

	private Label label;
	private ListBox listbox;
	private Button button;
	
	public JarChooser(String[] jarFilenames){
		super();
		
		this.label = new Label("Choose your algorithm:");
		this.add(label);
		
		this.listbox = new ListBox();
		for (String filename : jarFilenames){
			this.listbox.addItem(filename);
		}
		this.add(listbox);
		
		this.button = new Button("OK", new ClickHandler() {
		      public void onClick(ClickEvent event) {
		        //call backend to get Algorithm configuration specification
		      }
		    });
		this.add(button);
	}

	public int getListItemCount() {
		return this.listbox.getItemCount();
	}
}
