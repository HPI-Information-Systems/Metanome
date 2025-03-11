/**
 * Copyright 2014-2017 by Metanome Project
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

import com.fasterxml.jackson.core.JsonProcessingException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.*;
import de.metanome.backend.results_db.ResultType;

import java.io.*;
import java.util.*;

/**
 * Writes all received Results immediately to disk. When all results were received, the results are read again
 * and returned.
 */
public class ResultPrinter extends ResultReceiver {

  protected static final String TABLE_MARKER = "# TABLES";
  protected static final String COLUMN_MARKER = "# COLUMN";
  protected static final String RESULT_MARKER = "# RESULTS";

  protected EnumMap<ResultType, PrintStream> openStreams;
  protected EnumMap<ResultType, Boolean> headerWritten;
  protected Map<String, String> columnMapping;
  protected Map<String, String> tableMapping;

  /**
   * Initializes the result printer. The given algorithm execution identifier and accepted columns are stored.
   * If the result receiver receives a result, which consists of a column identifier, which is not listed in the
   * accepted columns, an exception will be thrown. If you do not want the result receiver to check the result for
   * matching columns, set the accepted columns to 'null'.
   * @param algorithmExecutionIdentifier the algorithm execution identifier
   * @param acceptedColumns              a list of accepted column identifiers
   * @throws FileNotFoundException if the directory, where all results are stored on disk in a file, could not be found
   */
  public ResultPrinter(String algorithmExecutionIdentifier, List<ColumnIdentifier> acceptedColumns)
    throws FileNotFoundException {
    super(algorithmExecutionIdentifier, acceptedColumns);
    this.headerWritten = new EnumMap<>(ResultType.class);
    this.openStreams = new EnumMap<>(ResultType.class);
    this.columnMapping = new HashMap<>();
    this.tableMapping = new HashMap<>();

    if (this.acceptedColumns != null) {
      this.initializeMappings();
    }
  }

  /**
   * Initializes the result printer. The given algorithm execution identifier and accepted columns are stored.
   * If the result receiver receives a result, which consists of a column identifier, which is not listed in the
   * accepted columns, an exception will be thrown. If you do not want the result receiver to check the result for
   * matching columns, set the accepted columns to 'null'.
   * @param algorithmExecutionIdentifier the algorithm execution identifier
   * @param acceptedColumns              a list of accepted column identifiers
   * @param test                         if true, a test directory is used to store the results on disk
   * @throws FileNotFoundException if the directory, where all results are stored on disk in a file, could not be found
   */
  protected ResultPrinter(String algorithmExecutionIdentifier, List<ColumnIdentifier> acceptedColumns, Boolean test)
    throws FileNotFoundException {
    super(algorithmExecutionIdentifier, acceptedColumns, test);
    this.headerWritten = new EnumMap<>(ResultType.class);
    this.openStreams = new EnumMap<>(ResultType.class);
    this.columnMapping = new HashMap<>();
    this.tableMapping = new HashMap<>();

    if (this.acceptedColumns != null) {
      this.initializeMappings();
    }
  }

  @Override
  public void receiveResult(BasicStatistic statistic)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(statistic)) {
      try {
        JsonConverter<BasicStatistic> jsonConverter = new JsonConverter<>();
        getStream(ResultType.BASIC_STAT).println(jsonConverter.toJsonString(statistic));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(functionalDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.FD)) {
            this.writeHeader(ResultType.FD);
          }
          String str = functionalDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.FD).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<FunctionalDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.FD).println(jsonConverter.toJsonString(functionalDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }
  
  @Override
  public void receiveResult(ConditionalInclusionDependency conditionalDependency)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(conditionalDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.CID)) {
            this.writeHeader(ResultType.CID);
          }
          String str = conditionalDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.CID).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<ConditionalInclusionDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.CID).println(jsonConverter.toJsonString(conditionalDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(RelaxedInclusionDependency relaxedDependency)
          throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(relaxedDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.RIND)) {
            this.writeHeader(ResultType.RIND);
          }
          String str = relaxedDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.RIND).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<RelaxedInclusionDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.RIND).println(jsonConverter.toJsonString(relaxedDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(MatchingDependency matchingDependency)
      throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(matchingDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.MD)) {
            this.writeHeader(ResultType.MD);
          }
          String str = matchingDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.MD).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<MatchingDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.MD).println(jsonConverter.toJsonString(matchingDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(ConditionalFunctionalDependency conditionalFunctionalDependency)
          throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(conditionalFunctionalDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.CFD)) {
            this.writeHeader(ResultType.CFD);
          }
          String str = conditionalFunctionalDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.CFD).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<ConditionalFunctionalDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.CFD).println(jsonConverter.toJsonString(conditionalFunctionalDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(RelaxedFunctionalDependency relaxedFunctionalDependency)
          throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(relaxedFunctionalDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.RFD)) {
            this.writeHeader(ResultType.RFD);
          }
          String str = relaxedFunctionalDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.RFD).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<RelaxedFunctionalDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.RFD).println(jsonConverter.toJsonString(relaxedFunctionalDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(MultivaluedDependency multivaluedDependency)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(multivaluedDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.MVD)) {
            this.writeHeader(ResultType.MVD);
          }
          String str = multivaluedDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.MVD).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<MultivaluedDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.MVD).println(jsonConverter.toJsonString(multivaluedDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(InclusionDependency inclusionDependency)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(inclusionDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.IND)) {
            this.writeHeader(ResultType.IND);
          }
          String str = inclusionDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.IND).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<InclusionDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.IND).println(jsonConverter.toJsonString(inclusionDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(uniqueColumnCombination)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.UCC)) {
            this.writeHeader(ResultType.UCC);
          }
          String str = uniqueColumnCombination.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.UCC).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<UniqueColumnCombination> jsonConverter = new JsonConverter<>();
          getStream(ResultType.UCC).println(jsonConverter.toJsonString(uniqueColumnCombination));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(conditionalUniqueColumnCombination)) {
      try {
        JsonConverter<ConditionalUniqueColumnCombination> jsonConverter = new JsonConverter<>();
        getStream(ResultType.CUCC)
          .println(jsonConverter.toJsonString(conditionalUniqueColumnCombination));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(RelaxedUniqueColumnCombination relaxedUniqueColumnCombination)
          throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(relaxedUniqueColumnCombination)) {
      try {
        JsonConverter<RelaxedUniqueColumnCombination> jsonConverter = new JsonConverter<>();
        getStream(ResultType.RUCC)
                .println(jsonConverter.toJsonString(relaxedUniqueColumnCombination));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  @Override
  public void receiveResult(OrderDependency orderDependency)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(orderDependency)) {
      if (this.acceptedColumns != null) {
        // write a customize string
        try {
          if (!getHeaderWritten(ResultType.OD)) {
            this.writeHeader(ResultType.OD);
          }
          String str = orderDependency.toString(this.tableMapping, this.columnMapping);
          getStream(ResultType.OD).println(str);
        } catch (Exception e) {
          throw new CouldNotReceiveResultException("Could not convert the result to string!");
        }
      } else {
        // write JSON to file
        // the acceptableColumnNames are null, that means a database connection was used
        // we do not know which columns are in the result
        try {
          JsonConverter<OrderDependency> jsonConverter = new JsonConverter<>();
          getStream(ResultType.OD).println(jsonConverter.toJsonString(orderDependency));
        } catch (JsonProcessingException e) {
          throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
        }
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }
  
  @Override
  public void receiveResult(DenialConstraint dc)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(dc)) {
      try {
        JsonConverter<DenialConstraint> jsonConverter = new JsonConverter<>();
        getStream(ResultType.DC).println(jsonConverter.toJsonString(dc));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The column name of the result does not match with the column names in the input!");
    }
  }

  protected PrintStream getStream(ResultType type) throws CouldNotReceiveResultException {
    if (!openStreams.containsKey(type)) {
      openStreams.put(type, openStream(type.getEnding()));
    }
    return openStreams.get(type);
  }

  protected PrintStream openStream(String fileSuffix) throws CouldNotReceiveResultException {
    try {
      return new PrintStream(new FileOutputStream(getOutputFilePathPrefix() + fileSuffix), true);
    } catch (FileNotFoundException e) {
      throw new CouldNotReceiveResultException("Could not open result file for writing", e);
    }
  }

  protected Boolean getHeaderWritten(ResultType type) throws CouldNotReceiveResultException {
    if (!this.headerWritten.containsKey(type)) {
      this.headerWritten.put(type, false);
    }
    return this.headerWritten.get(type);
  }

  private void writeHeader(ResultType resultType) throws CouldNotReceiveResultException {
    PrintStream stream = getStream(resultType);

    stream.println(TABLE_MARKER);
    for (Map.Entry<String, String> entry : this.tableMapping.entrySet()) {
      stream.println(entry.getKey() + MAPPING_SEPARATOR + entry.getValue());
    }

    stream.println(COLUMN_MARKER);
    for (Map.Entry<String, String> entry : this.columnMapping.entrySet()) {
      stream.println(entry.getKey() + MAPPING_SEPARATOR + entry.getValue());
    }

    stream.println(RESULT_MARKER);

    this.headerWritten.put(resultType, true);
  }

  @Override
  public void close() throws IOException {
    for (PrintStream stream : openStreams.values()) {
      stream.close();
    }
  }

  /**
   * Reads the results from disk and returns them.
   *
   * @return all results
   * @throws java.io.IOException if file could not be read
   */
  public List<Result> getResults()
    throws IOException, NullPointerException, IndexOutOfBoundsException {
    List<Result> results = new ArrayList<>();

    for (ResultType type : openStreams.keySet()) {
      if (existsFile(type.getEnding())) {
        String fileName = getOutputFilePathPrefix() + type.getEnding();
        results.addAll(ResultReader.readResultsFromFile(fileName, type.getName()));
      }
    }

    return results;
  }

  private Boolean existsFile(String fileSuffix) {
    return new File(getOutputFilePathPrefix() + fileSuffix).exists();
  }

  private void initializeMappings() throws IndexOutOfBoundsException {
    int tableCounter = 1;
    int columnCounter = 1;

    for (ColumnIdentifier ci : this.acceptedColumns) {
      String tableName = ci.getTableIdentifier();
      String columnName = ci.getColumnIdentifier();

      if (!this.tableMapping.containsKey(tableName)) {
        this.tableMapping.put(tableName, String.valueOf(tableCounter));
        tableCounter++;
      }

      String tableValue = this.tableMapping.get(tableName);
      columnName = tableValue + ColumnIdentifier.TABLE_COLUMN_CONCATENATOR + columnName;

      if (!this.columnMapping.containsKey(columnName)) {
        this.columnMapping.put(columnName, String.valueOf(columnCounter));
        columnCounter++;
      }
    }
  }

}
