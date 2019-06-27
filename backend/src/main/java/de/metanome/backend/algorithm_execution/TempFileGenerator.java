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
package de.metanome.backend.algorithm_execution;

import de.metanome.algorithm_integration.algorithm_execution.FileCreationException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.backend.constants.Constants;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;

/**
 * The generator builds empty, readable, writable files with random file names, that are deleted on
 * close.
 *
 * @author Jakob Zwiener
 */
public class TempFileGenerator implements FileGenerator {

  protected String pathToFolder;
  protected List<File> createdFiles;

  public TempFileGenerator() throws UnsupportedEncodingException {
    // Get path to resource dir.
    String pathToFolder = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    pathToFolder += Constants.FILE_SEPARATOR + Constants.TEMP_FILE_PATH;
    this.pathToFolder = URLDecoder.decode(pathToFolder, Constants.FILE_ENCODING);
    // Create subdir.
    new File(this.pathToFolder).mkdirs();

    this.createdFiles = new LinkedList<>();
  }

  @Override
  public File getTemporaryFile() throws FileCreationException {
    String fileName = RandomStringUtils.randomAlphanumeric(Constants.FILE_NAME_LENGTH).toLowerCase();
    File tempFile = new File(pathToFolder + Constants.FILE_SEPARATOR + fileName);

    try {
      tempFile.createNewFile();
    } catch (IOException e) {
      throw new FileCreationException("Could not create temporary file.");
    }

    // Mark the file for deletion on vm exit.
    tempFile.deleteOnExit();
    // Remember the file to be deleted on close.
    createdFiles.add(tempFile);

    return tempFile;
  }

  @Override
  public void close() {
    for (File tempFile : createdFiles) {
      try {
        tempFile.delete();
      } catch (Exception e) {
        System.out.println("A file could not be deleted.");
      }
    }
  }
}
