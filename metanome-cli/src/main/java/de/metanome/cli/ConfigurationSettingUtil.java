package de.metanome.cli;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementCheckBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDefaultValue;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.configuration.ConfigurationSetting;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingCheckBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.reflect.ConstructorUtils;

class ConfigurationSettingUtil {

  private static final Map<Class<? extends ConfigurationRequirementDefaultValue<?, ?>>, Class<? extends ConfigurationSetting>> requirementToSetting = new HashMap<>();

  static {
    requirementToSetting
        .put(ConfigurationRequirementBoolean.class, ConfigurationSettingBoolean.class);
    requirementToSetting
        .put(ConfigurationRequirementInteger.class, ConfigurationSettingInteger.class);
    requirementToSetting
        .put(ConfigurationRequirementString.class, ConfigurationSettingString.class);
    requirementToSetting
        .put(ConfigurationRequirementListBox.class, ConfigurationSettingListBox.class);
    requirementToSetting
        .put(ConfigurationRequirementCheckBox.class, ConfigurationSettingCheckBox.class);
  }

  static ConfigurationSetting newSettingsInstance(final ConfigurationRequirement<?> requirement,
      final Object value) throws AlgorithmConfigurationException {

    try {
      return ConstructorUtils
          .invokeConstructor(requirementToSetting.get(requirement.getClass()), value);
    } catch (final ReflectiveOperationException e) {
      throw new AlgorithmConfigurationException("failed to create setting", e);
    }
  }

}
