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
package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.input.file.DefaultFileInputGenerator;
import de.metanome.backend.results_db.AlgorithmType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents {@link de.metanome.algorithm_integration.input.FileInputGenerator} configuration
 * values for {@link Algorithm}s.
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueFileInputGenerator
  extends ConfigurationValue<FileInputGenerator, ConfigurationRequirementFileInput> {

  protected ConfigurationValueFileInputGenerator() {
  }

  public ConfigurationValueFileInputGenerator(String identifier,
                                              FileInputGenerator... values) {
    super(identifier, values);
  }

  public ConfigurationValueFileInputGenerator(ConfigurationRequirementFileInput requirement)
    throws AlgorithmConfigurationException, FileNotFoundException {
    super(requirement);
  }

  @Override
  protected FileInputGenerator[] convertToValues(
    ConfigurationRequirementFileInput requirement)
          throws AlgorithmConfigurationException {
    ConfigurationSettingFileInput[] settings = requirement.getSettings();

    List<FileInputGenerator>
      fileInputGenerators =
      new ArrayList<>();

    for (int i = 0; i < settings.length; i++) {
      try {
        File currFile = new File(settings[i].getFileName());
        if (currFile.isFile()) {
            fileInputGenerators.add(new DefaultFileInputGenerator(currFile, settings[i]));
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
            fileInputGenerators.add(new DefaultFileInputGenerator(file, settings[i]));
          }
        }
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
    }
    FileInputGenerator[] fileInputArray = new FileInputGenerator[fileInputGenerators.size()];
    return fileInputGenerators.toArray(fileInputArray);
  }

  @Override
  public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
    throws AlgorithmConfigurationException {
    if (!algorithmInterfaces.contains(AlgorithmType.FILE_INPUT.getAlgorithmClass())) {
      throw new AlgorithmConfigurationException(
        "Algorithm does not accept file input configuration values.");
    }

    FileInputParameterAlgorithm fileInputParameterAlgorithm =
      (FileInputParameterAlgorithm) algorithm;
    fileInputParameterAlgorithm.setFileInputConfigurationValue(identifier, values);
  }
}
