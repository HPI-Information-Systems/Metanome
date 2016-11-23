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
package de.metanome.backend.input.file;

import java.io.*;

/**
 * Creates a file for testing. The file content is stored upon construction and every call to {@link
 * de.metanome.backend.input.file.FileFixture#getTestData(String)} writes and returns a file with
 * the content to the given path.
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
    File file = new File(System.getProperty("java.io.tmpdir"), fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // Mark files for deletion once vm exits.
    file.deleteOnExit();

    PrintWriter writer = new PrintWriter(file);

    writer.print(fileData);
    writer.close();

    return file;
  }
}
