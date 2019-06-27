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
package de.metanome.backend.input;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.RelationalInputGeneratorInitializer;
import de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.input.database.DefaultTableInputGenerator;
import de.metanome.backend.input.file.DefaultFileInputGenerator;

/**
 * Initializes {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}s that are
 * based on files or database tables.
 */
public class DefaultRelationalInputGeneratorInitializer
  implements RelationalInputGeneratorInitializer {

  List<RelationalInputGenerator> generatorList = new ArrayList<>();
  String identifier;

  /**
   * @param requirementRelationalInput the requirement to initialize from
   * @throws AlgorithmConfigurationException if one of the settings from the requirement cannot be
   *                                         converted
   */
  public DefaultRelationalInputGeneratorInitializer(
    ConfigurationRequirementRelationalInput requirementRelationalInput)
    throws AlgorithmConfigurationException {
    this.identifier = requirementRelationalInput.getIdentifier();

    ConfigurationSettingRelationalInput[] settings = requirementRelationalInput.getSettings();
    for (ConfigurationSettingRelationalInput setting : settings) {
      setting.generate(this);
    }
  }

  /**
   * Initialize {@link de.metanome.algorithm_integration.input.RelationalInputGenerator} from a
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput}.
   *
   * @param setting the setting used to initialize the input
   * @throws AlgorithmConfigurationException if the input cannot be initialized
   */
  @Override
  public void initialize(ConfigurationSettingFileInput setting)
    throws AlgorithmConfigurationException {

    File currFile = new File(setting.getFileName());
    try {
      if (currFile.isFile()) {
          generatorList.add(new DefaultFileInputGenerator(currFile, setting));
      } else if (currFile.isDirectory()) {
          File[] filesInDirectory = currFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
              for (String fileEnding : Constants.ACCEPTED_FILE_ENDINGS_ARRAY) {
                if (name.endsWith(fileEnding)) {
                  return true;
                }
              }
              return false;
            }
          });
          for (File file : filesInDirectory) {
            generatorList.add(new DefaultFileInputGenerator(file, setting));
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }

  /**
   * Initialize {@link de.metanome.algorithm_integration.input.RelationalInputGenerator} from a
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput}.
   *
   * @param setting the setting used to initialize the input
   * @throws AlgorithmConfigurationException if the input cannot be initialized
   */
  @Override
  public void initialize(ConfigurationSettingTableInput setting)
    throws AlgorithmConfigurationException {
    generatorList.add(new DefaultTableInputGenerator(setting));
  }

  /**
   * @return the initialized {@link de.metanome.algorithm_integration.input.RelationalInputGenerator}s
   */
  public ConfigurationValueRelationalInputGenerator getConfigurationValue() {
    return new ConfigurationValueRelationalInputGenerator(identifier,
      generatorList.toArray(
        new RelationalInputGenerator[generatorList
          .size()]));
  }
}
