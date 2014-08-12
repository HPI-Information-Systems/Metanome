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

package de.uni_potsdam.hpi.metanome.frontend.client.helpers;

/**
 * TODO docs
 *
 * @author Claudia Exeler
 */
public class StringHelper {

  /**
   * TODO docs
   *
   * @param input input string
   * @return the first char of the input string
   */
  public static char getFirstCharFromInput(String input) {
    if (input.length() == 1) {
      return input.charAt(0);
    } else if (input.equals("\\n")) {
      return '\n';
    } else if (input.equals("\\t")) {
      return '\t';
    } else {
      return 0;
    }
  }

  /**
   * Returns one character from input string.
   *
   * @param value input string
   * @return first character
   * @throws InputValidationException thrown if no character was extracted from value.
   */
  public static char getValidatedInput(String value) throws InputValidationException {
    char firstChar = getFirstCharFromInput(value);
    if (firstChar == 0) {
      throw new InputValidationException(
          "You must specify one-character values for advanced CSV settings.");
    }
    return firstChar;
  }
}
