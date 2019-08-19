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

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.Operator;
import de.metanome.algorithm_integration.Predicate;
import de.metanome.algorithm_integration.PredicateConstant;
import de.metanome.algorithm_integration.PredicateVariable;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.*;
import de.metanome.algorithm_integration.results.OrderDependency.ComparisonOperator;
import de.metanome.algorithm_integration.results.OrderDependency.OrderType;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueInteger;
import de.metanome.backend.results_db.ResultType;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

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
  protected ColumnIdentifier ci1;
  protected ColumnIdentifier ci2;

  @Before
  public void setUp() throws Exception {
    testAlgoExecutionIdentifier = "testAlgoExecution";

    ci1 = new ColumnIdentifier("table1", "column2");
    ci2 = new ColumnIdentifier("table1", "column23");

    List<ColumnIdentifier> names = new ArrayList<>();
    names.add(ci1);
    names.add(ci2);

    printer = new ResultPrinter(testAlgoExecutionIdentifier, names, true);
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
  public void testWriteBasicStatistic() throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values
    BasicStatistic
      expectedStat =
      new BasicStatistic(ci1);
    expectedStat.addStatistic("Min", new BasicStatisticValueInteger(5));

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.BASIC_STAT));

    // Execute functionality
    printer.receiveResult(expectedStat);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.BASIC_STAT.getEnding());
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
  public void testWriteFunctionalDependency() throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values
    FunctionalDependency expectedFd = new FunctionalDependency(
      new ColumnCombination(ci1), ci2);

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.FD));

    // Execute functionality
    printer.receiveResult(expectedFd);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.FD.getEnding());
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    assertTrue(fileContent.contains(expectedFd.toString(printer.tableMapping, printer.columnMapping)));

    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedFd));

    // Cleanup
    actualFile.delete();
  }
  
  /**
   * Test method for {@link ResultPrinter#receiveResult(ConditionalInclusionDependency)} <p/> Received {@link
   * ConditionalInclusionDependency}s should be written to the appropriate file.
   */
  @Test
  public void testWriteConditionalInclusionDependency() throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values
    ConditionalInclusionDependency expectedCid = new ConditionalInclusionDependency(
      new ColumnPermutation(ci1),
      new ColumnPermutation(ci2),
      "condition"
    );
    
    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.CID));

    // Execute functionality
    printer.receiveResult(expectedCid);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.CID.getEnding());
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    assertTrue(fileContent.contains(expectedCid.toString(printer.tableMapping, printer.columnMapping)));

    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedCid));

    // Cleanup
    actualFile.delete();
  }

  /**
   * Test method for {@link ResultPrinter#receiveResult(FunctionalDependency)} <p/> Received {@link
   * FunctionalDependency}s should be written to the appropriate file.
   */
  @Test
  public void testWriteFunctionalDependencyWithoutMapping() throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values
    FunctionalDependency expectedFd = new FunctionalDependency(
      new ColumnCombination(ci1), ci2);
    printer.acceptedColumns = null;

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.FD));

    // Execute functionality
    printer.receiveResult(expectedFd);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.FD.getEnding());
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
  public void testWriteInclusionDependency() throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values
    InclusionDependency expectedInd = new InclusionDependency(
      new ColumnPermutation(ci1),
      new ColumnPermutation(ci2)
    );

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.IND));

    // Execute functionality
    printer.receiveResult(expectedInd);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.IND.getEnding());
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    assertTrue(fileContent.contains(expectedInd.toString(printer.tableMapping, printer.columnMapping)));

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
    throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values
    UniqueColumnCombination
      expectedUcc =
      new UniqueColumnCombination(ci1);

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.UCC));

    // Execute functionality
    printer.receiveResult(expectedUcc);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.UCC.getEnding());
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    assertTrue(fileContent.contains(expectedUcc.toString(printer.tableMapping, printer.columnMapping)));

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
  public void testWriteOrderDependency() throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values
    OrderDependency expectedOd =
      new OrderDependency(new ColumnPermutation(ci1),
        new ColumnPermutation(ci2),
        OrderType.LEXICOGRAPHICAL,
        ComparisonOperator.SMALLER_EQUAL);

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.OD));

    // Execute functionality
    printer.receiveResult(expectedOd);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.OD.getEnding());
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    assertTrue(fileContent.contains(expectedOd.toString(printer.tableMapping, printer.columnMapping)));

    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedOd));

    // Cleanup
    actualFile.delete();
  }

  /**
   * Test method for {@link ResultPrinter#receiveResult(OrderDependency)} <p/> Received {@link
   * OrderDependency}s should be written to the appropriate file.
   */
  @Test
  public void testWriteOrderDependencyWithoutMapping() throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values
    OrderDependency expectedOd =
      new OrderDependency(new ColumnPermutation(ci1),
        new ColumnPermutation(ci2),
        OrderType.LEXICOGRAPHICAL,
        ComparisonOperator.SMALLER_EQUAL);
    printer.acceptedColumns = null;

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.OD));

    // Execute functionality
    printer.receiveResult(expectedOd);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.OD.getEnding());
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
   * Test method for {@link ResultPrinter#receiveResult(OrderDependency)} <p/> Received {@link
   * DenialConstraint}s should be written to the appropriate file.
   */
  @Test
  public void testWriteDenialConstraint() throws CouldNotReceiveResultException, IOException, ColumnNameMismatchException {
    // Expected values

    ColumnIdentifier c1 = new ColumnIdentifier("table", "c1");
    Predicate p1 = new PredicateVariable(c1, 1, Operator.EQUAL, c1, 2);
    Predicate p2 = new PredicateConstant<Double>(c1, 1, Operator.EQUAL, 2.0d);
    DenialConstraint expectedDc =
      new DenialConstraint(p1, p2);
    printer.acceptedColumns = null;

    // Check precondition
    assertTrue(!printer.openStreams.containsKey(ResultType.DC));

    // Execute functionality
    printer.receiveResult(expectedDc);

    // Check result
    File actualFile = new File(printer.getOutputFilePathPrefix() + ResultType.DC.getEnding());
    assertTrue(actualFile.exists());

    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

    JsonConverter<DenialConstraint> jsonConverter = new JsonConverter<>();
    assertTrue(fileContent.contains(jsonConverter.toJsonString(expectedDc)));
    List<Result> results = printer.getResults();
    assertTrue(results.contains(expectedDc));

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
    printer.openStreams.put(ResultType.BASIC_STAT, statStream);

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
  
  /**
   * Test method for {@link ResultPrinter#close()} <p/> If the dcStream is set it should be closed
   * upon close.
   */
  @Test
  public void testCloseSetDcStream() throws IOException {
    // Setup
    // Expected values
    PrintStream dcStream = mock(PrintStream.class);
    printer.openStreams.put(ResultType.DC, dcStream);

    // Execute functionality
    printer.close();

    // Check result
    verify(dcStream).close();
  }

  @Test
  public void testMapping() {
    assertTrue(printer.tableMapping.containsKey("table1"));
    assertTrue(printer.tableMapping.get("table1").equals("1"));

    assertTrue(printer.columnMapping.containsKey("1.column2"));
    assertTrue(printer.columnMapping.get("1.column2").equals("1"));
    assertTrue(printer.columnMapping.containsKey("1.column23"));
    assertTrue(printer.columnMapping.get("1.column23").equals("2"));
  }


}
