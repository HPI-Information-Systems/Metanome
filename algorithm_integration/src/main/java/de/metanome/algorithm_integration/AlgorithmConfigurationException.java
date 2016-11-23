/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithm_integration;

/**
 * Exception to signal an error while configuring the algorithm.
 *
 * @author Jakob Zwiener
 */
public class AlgorithmConfigurationException extends AlgorithmExecutionException {

  private static final long serialVersionUID = 846178386204773632L;

  public AlgorithmConfigurationException() {
    super();
  }

  public AlgorithmConfigurationException(String message) {
    super(message);
  }

  public AlgorithmConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

}
