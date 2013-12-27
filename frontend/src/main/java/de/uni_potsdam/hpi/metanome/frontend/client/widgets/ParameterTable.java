package de.uni_potsdam.hpi.metanome.frontend.client.widgets;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.ParameterTableSubmitHandler;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.RunConfigurationPage;

public class ParameterTable extends FlexTable {

	private List<InputParameterWidget> childWidgets = new LinkedList<InputParameterWidget>();
	private Button executeButton;

	/**
	 * Creates a ParameterTable for user input for the given parameters. 
	 * Prompt and type specific input field are created for each parameter,
	 * and a button added at the bottom that triggers algorithm execution.
	 * 
	 * @param requiredParams the list of parameters asked for by the algorithm.
	 */
	public ParameterTable(List<InputParameter> requiredParams) {
		super();	
		
		int i = 0;
		for (InputParameter param : requiredParams) {
			this.setText(i, 0, param.getIdentifier());
			InputParameterWidget currentWidget = param.createWrappingWidget();
			this.setWidget(i, 1, currentWidget);
			this.childWidgets.add(currentWidget);
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
		List<InputParameter> parameters = getInputParametersFromChildren();
		getAlgorithmTab().callExecutionService(parameters);
	}

	public List<InputParameter> getInputParametersFromChildren() {
		LinkedList<InputParameter> parameterList = new LinkedList<InputParameter>();
		for (InputParameterWidget childWidget : this.childWidgets){
			parameterList.add(childWidget.getInputParameter());
		}
		return parameterList;
	}


	/**
	 * The AlgorithmTabs implement algorithm type specific methods, which can
	 * be called via the AlgorithmTab's interface.
	 * 
	 * @return the parent AlgorithmTab 
	 */
	private RunConfigurationPage getAlgorithmTab() {
		return (RunConfigurationPage) this.getParent();
	}

}
