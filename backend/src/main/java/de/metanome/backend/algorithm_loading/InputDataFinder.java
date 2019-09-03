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
package de.metanome.backend.algorithm_loading;

import de.metanome.backend.constants.Constants;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Searches for input files in Metanome's input data directory.
 */
public class InputDataFinder {

  /**
   * Returns all possible input files from Metanome's input file directory.
   * @param dir decides whether all directories and files or just all files are returned
   *
   * @return an array of input files
   * @throws UnsupportedEncodingException when file path is not utf-8 decoded
   */
  public File[] getAvailableFiles(boolean dir) throws UnsupportedEncodingException {
    String pathToFolder = "";
    try {
      pathToFolder = Thread.currentThread().getContextClassLoader().getResource(Constants.INPUTDATA_RESOURCE_NAME).getPath();
    } catch (NullPointerException e) {
      // The input data folder does not exist
      return new File[]{};
    }

    return retrieveCsvTsvFiles(pathToFolder, dir);
  }

  /**
   * Returns a String with the path of Metanome's input file directory.
   *
   * @return String with path to algorithm directory
   */

  public String getFileDirectory() {
    String pathToFolder = "";
    try {
      pathToFolder = Thread.currentThread().getContextClassLoader().getResource(Constants.INPUTDATA_RESOURCE_NAME).getPath();
    } catch (NullPointerException e) {
      throw new NullPointerException("Input Data Directory does not exist");
    }
    return pathToFolder;
  }

  /**
   * Retrieves all csv and tsv files located directly in the given directory.
   *
   * @param pathToFolder path to the folder to be searched in
   * @param dir decides whether all directories and files or just all files are returned
   * @return names of all CSV and TSV files located directly in the given directory (no subfolders)
   * @throws UnsupportedEncodingException when file path is not utf-8 decoded
   */
  File[] retrieveCsvTsvFiles(String pathToFolder, boolean dir) throws UnsupportedEncodingException {
    File folder = new File(URLDecoder.decode(pathToFolder, Constants.FILE_ENCODING));
    List<File> allFiles = new ArrayList<>();
    File[] currentFiles = folder.listFiles();

      if (currentFiles != null) {
          for (File currentFile : currentFiles) {
              /**
               * adds files of sub directories to file overview
               */
              if (currentFile.isDirectory()) {
                  File[] dirFiles = retrieveCsvTsvFiles(currentFile.getPath(), dir);
                  if (dir == true) {
                      allFiles.add(currentFile);
                  }
                  Collections.addAll(allFiles, dirFiles);
              } else if (currentFile.isFile()) {
                  for (String fileEnding : Constants.ACCEPTED_FILE_ENDINGS_ARRAY) {
                      if (currentFile.getName().endsWith(fileEnding)) {
                          allFiles.add(currentFile);
                      }
                  }
              }
          }
      } else {
          return new File[0];
      }
      return allFiles.toArray(new File[allFiles.size()]);

  }

}
