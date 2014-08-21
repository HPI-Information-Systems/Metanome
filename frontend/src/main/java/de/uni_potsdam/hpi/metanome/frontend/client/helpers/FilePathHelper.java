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
 * Helper class to extract the file name and path from a string.
 */
public class FilePathHelper {

  /**
   * Extracts the file name from the file path and returns it.
   * @param path the complete file path
   * @return the file name
   */
  public static String getFileName(String path) {
    String[] fileParts = path.replace("\\", "/").split("/");
    return fileParts[fileParts.length - 1];
  }

  /**
   *
   * @param path the complete file path with file name
   * @return the path without file name
   */
  public static String getFilePath(String path) {
    String[] fileParts = path.replace("\\", "/").split("/");
    return path.replace(fileParts[fileParts.length - 1], "");
  }

}
