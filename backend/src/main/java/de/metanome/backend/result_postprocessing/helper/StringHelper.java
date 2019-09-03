/**
 * Copyright 2015-2016 by Metanome Project
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


import de.metanome.backend.constants.Constants;

public class StringHelper {

  /**
   * Removes the file ending "csv" and "tsv" from the given string.
   *
   * @param fileName the file name
   * @return the file name without file ending
   */
  public static String removeFileEnding(String fileName) {
    for (String fileEnding : Constants.ACCEPTED_FILE_ENDINGS_ARRAY) {
      if (fileName.endsWith(fileEnding)) {
        return fileName.substring(0, fileName.length() - 4);
      }
    }
    return fileName;
  }

}
