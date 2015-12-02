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

package de.metanome.backend.result_receiver;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class ResultReceiver implements CloseableOmniscientResultReceiver {

  public static final String RESULT_TEST_DIR = "results" + File.separator + "test";
  public static final String RESULT_DIR = "results";

  protected String algorithmExecutionIdentifier;
  protected String directory;
  protected Boolean testDirectory;

  public ResultReceiver(String algorithmExecutionIdentifier)
    throws FileNotFoundException {
    this(algorithmExecutionIdentifier, false);
  }

  protected ResultReceiver(String algorithmExecutionIdentifier, Boolean testDirectory)
    throws FileNotFoundException {
    this.testDirectory = testDirectory;

    if (testDirectory) {
      this.directory = RESULT_TEST_DIR;
    } else {
      this.directory = RESULT_DIR;
    }

    File directory = new File(this.directory);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    this.algorithmExecutionIdentifier = algorithmExecutionIdentifier;
  }

  public void setResultTestDir() {
    this.testDirectory = true;
    this.directory = RESULT_TEST_DIR;

    File directory = new File(this.directory);
    if (!directory.exists()) {
      directory.mkdirs();
    }
  }

  public String getOutputFilePathPrefix() {
    return this.directory + "/" + this.algorithmExecutionIdentifier;
  }

}
