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

package de.metanome.backend.helper;

/**
 * This class builds a detailed exception string.
 *
 * GWT does not support wrapper exceptions, so that the original cause of an exception is not known
 * in the frontend. However a string can be passed to the frontend with an exception. To give the
 * user the possibility to understand what went wrong, this class builds a detailed exception
 * string.
 */
public class ExceptionParser {

  /**
   * Builds a detailed exception string from the given exception.
   *
   * @param throwable the exception
   * @return the detailed exception string containing the message and a part of the stack trace
   */
  public static String parse(Throwable throwable) {
    return ExceptionParser.parse(throwable, "");
  }

  /**
   * Builds a details exception string with an optional additional message.
   *
   * @param throwable the exception
   * @param message   the additional message
   * @return the detailed exception string containing the message and a part of the stack trace
   */
  public static String parse(Throwable throwable, String message) {
    String lineSeparator = "<br/>";
    StringBuilder errorMessage = new StringBuilder();
    int max = throwable.getStackTrace().length;

    if (!message.isEmpty()) {
      errorMessage.append(message);
      errorMessage.append(lineSeparator);
    }

    // Throwable
    errorMessage.append(throwable.toString());
    errorMessage.append(lineSeparator);
    errorMessage.append("Message: ");
    errorMessage.append(throwable.getMessage());
    errorMessage.append(lineSeparator);

    // Stack Trace
    errorMessage.append("StackTrace:");
    errorMessage.append(lineSeparator);
    for (int i = 0; i < max; i++) {
      errorMessage.append(throwable.getStackTrace()[i].toString());
      errorMessage.append(lineSeparator);
    }

    return errorMessage.toString();
  }

}
