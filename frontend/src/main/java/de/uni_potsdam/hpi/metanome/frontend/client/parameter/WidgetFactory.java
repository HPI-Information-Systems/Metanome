package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationSQLIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;

public class WidgetFactory {

	public static InputParameterWidget buildWidget(ConfigurationSpecification config){
		System.out.println("Expected no. of values: " + config.getNumberOfValues());
		InputParameterWidget widget = null;
		if (config instanceof ConfigurationSpecificationBoolean)
			widget = new InputParameterBooleanWidget((ConfigurationSpecificationBoolean) config);
		else if (config instanceof ConfigurationSpecificationString)
			widget = new InputParameterStringWidget((ConfigurationSpecificationString) config);
		else if (config instanceof ConfigurationSpecificationCsvFile)
			widget = new InputParameterCsvFileWidget((ConfigurationSpecificationCsvFile) config);
		else if (config instanceof ConfigurationSpecificationSQLIterator)
			widget = new InputParameterSQLIteratorWidget((ConfigurationSpecificationSQLIterator) config);
		return widget;
	}
}
