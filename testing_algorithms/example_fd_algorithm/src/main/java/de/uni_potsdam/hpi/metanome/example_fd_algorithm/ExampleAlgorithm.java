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

package de.uni_potsdam.hpi.metanome.example_fd_algorithm;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.ListBoxParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.*;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.FileInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;

import java.util.ArrayList;
import java.util.List;

public class ExampleAlgorithm implements FunctionalDependencyAlgorithm, StringParameterAlgorithm, FileInputParameterAlgorithm, ListBoxParameterAlgorithm {

	public final static String LISTBOX_IDENTIFIER = "column names";
	public final static String STRING_IDENTIFIER = "pathToOutputFile";
	public final static String CSVFILE_IDENTIFIER = "input file";
	public final static String SQL_IDENTIFIER = "DB-connection";
	protected String path = null;
	protected String selectedColumn = null;
	protected FunctionalDependencyResultReceiver resultReceiver;

	@Override
	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List<ConfigurationSpecification> configurationSpecification = new ArrayList<>();

		configurationSpecification.add(new ConfigurationSpecificationString(STRING_IDENTIFIER));
		configurationSpecification.add(new ConfigurationSpecificationCsvFile(CSVFILE_IDENTIFIER));
		configurationSpecification.add(new ConfigurationSpecificationSqlIterator(SQL_IDENTIFIER));

		ArrayList<String> listBoxValues = new ArrayList<>();
		listBoxValues.add("column 1");
		listBoxValues.add("column 2");
		listBoxValues.add("column 3");
		ConfigurationSpecificationListBox specificationListBox = new ConfigurationSpecificationListBox(LISTBOX_IDENTIFIER, listBoxValues, 1);
		configurationSpecification.add(specificationListBox);

		return configurationSpecification;
	}

	@Override
	public void execute() {
		if (path != null && selectedColumn != null) {
			try {
				resultReceiver.receiveResult(
						new FunctionalDependency(
								new ColumnCombination(
										new ColumnIdentifier("table1", "column1"),
										new ColumnIdentifier("table1", "column2")),
								new ColumnIdentifier("table1", "column5")
						)
				);
			} catch (CouldNotReceiveResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setResultReceiver(FunctionalDependencyResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	@Override
	public void setStringConfigurationValue(String identifier, String... values) throws AlgorithmConfigurationException {
		if ((identifier.equals(STRING_IDENTIFIER)) && (values.length == 1)) {
			path = values[0];
		} else {
			throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
		}
	}

	@Override
	public void setFileInputConfigurationValue(String identifier,
											   FileInputGenerator... values)
			throws AlgorithmConfigurationException {
		if (identifier.equals(CSVFILE_IDENTIFIER)) {
			System.out.println("Input file is not being set on algorithm.");
		}
	}

	@Override
	public void setListBoxConfigurationValue(String identifier, String... selectedValues) throws AlgorithmConfigurationException {
		if ((identifier.equals(LISTBOX_IDENTIFIER)) && (selectedValues.length == 1)) {
			selectedColumn = selectedValues[0];
		} else {
			throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
		}
	}

}
