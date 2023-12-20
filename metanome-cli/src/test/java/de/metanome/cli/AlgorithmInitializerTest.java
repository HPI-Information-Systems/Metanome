package de.metanome.cli;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_types.BooleanParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.IntegerParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class AlgorithmInitializerTest {

  private static final String KEY1 = "KEY1";
  private static final String KEY2 = "KEY2";

  private AlgorithmInitializer initializer;
  private ExampleAlgorithm algorithm;

  @Before
  public void setUp() {
    algorithm = new ExampleAlgorithm();
  }

  private void initialize(final Pair<String, String>... config) throws Exception {
    AlgorithmInitializer.forAlgorithm(algorithm, asList(config)).apply();
  }

  @Test
  public void singleItem() throws Exception {
    algorithm.requirements.add(booleanRequirement());
    algorithm.requirements.add(integerRequirement());

    initialize(Pair.of(KEY1, "true"));

    assertThat(algorithm.recordedIdentifier).hasSize(1).first().isEqualTo(KEY1);
    assertThat(algorithm.recordedValues).hasSize(1).containsOnly(new Boolean[]{true});
  }

  @Test
  public void twoItemsOfSameKey() throws Exception {
    algorithm.requirements.add(booleanRequirement());
    algorithm.requirements.add(integerRequirement());

    initialize(Pair.of(KEY1, "true"), Pair.of(KEY1, "false"));

    assertThat(algorithm.recordedIdentifier).hasSize(1).first().isEqualTo(KEY1);
    assertThat(algorithm.recordedValues).hasSize(1).containsOnly(new Boolean[]{true, false});
  }

  @Test
  public void shouldThrowOnDuplicateRequirement() {
    algorithm.requirements.add(booleanRequirement());
    algorithm.requirements.add(booleanRequirement());

    assertThatThrownBy(this::initialize)
        .isExactlyInstanceOf(AlgorithmConfigurationException.class)
        .hasMessageContaining("duplicate");
  }

  @Test
  public void twoItemsOfDifferentTypeAndKey() throws Exception {
    algorithm.requirements.add(integerRequirement());
    algorithm.requirements.add(booleanRequirement());

    initialize(Pair.of(KEY1, "true"), Pair.of(KEY2, "42"));

    assertThat(algorithm.getRecorded()).containsEntry(KEY1, new Boolean[]{true})
        .containsEntry(KEY2, new Integer[]{42});
  }

  @Test
  public void shouldReceiveParsedValues() throws Exception {
    final ListMultimap<String, Object> received = ArrayListMultimap.create();
    algorithm.requirements.add(integerRequirement());
    algorithm.requirements.add(booleanRequirement());

    AlgorithmInitializer.forAlgorithm(algorithm,
        asList(Pair.of(KEY1, "true"), Pair.of(KEY1, "false"), Pair.of(KEY2, "66")),
        received::put).apply();

    assertThat(received.asMap())
        .contains(entry(KEY1, asList(true, false)))
        .contains(entry(KEY2, asList(66)));
  }

  @Test
  public void nonExistingIdentifier() {
    algorithm.requirements.add(integerRequirement());

    assertThatThrownBy(() -> initialize(Pair.of("ABC", "DEF")))
        .isExactlyInstanceOf(AlgorithmConfigurationException.class)
        .hasMessageContaining("no configuration requirement present");
  }

  @Test
  public void requirementWithDefaultValue() throws Exception {
    algorithm.requirements.add(defaultValueRequirement());

    initialize();

    assertThat(algorithm.recordedIdentifier).containsOnly(KEY1);
    assertThat(algorithm.recordedValues).containsExactly(new Boolean[]{true, false, true});
  }

  private ConfigurationRequirement<?> booleanRequirement() {
    return new ConfigurationRequirementBoolean(KEY1, 0, 2);
  }

  private ConfigurationRequirement<?> integerRequirement() {
    return new ConfigurationRequirementInteger(KEY2, 0, 1);
  }

  private ConfigurationRequirement<?> defaultValueRequirement() {
    final ConfigurationRequirementBoolean r = new ConfigurationRequirementBoolean(KEY1, 1, 3);
    r.setDefaultValues(new Boolean[]{true, false, true});
    return r;
  }

  private static class ExampleAlgorithm implements Algorithm,
      BooleanParameterAlgorithm,
      IntegerParameterAlgorithm {

    private List<String> recordedIdentifier = new ArrayList<>();
    private List<Object[]> recordedValues = new ArrayList<>();
    private List<ConfigurationRequirement<?>> requirements = new ArrayList<>();

    @Override
    public void setBooleanConfigurationValue(final String identifier, final Boolean... values) {
      recordedIdentifier.add(identifier);
      recordedValues.add(values);
    }

    @Override
    public void setIntegerConfigurationValue(final String identifier, final Integer... values) {
      recordedIdentifier.add(identifier);
      recordedValues.add(values);
    }

    private Map<String, Object[]> getRecorded() {
      final Map<String, Object[]> recorded = new HashMap<>();
      for (int index = 0; index < recordedIdentifier.size(); ++index) {
        recorded.put(recordedIdentifier.get(index), recordedValues.get(index));
      }
      return recorded;
    }

    @Override
    public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
      return new ArrayList<>(requirements);
    }

    @Override
    public void execute() throws AlgorithmExecutionException {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthors() {
      return null;
    }

    @Override
    public String getDescription() {
      return null;
    }
  }
}
