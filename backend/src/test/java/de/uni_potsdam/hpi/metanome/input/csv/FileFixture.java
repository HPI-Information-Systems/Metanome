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

package de.uni_potsdam.hpi.metanome.input.csv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Creates a file for testing. The file content is stored upon construction and every call to {@link
 * de.uni_potsdam.hpi.metanome.input.csv.FileFixture#getTestData(String)} writes and returns a file
 * with the content to the given path.
 *
 * @author Jakob Zwiener
 */
public class FileFixture {

  protected String fileData;

  public FileFixture(String fileData) {
    this.fileData = fileData;
  }

  public File getTestData(String fileName)
      throws FileNotFoundException, UnsupportedEncodingException {
    String filePath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    filePath += fileName;
    File file = new File(URLDecoder.decode(filePath, "utf-8"));
    // Mark files for deletion once vm exits.
    file.deleteOnExit();

    PrintWriter writer = new PrintWriter(file);

    writer.print(fileData);
    writer.close();

    return file;
  }
}
