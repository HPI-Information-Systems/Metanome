package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;

import de.uni_potsdam.hpi.metanome.frontend.client.runs.RunConfigurationPage;

public class ParameterTable extends FlexTable {

	private List<InputParameterWidget> childWidgets = new LinkedList<InputParameterWidget>();
	private List<InputParameterDataSourceWidget> dataSourceWidgets = new LinkedList<InputParameterDataSourceWidget>();
	private Button executeButton;

	/**
	 * Creates a ParameterTable for user input for the given parameters. 
	 * Prompt and type specific input field are created for each parameter,
	 * and a button added at the bottom that triggers algorithm execution.
	 * 
	 * @param requiredParams the list of parameters asked for by the algorithm.
	 * @param primaryDataSource 
	 */
	public ParameterTable(List<InputParameter> requiredParams, InputParameterDataSource primaryDataSource) {
		super();	
		
		int i = 0;
		for (InputParameter param : requiredParams) {
			this.setText(i, 0, param.getIdentifier());
			InputParameterWidget currentWidget = param.createWrappingWidget();
			this.setWidget(i, 1, currentWidget);
			if (currentWidget instanceof InputParameterDataSourceWidget) {
				InputParameterDataSourceWidget dataSourceWidget = (InputParameterDataSourceWidget) currentWidget;
				if (primaryDataSource != null && 
						currentWidget.getInputParameter().getClass().equals(primaryDataSource.getClass()))
					dataSourceWidget.setInputParameter(primaryDataSource);
				this.dataSourceWidgets.add(dataSourceWidget);
			}
			else
				this.childWidgets.add(currentWidget);
			i++;
		}
		
		this.executeButton = new Button("Run");
		this.executeButton.addClickHandler(new ParameterTableSubmitHandler());
		
		this.setWidget(i, 0, executeButton);
	}
	
	/**
	 * When parameter values are submitted, their values are set and used to call
	 * the execution service corresponding to the current tab.
	 */
	public void submit(){
		List<InputParameter> parameters = getInputParametersWithValues();
		List<InputParameterDataSource> dataSources = getInputParameterDataSourcesWithValues();
		getAlgorithmTab().callExecutionService(parameters, dataSources);
	}

	/**
	 * TODO docs
	 * @return
	 */
	public List<InputParameterDataSource> getInputParameterDataSourcesWithValues() {
		LinkedList<InputParameterDataSource> parameterList = new LinkedList<InputParameterDataSource>();
		for (InputParameterDataSourceWidget childWidget : this.dataSourceWidgets){
			parameterList.add(childWidget.getInputParameter());
		}
		return parameterList;
	}

	/**
	 * Iterates over the child widgets and retrieves their user input.
	 * @return The list of InputParameters of this ParameterTable with their user-set values.
	 */
	public List<InputParameter> getInputParametersWithValues() {
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
