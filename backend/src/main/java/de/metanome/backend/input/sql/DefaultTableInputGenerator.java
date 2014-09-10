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

package de.metanome.backend.input.sql;


import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;

public class DefaultTableInputGenerator implements RelationalInputGenerator {

  protected SqlIteratorGenerator sqlIteratorGenerator;
  protected String tableName;

  public DefaultTableInputGenerator(SqlIteratorGenerator sqlIteratorGenerator) {
    this.sqlIteratorGenerator = sqlIteratorGenerator;
  }

  public DefaultTableInputGenerator(ConfigurationSettingTableInput setting)
      throws AlgorithmConfigurationException {
    this.tableName = setting.getTable();
    this.sqlIteratorGenerator = new SqlIteratorGenerator(setting.getDatabaseConnection());
  }

  @Override
  public RelationalInput generateNewCopy() throws InputGenerationException {
    return sqlIteratorGenerator.generateRelationalInputFromSql(buildStatement());
  }

  private String buildStatement() {
    return null;
  }
}
