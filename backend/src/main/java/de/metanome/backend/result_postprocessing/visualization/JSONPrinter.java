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
package de.metanome.backend.result_postprocessing.visualization;

import org.json.simple.JSONAware;

import java.io.*;

/**
 * Allows to print result structures for visualizations as JSON to use them in D3 later.
 */
public class JSONPrinter {

  /**
   * Writes a JSON structure to file
   *
   * @param filePath   File path to the output file
   * @param jsonObject JSON structure which should be printed
   */
  public static void writeToFile(String filePath, JSONAware jsonObject) {
    try {
      File file = new File(filePath);
      file.mkdirs();
      if (file.exists()) {
        file.delete();
      }
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write(jsonObject.toJSONString());
      fileWriter.flush();
      fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Clear all the content in the given file.
   *
   * @param filePath the file path
   */
  public static void clearFile(String filePath) {
    try {
      PrintWriter writer = new PrintWriter(filePath);
      writer.print("");
      writer.close();
    } catch (FileNotFoundException e) {
      // File does not exists, can not be cleared.
    }
  }

}
