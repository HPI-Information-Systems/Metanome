package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.ListBox;

public class MetanomeListBox extends ListBox implements TakesValue<String>{

	@Override
	public String getValue() {
		return this.getValue(this.getSelectedIndex());
	}

	@Override
	public void setValue(String value) {
		for (int i=0; i<this.getItemCount(); i++){
			if (this.getValue(i) == value)
				this.setSelectedIndex(i);
		}
	}
}
