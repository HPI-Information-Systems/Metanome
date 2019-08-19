/**
 * Copyright 2014-2019 by Metanome Project
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
package de.metanome.algorithms.testing.example_cid_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.algorithm_types.*;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.result_receiver.ConditionalInclusionDependencyResultReceiver;
import de.metanome.algorithm_integration.results.ConditionalInclusionDependency;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExampleAlgorithm
    implements ConditionalInclusionDependencyAlgorithm, TempFileAlgorithm, StringParameterAlgorithm,
               FileInputParameterAlgorithm, IntegerParameterAlgorithm {

  public final static String CSV_FILE_IDENTIFIER = "input file";
  public final static String STRING_IDENTIFIER = "tableName";
  public final static String INTEGER_IDENTIFIER = "numberOfTables";
  protected String tableName = null;
  protected int numberOfTables = -1;
  protected ConditionalInclusionDependencyResultReceiver resultReceiver;
  protected FileGenerator tempFileGenerator;
  protected boolean fileInputSet = false;

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurationRequirement = new ArrayList<>();

    ConfigurationRequirementFileInput
        requirementFileInput =
        new ConfigurationRequirementFileInput(CSV_FILE_IDENTIFIER);
    ConfigurationRequirementString
        requirementString = new ConfigurationRequirementString(STRING_IDENTIFIER);
    ConfigurationRequirementInteger
        requirementInteger = new ConfigurationRequirementInteger(INTEGER_IDENTIFIER);
    try {
      requirementString.checkAndSetDefaultValues("test");
      requirementInteger.checkAndSetDefaultValues(5);
    } catch (AlgorithmConfigurationException e) {
      e.printStackTrace();
    }

    requirementFileInput.setRequired(true);
    requirementFileInput.setRequired(true);
    requirementFileInput.setRequired(true);

    configurationRequirement.add(requirementFileInput);
    configurationRequirement.add(requirementString);
    configurationRequirement.add(requirementInteger);

    return configurationRequirement;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    File tempFile = tempFileGenerator.getTemporaryFile();
    PrintWriter tempWriter;
    try {
      tempWriter = new PrintWriter(tempFile);
    } catch (FileNotFoundException e) {
      throw new AlgorithmExecutionException("File not found.");
    }
    tempWriter.write("table1");
    tempWriter.close();

      try {
          FileUtils.readFileToString(tempFile);
      } catch (IOException ex) {
          throw new AlgorithmExecutionException("Could not read from file.");
      }

    if ((tableName != null) && fileInputSet && (numberOfTables != -1)) {
      resultReceiver.receiveResult(
          new ConditionalInclusionDependency(
              new ColumnPermutation(
                  new ColumnIdentifier("WDC_planets.csv", "Name"),
                  new ColumnIdentifier("WDC_planets.csv", "Type")),
              new ColumnPermutation(
                  new ColumnIdentifier("WDC_planets.csv", "Mass"),
                  new ColumnIdentifier("WDC_planets.csv", "Rings")),
              "Condition"
          )
      );
    }
  }

  @Override
  public void setResultReceiver(ConditionalInclusionDependencyResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }


  @Override
  public void setTempFileGenerator(FileGenerator tempFileGenerator) {
    this.tempFileGenerator = tempFileGenerator;
  }

  @Override
  public void setStringConfigurationValue(String identifier, String... values)
      throws AlgorithmConfigurationException {
    if ((identifier.equals("tableName")) && (values.length == 1)) {
      tableName = values[0];
    } else {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setFileInputConfigurationValue(String identifier, FileInputGenerator... values)
      throws AlgorithmConfigurationException {
    if ((identifier.equals(CSV_FILE_IDENTIFIER)) && (values.length == 1)) {
      // Input file is not being set on algorithm.
      fileInputSet = true;
    } else {
      throw new AlgorithmConfigurationException("Incorrect configuration.");
    }
  }

  @Override
  public void setIntegerConfigurationValue(String identifier, Integer... values)
      throws AlgorithmConfigurationException {
    if ((identifier.equals(INTEGER_IDENTIFIER)) && (values.length == 1)) {
      numberOfTables = values[0];
    } else {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm for conditional inclusion dependencies.";
  }
}