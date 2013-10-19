package de.uni_potsdam.hpi.metanome.frontend.client;

import java.io.Serializable;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.ListBox;

public class MetanomeListBox extends ListBox implements TakesValue<String>, Serializable{

	private static final long serialVersionUID = -8848491964309993192L;

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
