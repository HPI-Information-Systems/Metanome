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

package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;

import java.io.Serializable;

/**
 * Represents a configuration parameter an {@link Algorithm} needs to be properly configured. The
 * ConfigurationSpecification is then used to construct a configuration value that is sent to the
 * {@link Algorithm} for configuration. Only type concrete ConfigurationSpecification subclasses
 * should be used to specify configuration parameters.
 *
 * @author Jakob Zwiener
 */
public abstract class ConfigurationSpecification implements Serializable {

  public static final int ARBITRARY_NUMBER_OF_VALUES = -1;
  private static final long serialVersionUID = 4312752686730530733L;
  protected String identifier;
  /**
   * would be good to make this final, but then it would not be serialized and thus be reset to 1 in
   * frontend
   */
  protected int numberOfValues;

  /**
   * Exists for GWT serialization.
   */
  public ConfigurationSpecification() {
  }

  /**
   * Construct a configuration specification. A string identifier is stored to identify
   * configuration parameter. The identifier should be unique among all parameters of one algorithm.
   * The number of requested values defaults to 1.
   *
   * @param identifier the specification's identifier
   */
  public ConfigurationSpecification(String identifier) {
    this(identifier, 1);
  }

  /**
   * Construct a configuration specification. A string identifier is stored to identify
   * configuration parameter. The identifier should be unique among all parameters of one algorithm.
   * The number of requested values is set. Use ARBITRARY_NUMBER_OF_VALUES to request arbitrary
   * number of values.
   *
   * @param identifier     the specification's identifier
   * @param numberOfValues the number of values expected
   */
  public ConfigurationSpecification(String identifier, int numberOfValues) {
    this.identifier = identifier;
    this.numberOfValues = numberOfValues;
  }

  /**
   * @return identifier
   */
  public String getIdentifier() {
    return identifier;
  }

  /**
   * @return numberOfValues
   */
  public int getNumberOfValues() {
    return numberOfValues;
  }

  //TODO use sth more specific than object
  public abstract Object[] getSettings();

}
