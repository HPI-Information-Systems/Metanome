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

package de.uni_potsdam.hpi.metanome.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.*;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SQLInputGenerator;
import de.uni_potsdam.hpi.metanome.input.csv.CsvFileGenerator;
import de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator;

import java.io.File;
import java.io.FileNotFoundException;

public class ConfigurationValueFactory {

    /**
     * @param specification
     * @return
     * @throws AlgorithmConfigurationException
     */
    public static ConfigurationValue createConfigurationValue(
            ConfigurationSpecification specification) throws AlgorithmConfigurationException {

        if (specification instanceof ConfigurationSpecificationBoolean)
            return new ConfigurationValueBoolean((ConfigurationSpecificationBoolean) specification);
        else if (specification instanceof ConfigurationSpecificationCsvFile)
            return new ConfigurationValueRelationalInputGenerator(specification.getIdentifier(),
                    createRelationalInputGenerators((ConfigurationSpecificationCsvFile) specification));
        else if (specification instanceof ConfigurationSpecificationSQLIterator) {
            return new ConfigurationValueSQLInputGenerator(specification.getIdentifier(),
                    createSqlIteratorGenerators((ConfigurationSpecificationSQLIterator) specification));
        } else if (specification instanceof ConfigurationSpecificationString)
            return new ConfigurationValueString((ConfigurationSpecificationString) specification);
        else
            throw new AlgorithmConfigurationException("Unsupported ConfigurationSpecification subclass.");
    }

    /**
     * @param specification
     * @return
     * @throws AlgorithmConfigurationException
     */
    private static SQLInputGenerator[] createSqlIteratorGenerators(
            ConfigurationSpecificationSQLIterator specification) throws AlgorithmConfigurationException {

        SqlIteratorGenerator[] sqlIteratorGenerators = new SqlIteratorGenerator[specification.getSettings().length];

        int i = 0;
        for (ConfigurationSettingSQLIterator setting : specification.getSettings()) {
            sqlIteratorGenerators[i] = new SqlIteratorGenerator(setting.getDbUrl(),
                    setting.getUsername(), setting.getPassword());
            i++;
        }
        return sqlIteratorGenerators;
    }

    /**
     * @param specification
     * @return
     * @throws FileNotFoundException
     * @throws AlgorithmConfigurationException
     */
    private static RelationalInputGenerator[] createRelationalInputGenerators(
            ConfigurationSpecificationCsvFile specification) throws AlgorithmConfigurationException {

        CsvFileGenerator[] csvFileGenerators = new CsvFileGenerator[specification.getSettings().length];

        int i = 0;
        for (ConfigurationSettingCsvFile setting : specification.getSettings()) {
            try {
                if (setting.isAdvanced())
                    csvFileGenerators[i] = new CsvFileGenerator(new File(setting.getFileName()), setting.getSeparatorChar(),
                            setting.getQuoteChar(), setting.getEscapeChar(), setting.getLine(),
                            setting.isStrictQuotes(), setting.isIgnoreLeadingWhiteSpace());
                else
                    csvFileGenerators[i] = new CsvFileGenerator(new File(setting.getFileName()));
            } catch (FileNotFoundException e) {
                throw new AlgorithmConfigurationException("Could not find CSV file.");
            }
            i++;
        }

        return csvFileGenerators;
    }

}
