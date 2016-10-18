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

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.CheckBoxParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementCheckBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingCheckBox;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigurationValueCheckBox
        extends ConfigurationValue<String[], ConfigurationRequirementCheckBox> {

    protected ConfigurationValueCheckBox() {
    }

    public ConfigurationValueCheckBox(String identifier, String[]... values) {
        super(identifier, values);
    }

    public ConfigurationValueCheckBox(ConfigurationRequirementCheckBox requirement)
            throws AlgorithmConfigurationException, FileNotFoundException {
        super(requirement);
    }

    @Override
    protected String[][] convertToValues(ConfigurationRequirementCheckBox requirement)
            throws AlgorithmConfigurationException {
        ConfigurationSettingCheckBox[] settings = requirement.getSettings();
        String[][] configValues = new String[settings.length][settings.length];
        int i = 0;
        for (ConfigurationSettingCheckBox setting : settings) {
                configValues[i] = setting.getValue();
                i++;
        }
        return configValues;
    }

    @Override
    public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
            throws AlgorithmConfigurationException {
        if (!algorithmInterfaces.contains(CheckBoxParameterAlgorithm.class)) {
            throw new AlgorithmConfigurationException(
                    "Algorithm does not accept arraylist configuration values.");
        }

        CheckBoxParameterAlgorithm checkBoxParameterAlgorithm = (CheckBoxParameterAlgorithm) algorithm;
        checkBoxParameterAlgorithm.setCheckBoxConfigurationValue(this.identifier, this.values);
    }
}
