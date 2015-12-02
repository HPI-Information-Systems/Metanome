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

package de.metanome.backend.algorithm_loading;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Searches for input files in Metanome's input data directory.
 */
public class InputDataFinder {

  public static final String[] ACCEPTED_FILE_ENDINGS = new String[]{".csv", ".tsv"};

  /**
   * Returns all possible input files from Metanome's input file directory.
   *
   * @return an array of input files
   * @throws UnsupportedEncodingException when file path is not utf-8 decoded
   */
  public File[] getAvailableFiles() throws UnsupportedEncodingException {
    String
      pathToFolder =
      Thread.currentThread().getContextClassLoader().getResource("inputData").getPath();

    return retrieveCsvTsvFiles(pathToFolder);
  }

  /**
   * Retrieves all csv and tsv files located directly in the given directory.
   *
   * @param pathToFolder path to the folder to be searched in
   * @return names of all CSV and TSV files located directly in the given directory (no subfolders)
   * @throws UnsupportedEncodingException when file path is not utf-8 decoded
   */
  protected File[] retrieveCsvTsvFiles(String pathToFolder) throws UnsupportedEncodingException {
    File folder = new File(URLDecoder.decode(pathToFolder, "utf-8"));
    return folder.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File file, String name) {
        for (String fileEnding : ACCEPTED_FILE_ENDINGS) {
          if (name.endsWith(fileEnding)) {
            return true;
          }
        }
        return false;
      }
    });
  }

}
