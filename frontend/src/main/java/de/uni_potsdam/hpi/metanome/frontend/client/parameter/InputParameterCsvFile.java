package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterCsvFileWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.InputParameterWidget;

public class InputParameterCsvFile extends InputParameter {
	private static final long serialVersionUID = -4018145396259206308L;

	private String filePath;
	
	public InputParameterCsvFile(){
		super();
	}

	public InputParameterCsvFile(String identifier){
		super(identifier);
	}
	
	public void setFileNameValue(String value) {
		this.filePath = value;
	}

	@Override
	public String getValue() {
		return filePath;
	}

	public boolean isAdvanced() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public InputParameterWidget createWrappingWidget() {
		return new InputParameterCsvFileWidget(this);
	}

}
