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

package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementCsvFile;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementSqlIterator;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.SqlInputGenerator;
import de.metanome.backend.input.csv.CsvFileGenerator;
import de.metanome.backend.input.sql.SqlIteratorGenerator;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Converts the incoming {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}s
 * to {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}s.
 */
public class ConfigurationValueFactory {

  /**
   * Converts the incoming {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}s
   * to {@link de.metanome.algorithm_integration.configuration.ConfigurationValue}s.
   *
   * @param specification the specification to convert
   * @return the created configuration value
   */
  public static ConfigurationValue createConfigurationValue(
      ConfigurationRequirement specification) throws AlgorithmConfigurationException {

    if (specification instanceof ConfigurationRequirementBoolean) {
      return new ConfigurationValueBoolean((ConfigurationRequirementBoolean) specification);
    } else if (specification instanceof ConfigurationRequirementCsvFile) {
      return new ConfigurationValueFileInputGenerator(specification.getIdentifier(),
                                                      createFileInputGenerators(
                                                          (ConfigurationRequirementCsvFile) specification));
    } else if (specification instanceof ConfigurationRequirementSqlIterator) {
      return new ConfigurationValueSqlInputGenerator(specification.getIdentifier(),
                                                     createSqlIteratorGenerators(
                                                         (ConfigurationRequirementSqlIterator) specification));
    } else if (specification instanceof ConfigurationRequirementString) {
      return new ConfigurationValueString((ConfigurationRequirementString) specification);
    } else if (specification instanceof ConfigurationRequirementInteger) {
      return new ConfigurationValueInteger((ConfigurationRequirementInteger) specification);
    } else if (specification instanceof ConfigurationRequirementListBox) {
      return new ConfigurationValueListBox((ConfigurationRequirementListBox) specification);
    } else {
      throw new AlgorithmConfigurationException("Unsupported ConfigurationSpecification subclass.");
    }
  }

  /**
   * Converts a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementSqlIterator}
   * to a {@link de.metanome.algorithm_integration.input.SqlInputGenerator}.
   *
   * @param specification the sql iterator specification
   * @return the created sql input generator
   */
  private static SqlInputGenerator[] createSqlIteratorGenerators(
      ConfigurationRequirementSqlIterator specification) throws AlgorithmConfigurationException {

    SqlIteratorGenerator[]
        sqlIteratorGenerators =
        new SqlIteratorGenerator[specification.getSettings().length];

    int i = 0;
    for (ConfigurationSettingSqlIterator setting : specification.getSettings()) {
      sqlIteratorGenerators[i] = new SqlIteratorGenerator(setting.getDbUrl(),
                                                          setting.getUsername(),
                                                          setting.getPassword());
      i++;
    }
    return sqlIteratorGenerators;
  }

  /**
   * Converts a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementCsvFile}
   * to a {@link de.metanome.algorithm_integration.input.FileInputGenerator}.
   *
   * @param specification the file input specification
   * @return the created file input generator
   */
  private static FileInputGenerator[] createFileInputGenerators(
      ConfigurationRequirementCsvFile specification) throws AlgorithmConfigurationException {

    CsvFileGenerator[] csvFileGenerators = new CsvFileGenerator[specification.getSettings().length];

    int i = 0;
    for (ConfigurationSettingCsvFile setting : specification.getSettings()) {
      try {
        if (setting.isAdvanced()) {
          csvFileGenerators[i] =
              new CsvFileGenerator(new File(setting.getFileName()), setting.getSeparatorChar(),
                                   setting.getQuoteChar(), setting.getEscapeChar(),
                                   setting.getSkipLines(),
                                   setting.isStrictQuotes(), setting.isIgnoreLeadingWhiteSpace(),
                                   setting.hasHeader(),
                                   setting.isSkipDifferingLines());
        } else {
          csvFileGenerators[i] = new CsvFileGenerator(new File(setting.getFileName()));
        }
      } catch (FileNotFoundException e) {
        throw new AlgorithmConfigurationException("Could not find CSV file.");
      }
      i++;
    }

    return csvFileGenerators;
  }

}
