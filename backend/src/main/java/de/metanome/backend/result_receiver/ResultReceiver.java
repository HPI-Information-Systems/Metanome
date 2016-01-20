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

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.results.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public abstract class ResultReceiver implements CloseableOmniscientResultReceiver {

  public static final String RESULT_TEST_DIR = "results" + File.separator + "test";
  public static final String RESULT_DIR = "results";

  protected String algorithmExecutionIdentifier;
  protected String directory;
  protected Boolean testDirectory;
  protected List<String> acceptableColumnNames;

  public ResultReceiver(String algorithmExecutionIdentifier, List<String> acceptableColumnNames)
    throws FileNotFoundException {
    this(algorithmExecutionIdentifier, acceptableColumnNames, false);
  }

  protected ResultReceiver(String algorithmExecutionIdentifier, List<String> acceptableColumnNames, Boolean testDirectory)
    throws FileNotFoundException {
    this.testDirectory = testDirectory;
    this.acceptableColumnNames = acceptableColumnNames;

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

  /**
   * Check if the table/column names of the given result are contained in the accepted column names.
   * @param result the result
   * @return true, if the names are accepted, false otherwise
   */
  protected Boolean acceptedResult(FunctionalDependency result) {
    if (this.acceptableColumnNames == null) {
      return true;
    }
    if (!this.acceptableColumnNames.contains(result.getDependant().toString())) {
      return false;
    }
    for (ColumnIdentifier ci : result.getDeterminant().getColumnIdentifiers()) {
      if (! this.columnAccepted(ci.toString())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if the table/column names of the given result are contained in the accepted column names.
   * @param result the result
   * @return true, if the names are accepted, false otherwise
   */
  protected Boolean acceptedResult(UniqueColumnCombination result) {
    if (this.acceptableColumnNames == null) {
      return true;
    }
    for (ColumnIdentifier ci : result.getColumnCombination().getColumnIdentifiers()) {
      if (! this.columnAccepted(ci.toString())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if the table/column names of the given result are contained in the accepted column names.
   * @param result the result
   * @return true, if the names are accepted, false otherwise
   */
  protected Boolean acceptedResult(InclusionDependency result) {
    if (this.acceptableColumnNames == null) {
      return true;
    }
    for (ColumnIdentifier ci : result.getDependant().getColumnIdentifiers()) {
      if (! this.columnAccepted(ci.toString())) {
        return false;
      }
    }
    for (ColumnIdentifier ci : result.getReferenced().getColumnIdentifiers()) {
      if (! this.columnAccepted(ci.toString())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if the table/column names of the given result are contained in the accepted column names.
   * @param result the result
   * @return true, if the names are accepted, false otherwise
   */
  protected Boolean acceptedResult(OrderDependency result) {
    if (this.acceptableColumnNames == null) {
      return true;
    }
    for (ColumnIdentifier ci : result.getLhs().getColumnIdentifiers()) {
      if (! this.columnAccepted(ci.toString())) {
        return false;
      }
    }
    for (ColumnIdentifier ci : result.getRhs().getColumnIdentifiers()) {
      if (! this.columnAccepted(ci.toString())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if the table/column names of the given result are contained in the accepted column names.
   * @param result the result
   * @return true, if the names are accepted, false otherwise
   */
  protected Boolean acceptedResult(ConditionalUniqueColumnCombination result) {
    if (this.acceptableColumnNames == null) {
      return true;
    }
    for (ColumnIdentifier ci : result.getColumnCombination().getColumnIdentifiers()) {
      if (! this.columnAccepted(ci.toString())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if the table/column names of the given result are contained in the accepted column names.
   * @param result the result
   * @return true, if the names are accepted, false otherwise
   */
  protected Boolean acceptedResult(BasicStatistic result) {
    if (this.acceptableColumnNames == null) {
      return true;
    }
    for (ColumnIdentifier ci : result.getColumnCombination().getColumnIdentifiers()) {
      if (! this.columnAccepted(ci.toString())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the given column name is accepted, i.e., if the column name is the same
   * as in the input.
   * @param columnName the column name
   * @return true, if the name is accepted, false otherwise
   */
  private Boolean columnAccepted(String columnName) {
    return this.acceptableColumnNames.contains(columnName);
  }
}
