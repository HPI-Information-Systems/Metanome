/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.backend.result_postprocessing.helper;


import de.metanome.backend.algorithm_loading.InputDataFinder;

public class StringHelper {

  /**
   * Removes the file ending "csv" and "tsv" from the given string.
   * @param fileName the file name
   * @return the file name without file ending
   */
  public static String removeFileEnding(String fileName) {
    for (String fileEnding : InputDataFinder.ACCEPTED_FILE_ENDINGS) {
      if (fileName.endsWith(fileEnding)) {
        return fileName.substring(0, fileName.length() - 4);
      }
    }
    return fileName;
  }

  /**
   * Removes brackets from the column name.
   * @param columnName the column name
   * @return the cleaned column name
   */
  public static String cleanColumnName(String columnName) {
    if (columnName.startsWith("[")) {
      columnName = columnName.substring(1, columnName.length());
    }
    if (columnName.endsWith("]")) {
      columnName = columnName.substring(0, columnName.length() - 1);
    }
    return columnName;
  }

}
