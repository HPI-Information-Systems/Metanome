/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.RunConfigurationPage;

import java.util.LinkedList;
import java.util.List;

public class ParameterTable extends FlexTable {

	private List<InputParameterWidget> childWidgets = new LinkedList<>();
	private List<InputParameterDataSourceWidget> dataSourceChildWidgets = new LinkedList<>();
	private Button executeButton;
	private TabWrapper errorReceiver;

	/**
	 * Creates a ParameterTable for user input for the given parameters.
	 * Prompt and type specific input field are created for each parameter,
	 * and a button added at the bottom that triggers algorithm execution.
	 *
	 * @param paramList         the list of parameters asked for by the algorithm.
	 * @param primaryDataSource
	 * @param errorReceiver
	 */
	public ParameterTable(List<ConfigurationSpecification> paramList, ConfigurationSettingDataSource primaryDataSource, TabWrapper errorReceiver) {
		super();
		this.errorReceiver = errorReceiver;

		int i = 0;
		for (ConfigurationSpecification param : paramList) {
			this.setText(i, 0, param.getIdentifier());

			InputParameterWidget currentWidget = null;
			currentWidget = WidgetFactory.buildWidget(param);
			this.setWidget(i, 1, currentWidget);
			if (currentWidget.isDataSource()) {
				InputParameterDataSourceWidget dataSourceWidget = (InputParameterDataSourceWidget) currentWidget;
				if (dataSourceWidget.accepts(primaryDataSource))
					try {
						dataSourceWidget.setDataSource(primaryDataSource);
					} catch (AlgorithmConfigurationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				this.dataSourceChildWidgets.add(dataSourceWidget);
			} else
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
	public void submit() {
		try {
			List<ConfigurationSpecification> parameters = getConfigurationSpecificationsWithValues();
			List<ConfigurationSpecification> dataSources = getConfigurationSpecificationDataSourcesWithValues();
			getAlgorithmTab().callExecutionService(parameters, dataSources);
		} catch (InputValidationException e) {
			this.errorReceiver.clearErrors();
			this.errorReceiver.addError(e.getMessage());
		}
	}

	/**
	 * Iterates over the child widgets that represent data sources and retrieves their user input.
	 *
	 * @return The list of {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterDataSourceWidget}s of this ParameterTable with their user-set values.
	 * @throws InputValidationException
	 */
	public List<ConfigurationSpecification> getConfigurationSpecificationDataSourcesWithValues() throws InputValidationException {
		LinkedList<ConfigurationSpecification> parameterList = new LinkedList<>();
		for (InputParameterDataSourceWidget childWidget : this.dataSourceChildWidgets) {
			parameterList.add(childWidget.getUpdatedSpecification());
		}
		return parameterList;
	}

	/**
	 * Iterates over the child widgets and retrieves their user input.
	 *
	 * @return The list of ConfigurationSpecifications of this ParameterTable with their user-set values.
	 * @throws InputValidationException
	 */
	public List<ConfigurationSpecification> getConfigurationSpecificationsWithValues() throws InputValidationException {
		LinkedList<ConfigurationSpecification> parameterList = new LinkedList<>();
		for (InputParameterWidget childWidget : this.childWidgets) {
			parameterList.add(childWidget.getUpdatedSpecification());
		}
		return parameterList;
	}

	/**
	 * Gives access to this ParameterTable's {@link InputParameterWidget} child widget whose underlying
	 * {@link ConfigurationSpecification} has the given identifier.
	 *
	 * @param identifier The identifier of the ConfigurationSpecification of the wanted widget.
	 * @return This parameter's child widgets that corresponds to the given identifier,
	 * or null if such a child does not exist.
	 */
	public InputParameterWidget getInputParameterWidget(String identifier) {
		for (InputParameterWidget w : this.childWidgets) {
			if (w.getSpecification().getIdentifier().equals(identifier))
				return w;
		}
		for (InputParameterWidget w : this.dataSourceChildWidgets) {
			if (w.getSpecification().getIdentifier().equals(identifier))
				return w;
		}
		return null;
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
