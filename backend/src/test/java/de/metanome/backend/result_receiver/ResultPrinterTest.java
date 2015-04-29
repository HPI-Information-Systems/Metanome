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

package de.metanome.backend.result_receiver;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.JsonConverter;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.algorithm_integration.results.OrderDependency.ComparisonOperator;
import de.metanome.algorithm_integration.results.OrderDependency.OrderType;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.results_db.ResultType;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link ResultPrinter}
 *
 * @author Jakob Zwiener
 */
public class ResultPrinterTest {

  protected String testAlgoExecutionIdentifier;
  protected ResultPrinter printer;

  @Before
  public void setUp() throws Exception {
    testAlgoExecutionIdentifier = "testAlgoExecution";
    printer = new ResultPrinter(testAlgoExecutionIdentifier, true);
  }

  @After
  public void tearDown() throws Exception {
    printer.close();
    FileUtils.deleteDirectory(new File(ResultPrinter.RESULT_TEST_DIR).getParentFile());
  }

  /**
   * Test method for {@link ResultPrinter#receiveResult(BasicStatistic)} <p/> Received {@link
   * BasicStatistic}s should be written to the appropriate file.
   */
  @Test
  public void testWriteBasicStatistic() throws CouldNotReceiveResultException, IOException {
    // Expected values
    BasicStatistic
        expectedStat =
        new BasicStatistic("Min", "minValue", new ColumnIdentifier("table1", "column2"));

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.STAT));

    // Execute functionality
    printer.receiveResult(expectedStat);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + "_stats");
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    JsonConverter<BasicStatistic> jsonConverter = new JsonConverter<>();
    assertTrue(fileContent.contains(jsonConverter.toJsonString(expectedStat)));

    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedStat));

    // Cleanup
    actualFile.delete();
  }

  /**
   * Test method for {@link ResultPrinter#receiveResult(FunctionalDependency)} <p/> Received {@link
   * FunctionalDependency}s should be written to the appropriate file.
   */
  @Test
  public void testWriteFunctionalDependency() throws CouldNotReceiveResultException, IOException {
    // Expected values
    FunctionalDependency expectedFd = new FunctionalDependency(
        new ColumnCombination(
            new ColumnIdentifier("table1", "column2")),
        new ColumnIdentifier("table1", "column23")
    );

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.FD));

    // Execute functionality
    printer.receiveResult(expectedFd);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + "_fds");
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    JsonConverter<FunctionalDependency> jsonConverter = new JsonConverter<>();
    assertTrue(fileContent.contains(jsonConverter.toJsonString(expectedFd)));

    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedFd));

    // Cleanup
    actualFile.delete();
  }

  /**
   * Test method for {@link ResultPrinter#receiveResult(InclusionDependency)} <p/> Received {@link
   * InclusionDependency}s should be written to the appropriate file.
   */
  @Test
  public void testWriteInclusionDependency() throws CouldNotReceiveResultException, IOException {
    // Expected values
    InclusionDependency expectedInd = new InclusionDependency(
        new ColumnPermutation(
            new ColumnIdentifier("table1", "column2")),
        new ColumnPermutation(
            new ColumnIdentifier("table2", "column23"))
    );

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.IND));

    // Execute functionality
    printer.receiveResult(expectedInd);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + "_inds");
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    JsonConverter<InclusionDependency> jsonConverter = new JsonConverter<>();
    assertTrue(fileContent.contains(jsonConverter.toJsonString(expectedInd)));

    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedInd));

    // Cleanup
    actualFile.delete();
  }

  /**
   * Test method for {@link ResultPrinter#receiveResult(UniqueColumnCombination)} <p/> Received
   * {@link UniqueColumnCombination}s should be written to the appropriate file.
   */
  @Test
  public void testWriteUniqueColumnCombination()
      throws CouldNotReceiveResultException, IOException {
    // Expected values
    UniqueColumnCombination
        expectedUcc =
        new UniqueColumnCombination(new ColumnIdentifier("table1", "column2"));

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.UCC));

    // Execute functionality
    printer.receiveResult(expectedUcc);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + "_uccs");
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    JsonConverter<UniqueColumnCombination> jsonConverter = new JsonConverter<>();
    assertTrue(fileContent.contains(jsonConverter.toJsonString(expectedUcc)));

    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedUcc));

    // Cleanup
    actualFile.delete();
  }

  /**
   * Test method for {@link ResultPrinter#receiveResult(OrderDependency)} <p/> Received {@link
   * OrderDependency}s should be written to the appropriate file.
   */
  @Test
  public void testWriteOrderDependency() throws CouldNotReceiveResultException, IOException {
    // Expected values
    OrderDependency expectedOd =
        new OrderDependency(new ColumnPermutation(
            new ColumnIdentifier("table1", "column2")),
                            new ColumnPermutation(
                                new ColumnIdentifier("table1", "column23")),
                            OrderType.LEXICOGRAPHICAL,
                            ComparisonOperator.SMALLER_EQUAL);

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.OD));

    // Execute functionality
    printer.receiveResult(expectedOd);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + "_ods");
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    JsonConverter<OrderDependency> jsonConverter = new JsonConverter<>();
    assertTrue(fileContent.contains(jsonConverter.toJsonString(expectedOd)));

    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedOd));

    // Cleanup
    actualFile.delete();
  }

  /**
   * Test method for {@link ResultPrinter#close()} <p/> Even if no streams are set the {@link
   * ResultPrinter} should be closeable.
   */
  @Test
  public void testClose() throws IOException {
    // Test close on printer without set streams.
    printer.close();
  }

  /**
   * Test method for {@link ResultPrinter#close()} <p/> If the statStream is set it should be closed
   * upon close.
   */
  @Test
  public void testCloseSetStatStream() throws IOException {
    // Setup
    // Expected values
    PrintStream statStream = mock(PrintStream.class);
    printer.openStreams.put(ResultType.STAT, statStream);

    // Execute functionality
    printer.close();

    // Check result
    verify(statStream).close();
  }

  /**
   * Test method for {@link ResultPrinter#close()} <p/> If the fdStream is set it should be closed
   * upon close.
   */
  @Test
  public void testCloseSetFdStream() throws IOException {
    // Setup
    // Expected values
    PrintStream fdStream = mock(PrintStream.class);
    printer.openStreams.put(ResultType.FD, fdStream);

    // Execute functionality
    printer.close();

    // Check result
    verify(fdStream).close();
  }

  /**
   * Test method for {@link ResultPrinter#close()} <p/> If the indStream is set it should be closed
   * upon close.
   */
  @Test
  public void testCloseSetIndStream() throws IOException {
    // Setup
    // Expected values
    PrintStream indStream = mock(PrintStream.class);
    printer.openStreams.put(ResultType.IND, indStream);

    // Execute functionality
    printer.close();

    // Check result
    verify(indStream).close();
  }

  /**
   * Test method for {@link ResultPrinter#close()} <p/> If the uccStream is set it should be closed
   * upon close.
   */
  @Test
  public void testCloseSetUccStream() throws IOException {
    // Setup
    // Expected values
    PrintStream uccStream = mock(PrintStream.class);
    printer.openStreams.put(ResultType.UCC, uccStream);

    // Execute functionality
    printer.close();

    // Check result
    verify(uccStream).close();
  }


}
