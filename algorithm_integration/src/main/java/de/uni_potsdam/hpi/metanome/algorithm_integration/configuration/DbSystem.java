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

/**
 * Enum representing database systems usable as input.
 *
 * @author Jakob Zwiener
 */
public enum DbSystem {
  DB2, MySQL, PostgreSQL, HANA, Oracle;

  /**
   * Returns a list of string representation of all options of the enum.
   *
   * @return string representations of all options
   */
  public static String[] names() {
    DbSystem[] systems = values();
    String[] names = new String[systems.length];

    for (int i = 0; i < systems.length; i++) {
      names[i] = systems[i].name();
    }

    return names;
  }
}
