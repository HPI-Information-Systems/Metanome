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

package de.metanome.algorithm_integration.configuration;


import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import javax.xml.bind.annotation.XmlTransient;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Concrete {@link ConfigurationRequirement} for check box of strings.
 *
 * @author Maxi Fischer
 * @see ConfigurationRequirement
 */
@JsonTypeName("ConfigurationRequirementCheckBox")
public class ConfigurationRequirementCheckBox
        extends ConfigurationRequirementDefaultValue<String[], ConfigurationSettingCheckBox> {

    private static final long serialVersionUID = -4281413599644981292L;

    // Needed for restful serialization
    public String type = "ConfigurationRequirementCheckBox";

    private String[] values;

    public ConfigurationRequirementCheckBox() {
    }

    public ConfigurationRequirementCheckBox(String identifier, String[] values) {
        super(identifier);
        this.values = values;
    }

    public ConfigurationRequirementCheckBox(String identifier, String[] values,
                                           int numberOfSettings) {
        super(identifier, numberOfSettings);
        this.values = values;
    }

    public ConfigurationRequirementCheckBox(String identifier, String[] values,
                                           int minNumberOfSetting, int maxNumberOfSetting) {
        super(identifier, minNumberOfSetting, maxNumberOfSetting);
        this.values = values;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    /**
     * {@inheritDoc}
     */
    @XmlTransient
    @Override
    public ConfigurationValue build(ConfigurationFactory factory)
            throws AlgorithmConfigurationException, FileNotFoundException {
        return factory.build(this);
    }

}
