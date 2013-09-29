package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.tabs.AlgorithmTab;

public class ParameterTable extends FlexTable {

	private List<InputParameter> parameters;
	private Button executeButton;

	public ParameterTable(List<InputParameter> requiredParams) {
		super();
		this.parameters = requiredParams;		
		
		int i = 0;
		for (InputParameter param : this.parameters) {
			this.setText(i, 0, param.getIdentifier());
			this.setWidget(i, 1, param.getWidget());
			i++;
		}
		
		this.executeButton = new Button("Run");
		this.executeButton.addClickHandler(new ParameterTableSubmitHandler());
		
		this.setWidget(i, 0, executeButton);
	}
	
	/**
	 * When parameter values are submitted, there values are set and used to call
	 * the execution service corresponding to the current tab.
	 */
	public void submit(){
		setParameterValues(this.parameters);
		getAlgorithmTab().callExecutionService(this.parameters);
	}

	/**
	 * TODO docs
	 * @param parameters 
	 */
	private void setParameterValues(List<InputParameter> params) {
		int i = 0;
		for (InputParameter param : params) {
			Widget widget = this.getWidget(i, 1);
			Object value = null;
			//TODO make ParameterInput interface to clean up this code
			if (widget instanceof TextBox){
				value = ((TextBox) widget).getValue();
			} else if (widget instanceof CheckBox){
				value = ((CheckBox) widget).getValue();
			} else if (widget instanceof IntegerBox){
				value = ((IntegerBox) widget).getValue();
			}

			param.setValue(value);
			i++;
		}
	}

	/**
	 * The AlgorithmTabs implement algorithm type specific methods, which can
	 * be called via the AlgorithmTab's interface.
	 * 
	 * @return the parent AlgorithmTab 
	 */
	private AlgorithmTab getAlgorithmTab() {
		return (AlgorithmTab) this.getParent();
	}

}
