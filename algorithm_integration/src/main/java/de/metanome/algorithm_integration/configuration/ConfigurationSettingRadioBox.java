package de.metanome.algorithm_integration.configuration;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The setting of a {@link ConfigurationRequirementRadioBox}
 *
 * @author Maxi Fischer
 */
@JsonTypeName("ConfigurationSettingRadioBox")
public class ConfigurationSettingRadioBox extends ConfigurationSettingPrimitive<String> {

    private static final long serialVersionUID = -4661885076384316960L;

    // Needed for restful serialization
    public String type = "ConfigurationSettingRadioBox";

    public ConfigurationSettingRadioBox() {
    }

    public ConfigurationSettingRadioBox(String value) {
        super(value);
    }

}
