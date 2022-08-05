package de.metanome.cli;

import com.google.common.collect.ImmutableMap;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementCheckBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingCheckBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingPrimitive;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parse a string value into the matching instance of a subclass of
 * {@link ConfigurationSettingPrimitive} given the source configuration requirement.
 */
class ConfigurationSettingPrimitiveParser {

  private static final Logger LOG = LoggerFactory
      .getLogger(ConfigurationSettingPrimitiveParser.class);

  @FunctionalInterface
  interface Parser {

    ConfigurationSettingPrimitive<?> parse(String value);
  }

  private final Map<Class<? extends ConfigurationRequirement<?>>, Parser> requirementToParser;

  ConfigurationSettingPrimitiveParser() {
    requirementToParser = ImmutableMap.<Class<? extends ConfigurationRequirement<?>>, Parser>builder()
        .put(ConfigurationRequirementBoolean.class, this::parseBoolean)
        .put(ConfigurationRequirementInteger.class, this::parseInteger)
        .put(ConfigurationRequirementString.class, this::parseString)
        .put(ConfigurationRequirementListBox.class, this::parseListBox)
        .put(ConfigurationRequirementCheckBox.class, this::parseCheckBox)
        .build();
  }

  boolean supports(final ConfigurationRequirement<?> requirement) {
    return requirementToParser.keySet().contains(requirement.getClass());
  }

  ConfigurationSettingPrimitive<?> parse(final ConfigurationRequirement<?> requirement,
      final String value) {

    LOG.trace("Parsing configuration value with key {}", requirement.getIdentifier());
    return requirementToParser.get(requirement.getClass()).parse(value);
  }

  private ConfigurationSettingPrimitive<?> parseBoolean(final String value) {
    return new ConfigurationSettingBoolean(Boolean.parseBoolean(value));
  }

  private ConfigurationSettingPrimitive<?> parseInteger(final String value) {
    return new ConfigurationSettingInteger(Integer.parseInt(value));
  }

  private ConfigurationSettingPrimitive<?> parseString(final String value) {
    return new ConfigurationSettingString(value);
  }

  private ConfigurationSettingPrimitive<?> parseListBox(final String value) {
    return new ConfigurationSettingListBox(value);
  }

  private ConfigurationSettingPrimitive<?> parseCheckBox(final String value) {
    // Currently restricted to single selection
    return new ConfigurationSettingCheckBox(new String[]{value});
  }
}
