/**
 * Copyright 2016 by Metanome Project
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
package de.metanome.backend.helper;


import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.backend.input.database.DefaultTableInputGenerator;
import de.metanome.backend.input.file.DefaultFileInputGenerator;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.TableInput;

public class InputToGeneratorConverter {

  public static RelationalInputGenerator convertInput(Input input)
    throws AlgorithmConfigurationException {
    if (input instanceof FileInput) {
      return new DefaultFileInputGenerator(convertInputToSetting((FileInput) input));
    } else if (input instanceof TableInput) {
      return new DefaultTableInputGenerator(convertInputToSetting((TableInput) input));
    } else if (input instanceof DatabaseConnection) {
      // we do not know which table was used for profiling, thus we can not compute
      // ranking results for results on database connections
      return null;
    }

    return null;
  }

  /**
   * Converts the given file input to a configuration setting.
   *
   * @param input file input
   * @return the configuration setting
   */
  public static ConfigurationSettingFileInput convertInputToSetting(FileInput input) {
    return new ConfigurationSettingFileInput()
      .setEscapeChar(input.getEscapeChar())
      .setFileName(input.getFileName())
      .setHeader(input.isHasHeader())
      .setIgnoreLeadingWhiteSpace(input.isIgnoreLeadingWhiteSpace())
      .setNullValue(input.getNullValue())
      .setQuoteChar(input.getQuoteChar())
      .setSeparatorChar(input.getSeparator())
      .setSkipDifferingLines(input.isSkipDifferingLines())
      .setSkipLines(input.getSkipLines())
      .setStrictQuotes(input.isStrictQuotes());
  }

  /**
   * Converts the given table input to a configuration setting.
   *
   * @param input table input
   * @return the configuration setting
   */
  public static ConfigurationSettingTableInput convertInputToSetting(TableInput input) {
    return new ConfigurationSettingTableInput()
      .setDatabaseConnection(convertInputToSetting(input.getDatabaseConnection()))
      .setTable(input.getTableName());
  }

  /**
   * Converts the given database connection input to a configuration setting.
   *
   * @param input database connection
   * @return the configuration setting
   */
  public static ConfigurationSettingDatabaseConnection convertInputToSetting(
    DatabaseConnection input) {
    return new ConfigurationSettingDatabaseConnection()
      .setDbUrl(input.getUrl())
      .setPassword(input.getPassword())
      .setSystem(input.getSystem())
      .setUsername(input.getUsername());
  }

}
