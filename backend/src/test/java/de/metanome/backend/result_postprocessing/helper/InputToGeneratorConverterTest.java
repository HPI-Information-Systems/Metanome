/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.result_postprocessing.helper;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.DbSystem;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.helper.InputToGeneratorConverter;
import de.metanome.backend.input.file.DefaultFileInputGenerator;
import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.TableInput;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InputToGeneratorConverterTest {

  @Test
  public void convertInput() throws AlgorithmConfigurationException, UnsupportedEncodingException {
    // Set up
    FileInput input = new FileInput(getTestFile());

    ConfigurationSettingFileInput setting = InputToGeneratorConverter.convertInputToSetting(input);

    // Expected Value
    DefaultFileInputGenerator expectedGenerator = new DefaultFileInputGenerator(setting);

    // Execute Functionality
    RelationalInputGenerator generator = InputToGeneratorConverter.convertInput(input);

    // Check
    assertTrue(generator instanceof DefaultFileInputGenerator);
    assertEquals(expectedGenerator.getInputFile(),
      ((DefaultFileInputGenerator) generator).getInputFile());
    assertEquals(0, expectedGenerator.getSetting().compareTo(((DefaultFileInputGenerator) generator).getSetting()));
  }


  @Test
  public void convertFileInput() {
    // Set up
    FileInput input = new FileInput("some file");

    // Execute Functionality
    ConfigurationSettingFileInput setting = InputToGeneratorConverter.convertInputToSetting(input);

    // Check
    assertEquals(input.getEscapeChar(), setting.getEscapeChar());
    assertEquals(input.getFileName(), setting.getFileName());
    assertEquals(input.getNullValue(), setting.getNullValue());
    assertEquals(input.getQuoteChar(), setting.getQuoteChar());
    assertEquals(input.getSeparator(), setting.getSeparatorChar());
    assertEquals(input.getSkipLines(), setting.getSkipLines());
    assertEquals(input.isHasHeader(), setting.hasHeader());
    assertEquals(input.isIgnoreLeadingWhiteSpace(), setting.isIgnoreLeadingWhiteSpace());
    assertEquals(input.isSkipDifferingLines(), setting.isSkipDifferingLines());
    assertEquals(input.isStrictQuotes(), setting.isStrictQuotes());
  }

  @Test
  public void convertDatabaseConnection() {
    // Set up
    DatabaseConnection input = new DatabaseConnection();
    input.setPassword("pwd")
      .setSystem(DbSystem.DB2)
      .setUrl("url")
      .setUsername("user");

    // Execute Functionality
    ConfigurationSettingDatabaseConnection setting = InputToGeneratorConverter.convertInputToSetting(input);

    // Check
    assertEquals(input.getPassword(), setting.getPassword());
    assertEquals(input.getSystem(), setting.getSystem());
    assertEquals(input.getUrl(), setting.getDbUrl());
    assertEquals(input.getUsername(), setting.getUsername());
  }

  @Test
  public void convertTableInput() {
    // Set up
    DatabaseConnection connection = new DatabaseConnection();
    connection.setPassword("pwd")
      .setSystem(DbSystem.DB2)
      .setUrl("url")
      .setUsername("user");
    ConfigurationSettingDatabaseConnection connectionSetting = InputToGeneratorConverter.convertInputToSetting(connection);

    TableInput input = new TableInput();
    input.setTableName("table")
      .setDatabaseConnection(connection);

    // Execute Functionality
    ConfigurationSettingTableInput setting = InputToGeneratorConverter.convertInputToSetting(input);

    // Check
    assertEquals(connectionSetting, setting.getDatabaseConnection());
    assertEquals(input.getTableName(), setting.getTable());
  }

  private String getTestFile() throws UnsupportedEncodingException {
    String filePath = Thread.currentThread().getContextClassLoader().getResource(Constants.INPUTDATA_RESOURCE_NAME).getPath();
    filePath += Constants.FILE_SEPARATOR + "inputA.csv";
    return new File(URLDecoder.decode(filePath, Constants.FILE_ENCODING)).getAbsolutePath();
  }

}
