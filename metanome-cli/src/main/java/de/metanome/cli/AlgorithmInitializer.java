package de.metanome.cli;

import static java.util.stream.Collectors.toSet;

import com.google.common.base.Joiner;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.reflect.TypeToken;
import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationFactory;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDefaultValue;
import de.metanome.algorithm_integration.configuration.ConfigurationSetting;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingPrimitive;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.backend.configuration.DefaultConfigurationFactory;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Apply all algorithm configuration items given to metanome-cli in form of key-value-pairs.
 *
 * <p>Repeated settings keys are supported and warning messages are emitted if some of the configuration
 * requirements are left without any value. Configuration requirements with duplicate keys are
 * not allowed.</p>
 */
class AlgorithmInitializer {

  private static final Logger LOG = LoggerFactory.getLogger(AlgorithmInitializer.class);

  private final Algorithm algorithm;
  private final Map<String, ConfigurationRequirement<?>> requirements;
  private final List<Pair<String, String>> config;
  private final BiConsumer<String, Object> listener;
  private final ConfigurationSettingPrimitiveParser parser;

  private AlgorithmInitializer(final Algorithm algorithm, final List<Pair<String, String>> config,
      final BiConsumer<String, Object> listener)
      throws AlgorithmConfigurationException {

    this.algorithm = algorithm;
    this.requirements = getRequirements(algorithm);
    this.config = config;
    this.listener = listener;
    parser = new ConfigurationSettingPrimitiveParser();
  }

  private Map<String, ConfigurationRequirement<?>> getRequirements(final Algorithm algorithm)
      throws AlgorithmConfigurationException {

    final Map<String, ConfigurationRequirement<?>> result = new HashMap<>();
    for (final ConfigurationRequirement<?> entry : algorithm.getConfigurationRequirements()) {
      if (result.put(entry.getIdentifier(), entry) != null) {
        throw new AlgorithmConfigurationException(
            "duplicate identifier in configuration requirements: " + entry.getIdentifier());
      }
    }
    return result;
  }

  void apply() throws AlgorithmConfigurationException {
    final ListMultimap<String, ConfigurationSetting> settings = toSettings();
    warnIfNotFullyConfigured(settings);
    applySettingsToConfigurationRequirements(settings);
    triggerSetValue();
  }

  private ListMultimap<String, ConfigurationSetting> toSettings()
      throws AlgorithmConfigurationException {
    final ListMultimap<String, ConfigurationSetting> settings = ArrayListMultimap.create();
    applyFromConfiguration(settings);
    applyDefaultValuesForRemaining(settings);
    return settings;
  }

  private void applyFromConfiguration(final ListMultimap<String, ConfigurationSetting> settings)
      throws AlgorithmConfigurationException {

    for (final Pair<String, String> entry : config) {
      final ConfigurationRequirement<?> requirement = requirements.get(entry.getLeft());
      throwOnAbsentRequirement(entry);
      if (parser.supports(requirement)) {
        final ConfigurationSettingPrimitive<?> value = parser.parse(requirement, entry.getRight());
        settings.put(entry.getLeft(), value);
        listener.accept(requirement.getIdentifier(), value.getValue());
      } else {
        LOG.debug("Requirement with identifier {} is not supported by the parser.",
            requirement.getIdentifier());
      }
    }
  }

  private void applyDefaultValuesForRemaining(
      final ListMultimap<String, ConfigurationSetting> settings)
      throws AlgorithmConfigurationException {

    final List<String> remaining = new ArrayList<>(requirements.keySet());
    for (final Pair<String, String> item : config) {
      remaining.remove(item.getKey());
    }

    for (final String item : remaining) {
      final ConfigurationRequirement<?> requirement = requirements.get(item);
      if (!(requirement instanceof ConfigurationRequirementDefaultValue)) {
        LOG.trace("Requirement with identifier '{}' cannot handle default values",
            requirement.getIdentifier());
        continue;
      }

      final ConfigurationRequirementDefaultValue<?, ?> c = (ConfigurationRequirementDefaultValue<?, ?>) requirement;
      final Object[] defaultValues = c.getDefaultValues();
      if (defaultValues == null) {
        LOG.trace("Requirement with identifier '{}' has no default values set", c.getIdentifier());
        continue;
      }

      for (final Object defaultValue : defaultValues) {
        settings.put(requirement.getIdentifier(),
            ConfigurationSettingUtil.newSettingsInstance(requirement, defaultValue));
        listener.accept(requirement.getIdentifier(), defaultValue);
        LOG.info("Set default value '{}' on requirement '{}'", defaultValue,
            requirement.getIdentifier());
      }
    }
  }

  private void throwOnAbsentRequirement(final Pair<String, String> entry)
      throws AlgorithmConfigurationException {

    if (!requirements.containsKey(entry.getLeft())) {
      final String message = String.format(
          "no configuration requirement present for key %s (value %s). available identifiers: %s",
          entry.getLeft(), entry.getRight(), Joiner.on(", ").join(requirements.keySet()));
      throw new AlgorithmConfigurationException(message);
    }
  }

  private void applySettingsToConfigurationRequirements(
      final ListMultimap<String, ConfigurationSetting> settings)
      throws AlgorithmConfigurationException {

    for (final String identifier : requirements.keySet()) {
      final List<ConfigurationSetting> settingsList = settings.get(identifier);
      if (!settingsList.isEmpty()) {
        final ConfigurationRequirement<?> requirement = requirements.get(identifier);
        setSettings(requirement, settingsList);
      }
    }
  }

  private void triggerSetValue() throws AlgorithmConfigurationException {
    final ConfigurationFactory factory = new DefaultConfigurationFactory();
    final Set<Class<?>> interfaces = collectInterfaces(algorithm);

    try {
      for (final ConfigurationRequirement<?> requirement : requirements.values()) {
        if (requirement.getSettings() != null && requirement.getSettings().length > 0) {
          final ConfigurationValue value = requirement.build(factory);
          value.triggerSetValue(algorithm, interfaces);
        }
      }
    } catch (final FileNotFoundException e) {
      throw new AlgorithmConfigurationException("trigger set value", e);
    }
  }

  private Set<Class<?>> collectInterfaces(final Object object) {
    return TypeToken.of(object.getClass()).getTypes().interfaces().stream()
        .map(TypeToken::getRawType)
        .collect(toSet());
  }

  @SuppressWarnings("unchecked")
  private <T extends ConfigurationSetting> void setSettings(
      final ConfigurationRequirement<T> requirement,
      final List<ConfigurationSetting> settingsList) throws AlgorithmConfigurationException {

    // Since instances of the array are casted back and forth,
    // the type tag of the array must be as specific as possible.
    final T[] settings = (T[]) Array
        .newInstance(settingsList.get(0).getClass(), settingsList.size());
    settingsList.toArray(settings);
    requirement.checkAndSetSettings(settings);
  }

  private void warnIfNotFullyConfigured(final ListMultimap<String, ConfigurationSetting> settings) {
    final Collection<String> remaining = new ArrayList<>(requirements.keySet());
    remaining.removeAll(settings.keySet());

    // Currently only the primitive types (string, int, ...) are in the scope of this class.
    // Remove complaints about missing complex requirements (tables, DB connections).
    remaining.removeIf(cr -> !parser.supports(requirements.get(cr)));

    if (!remaining.isEmpty()) {
      LOG.warn("Configuration for the following identifiers will remain uninitialized: {}.",
          Joiner.on(", ").join(remaining));
      LOG.warn("This may cause algorithms to malfunction or hinder reproducibility.");
    }
  }

  static AlgorithmInitializer forAlgorithm(final Algorithm algorithm,
      final List<Pair<String, String>> config) throws AlgorithmConfigurationException {

    return new AlgorithmInitializer(algorithm, config, (a, b) -> {
    });
  }

  static AlgorithmInitializer forAlgorithm(final Algorithm algorithm,
      final List<Pair<String, String>> config, final BiConsumer<String, Object> listener)
      throws AlgorithmConfigurationException {

    return new AlgorithmInitializer(algorithm, config, listener);
  }
}
