/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithms.testing.example_fd_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.algorithm_types.DatabaseConnectionParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ListBoxParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.CheckBoxParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementCheckBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.metanome.algorithm_integration.results.FunctionalDependency;

import java.util.ArrayList;
import java.util.List;

public class ExampleAlgorithm
    implements FunctionalDependencyAlgorithm, StringParameterAlgorithm, FileInputParameterAlgorithm,
               CheckBoxParameterAlgorithm, DatabaseConnectionParameterAlgorithm, ListBoxParameterAlgorithm {

  public final static String LISTBOX_IDENTIFIER = "column names";
  public final static String CHECKBOX_IDENTIFIER = "column names";
  public final static String STRING_IDENTIFIER = "pathToOutputFile";
  public final static String CSVFILE_IDENTIFIER = "input file";
  public final static String DATABASE_IDENTIFIER = "DB-connection";
  protected String path = null;
  protected String selectedColumn = null;
  protected DatabaseConnectionGenerator inputGenerator;
  protected FunctionalDependencyResultReceiver resultReceiver;

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurationRequirement = new ArrayList<>();

    ConfigurationRequirementString requirementString =
        new ConfigurationRequirementString(STRING_IDENTIFIER);
    ConfigurationRequirementFileInput requirementFileInput =
        new ConfigurationRequirementFileInput(CSVFILE_IDENTIFIER);
    ConfigurationRequirementDatabaseConnection requirementDatabaseConnection =
        new ConfigurationRequirementDatabaseConnection(DATABASE_IDENTIFIER);

    ArrayList<String> listBoxValues = new ArrayList<>();
    listBoxValues.add("column 1");
    listBoxValues.add("column 2");
    listBoxValues.add("column 3");
    ConfigurationRequirementListBox
        requirementListBox =
        new ConfigurationRequirementListBox(LISTBOX_IDENTIFIER, listBoxValues, 1);


    String[] checkBoxValues = new String[3];
    checkBoxValues[0] = "column 1";
    checkBoxValues[1] = "column 2";
    checkBoxValues[2] = "column 3";
    ConfigurationRequirementCheckBox
            requirementCheckBox =
            new ConfigurationRequirementCheckBox(CHECKBOX_IDENTIFIER, checkBoxValues, 1);

    requirementString.setRequired(false);
    requirementFileInput.setRequired(false);
    requirementDatabaseConnection.setRequired(false);
    requirementListBox.setRequired(false);
    requirementCheckBox.setRequired(false);

    configurationRequirement.add(requirementString);
    configurationRequirement.add(requirementListBox);
    configurationRequirement.add(requirementCheckBox);
    configurationRequirement.add(requirementFileInput);
    configurationRequirement.add(requirementDatabaseConnection);

    return configurationRequirement;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    try {
      resultReceiver.receiveResult(
          new FunctionalDependency(
              new ColumnCombination(
                  new ColumnIdentifier("WDC_planets.csv", "Name"),
                  new ColumnIdentifier("WDC_planets.csv", "Type")),
              new ColumnIdentifier("WDC_planets.csv", "Mass")
          )
      );
    } catch (CouldNotReceiveResultException | ColumnNameMismatchException e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void setResultReceiver(FunctionalDependencyResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }

  @Override
  public void setStringConfigurationValue(String identifier, String... values)
      throws AlgorithmConfigurationException {
    if (!identifier.equals(STRING_IDENTIFIER)) {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setFileInputConfigurationValue(String identifier,
                                             FileInputGenerator... values)
      throws AlgorithmConfigurationException {
    if (!identifier.equals(CSVFILE_IDENTIFIER)) {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setCheckBoxConfigurationValue(String identifier, String[]... selectedValues)
          throws AlgorithmConfigurationException {
    if (!identifier.equals(CHECKBOX_IDENTIFIER)) {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setListBoxConfigurationValue(String identifier, String... selectedValues)
          throws AlgorithmConfigurationException {
    if (!identifier.equals(LISTBOX_IDENTIFIER)) {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setDatabaseConnectionGeneratorConfigurationValue(String identifier,
                                                               DatabaseConnectionGenerator... values)
      throws AlgorithmConfigurationException {

    if (!identifier.equals(DATABASE_IDENTIFIER)) {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm for functional dependencies.";
  }
}
