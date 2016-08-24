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
import de.metanome.algorithm_integration.algorithm_types.RadioBoxParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRadioBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingRadioBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ConfigurationValueRadioBox
        extends ConfigurationValue<String[], ConfigurationRequirementRadioBox> {

    protected ConfigurationValueRadioBox() {
    }

    public ConfigurationValueRadioBox(String identifier, String[]... values) {
        super(identifier, values);
    }

    public ConfigurationValueRadioBox(ConfigurationRequirementRadioBox requirement)
            throws AlgorithmConfigurationException {
        super(requirement);
    }

    @Override
    protected String[][] convertToValues(ConfigurationRequirementRadioBox requirement)
            throws AlgorithmConfigurationException {
        ConfigurationSettingRadioBox[] settings = requirement.getSettings();
        String[][] configValues = new String[settings.length][settings.length];
        int i = 0;
        for (ConfigurationSettingRadioBox setting : settings) {
                configValues[i] = setting.getValue();
                i++;
        }
        return configValues;
    }

    @Override
    public void triggerSetValue(Algorithm algorithm, Set<Class<?>> algorithmInterfaces)
            throws AlgorithmConfigurationException {
        if (!algorithmInterfaces.contains(RadioBoxParameterAlgorithm.class)) {
            throw new AlgorithmConfigurationException(
                    "Algorithm does not accept arraylist configuration values.");
        }

        RadioBoxParameterAlgorithm radioBoxParameterAlgorithm = (RadioBoxParameterAlgorithm) algorithm;
        radioBoxParameterAlgorithm.setRadioBoxConfigurationValue(this.identifier, this.values);
    }
}
