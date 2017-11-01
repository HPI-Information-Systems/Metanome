/**
 * Copyright 2017 by Metanome Project
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
package de.metanome.algorithms.testing.example_dc_algorithm;

import java.util.ArrayList;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.Operator;
import de.metanome.algorithm_integration.Predicate;
import de.metanome.algorithm_integration.PredicateConstant;
import de.metanome.algorithm_integration.PredicateVariable;
import de.metanome.algorithm_integration.algorithm_types.DatabaseConnectionParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.DenialConstraintAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.input.DatabaseConnectionGenerator;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.DenialConstraintResultReceiver;
import de.metanome.algorithm_integration.results.DenialConstraint;

public class ExampleAlgorithm implements DenialConstraintAlgorithm, StringParameterAlgorithm,
    FileInputParameterAlgorithm, DatabaseConnectionParameterAlgorithm {

  public final static String STRING_IDENTIFIER = "pathToOutputFile";
  public final static String CSVFILE_IDENTIFIER = "input file";
  public final static String DATABASE_IDENTIFIER = "DB-connection";
  protected String path = null;
  protected String selectedColumn = null;
  protected DatabaseConnectionGenerator inputGenerator;
  protected DenialConstraintResultReceiver resultReceiver;

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurationRequirement = new ArrayList<>();

    ConfigurationRequirementString requirementString =
        new ConfigurationRequirementString(STRING_IDENTIFIER);
    ConfigurationRequirementFileInput requirementFileInput =
        new ConfigurationRequirementFileInput(CSVFILE_IDENTIFIER);
    ConfigurationRequirementDatabaseConnection requirementDatabaseConnection =
        new ConfigurationRequirementDatabaseConnection(DATABASE_IDENTIFIER);

    requirementString.setRequired(false);
    requirementFileInput.setRequired(false);
    requirementDatabaseConnection.setRequired(false);

    configurationRequirement.add(requirementString);
    configurationRequirement.add(requirementFileInput);
    configurationRequirement.add(requirementDatabaseConnection);

    return configurationRequirement;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    try {
      ColumnIdentifier c1 = new ColumnIdentifier("WDC_planets.csv", "Name");
      ColumnIdentifier c2 = new ColumnIdentifier("WDC_planets.csv", "Mass");

      Predicate p1 = new PredicateVariable(c1, 1, Operator.EQUAL, c2, 2);
      Predicate p2 = new PredicateConstant<Double>(c2, 1, Operator.GREATER, 5.0d);
      Predicate p3 = new PredicateVariable(c1, 1, Operator.GREATER_EQUAL, c2, 2);

      resultReceiver.receiveResult(new DenialConstraint(p1, p2));
      resultReceiver.receiveResult(new DenialConstraint(p3));
    } catch (CouldNotReceiveResultException | ColumnNameMismatchException e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void setResultReceiver(DenialConstraintResultReceiver resultReceiver) {
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
  public void setFileInputConfigurationValue(String identifier, FileInputGenerator... values)
      throws AlgorithmConfigurationException {
    if (!identifier.equals(CSVFILE_IDENTIFIER)) {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setDatabaseConnectionGeneratorConfigurationValue(String identifier,
      DatabaseConnectionGenerator... values) throws AlgorithmConfigurationException {

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
    return "Example algorithm for denial constraints.";
  }

}
