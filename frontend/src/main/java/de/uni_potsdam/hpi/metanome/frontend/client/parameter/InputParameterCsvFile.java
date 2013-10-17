package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.client.ui.Widget;

public class InputParameterCsvFile extends InputParameter {

	private static final long serialVersionUID = -4018145396259206308L;

	private String valueFileName;
	
	public InputParameterCsvFile(){
		super();
	}

	public InputParameterCsvFile(String identifier){
		super(identifier);
	}
	
	@Override
	public void setValue(Object value) {
		this.setValue((String) value); 
	}
	
	public void setValue(String value) {
		this.valueFileName = value;
	}

	@Override
	public Object getValue() {
		return valueFileName;
	}

	@Override
	public Widget getWidget() {
		ValueListBox<String> listbox = new ValueListBox<String>(new Renderer<String>(){

			@Override
			public String render(String object) {
				return object;
			}

			@Override
			public void render(String object, Appendable appendable)
					throws IOException {
				appendable.append(object);
			}
			
		});
		
		//unselectable default entry
		listbox.setValue("--");
		listbox.getElement().getFirstChildElement().setAttribute("disabled", "disabled");
		ArrayList<String> values = new ArrayList<String>();
		values.add("some CSV file found in designated folder");
		listbox.setAcceptableValues(values);
		
		return listbox;
	}

}
