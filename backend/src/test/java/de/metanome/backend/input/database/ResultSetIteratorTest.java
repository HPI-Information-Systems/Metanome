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
package de.metanome.backend.input.database;

import com.google.common.collect.ImmutableList;
import de.metanome.algorithm_integration.input.InputIterationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test for {@link ResultSetIterator}
 *
 * @author Jakob Zwiener
 */
public class ResultSetIteratorTest {

  protected ResultSetFixture minimalResultSetFixture;
  protected ResultSetWithoutRelationNameFixture emptyResultSetWithoutRelationNameFixture;
  protected ResultSetTwoLinesFixture twoLinesResultSetFixture;

  @Before
  public void setUp() throws Exception {
    minimalResultSetFixture = new ResultSetFixture();
    emptyResultSetWithoutRelationNameFixture = new ResultSetWithoutRelationNameFixture();
    twoLinesResultSetFixture = new ResultSetTwoLinesFixture();
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * hasNext should give the information supplied by the method next on the {@link ResultSet}. Next
   * should only be called once, though on the {@link ResultSet} and the hasNext information should
   * be stored.
   */
  @Test
  public void testHasNext() throws SQLException, InputIterationException {
    // Setup
    ResultSet resultSet = minimalResultSetFixture.getTestData();
    ResultSetIterator resultSetIterator = new ResultSetIterator(resultSet);
    // Expected values
    boolean expectedFirstNext = true;
    boolean expectedSecondNext = false;
    when(resultSet.next())
      .thenReturn(expectedFirstNext)
        // The second call should not be made.
      .thenReturn(false);

    // Execute functionality
    // Check result
    assertEquals(expectedFirstNext, resultSetIterator.hasNext());
    // This should give the cached next result.
    assertEquals(expectedFirstNext, resultSetIterator.hasNext());
    // After having called next on the SqlIterator the second next call should be made to the ResultSet
    resultSetIterator.next();
    assertEquals(expectedSecondNext, resultSetIterator.hasNext());
  }

  /**
   * Next should return the values in the record set as an ImmutableList of Strings per row.
   */
  @Test
  public void testNext() throws SQLException, InputIterationException {
    // Setup
    ResultSet resultSet = twoLinesResultSetFixture.getTestData();
    ResultSetIterator resultSetIterator = new ResultSetIterator(resultSet);

    // Check result
    List<Boolean> expectedNextValues = twoLinesResultSetFixture.getExpectedNextValues();
    List<ImmutableList<String>> expectedRecords = twoLinesResultSetFixture.getExpectedRecords();
    for (int i = 0; i < twoLinesResultSetFixture.numberOfRows(); i++) {
      assertEquals(expectedNextValues.get(i), resultSetIterator.hasNext());
      assertEquals(expectedRecords.get(i), resultSetIterator.next());
    }
    assertEquals(expectedNextValues.get(twoLinesResultSetFixture.numberOfRows()),
      resultSetIterator.hasNext());
    // Next should have been called.
    verify(resultSet, times(3)).next();

  }

  /**
   * Next should return the correct rows and call next on the result set although hasNext has not
   * been called.
   */
  @Test
  public void testNextWithoutHasNext() throws SQLException, InputIterationException {
    // Setup
    ResultSet resultSet = twoLinesResultSetFixture.getTestData();
    ResultSetIterator resultSetIterator = new ResultSetIterator(resultSet);

    // Check result
    List<ImmutableList<String>> expectedRecords = twoLinesResultSetFixture.getExpectedRecords();
    for (int i = 0; i < twoLinesResultSetFixture.numberOfRows(); i++) {
      assertEquals(expectedRecords.get(i), resultSetIterator.next());
    }
    // Next should have been called (although hasNext has not been called on sqlIterator).
    verify(resultSet, times(2)).next();
  }

  /**
   * Test method for {@link ResultSetIterator#numberOfColumns()} <p/> A {@link ResultSetIterator}
   * should return the correct number of columns of the result.
   */
  @Test
  public void testNumberOfColumns() throws SQLException {
    // Setup
    ResultSetIterator
      resultSetIterator =
      new ResultSetIterator(twoLinesResultSetFixture.getTestData());

    // Check result
    assertEquals(twoLinesResultSetFixture.numberOfColumns(), resultSetIterator.numberOfColumns());
  }

  /**
   * Test method for {@link ResultSetIterator#relationName()} <p/> A {@link ResultSetIterator}
   * should return the table name of the first column from the meta data.
   */
  @Test
  public void testRelationName() throws SQLException {
    // Setup
    ResultSetIterator
      resultSetIterator =
      new ResultSetIterator(twoLinesResultSetFixture.getTestData());

    // Execute functionality
    // Check result
    assertEquals(twoLinesResultSetFixture.getExpectedRelationName(),
      resultSetIterator.relationName());
  }

  /**
   * Test method for {@link ResultSetIterator#relationName()} <p/> A {@link ResultSetIterator}
   * should return the fallback value if it cannot extract the relation name from the first column's relation.
   */
  @Test
  public void testMissingRelationNameWithFallback() throws SQLException {
    // Setup
    ResultSetIterator resultSetIterator =
      new ResultSetIterator(
              emptyResultSetWithoutRelationNameFixture.getTestData(),
              emptyResultSetWithoutRelationNameFixture.getExpectedRelationName()
      );

    // Execute functionality
    // Check result
    assertEquals(emptyResultSetWithoutRelationNameFixture.getExpectedRelationName(), resultSetIterator.relationName());
  }

  /**
   * Test method for {@link ResultSetIterator#relationName()} <p/> A {@link ResultSetIterator}
   * should return {@value ResultSetIterator#UNKNOWN_RELATION_NAME} if it cannot extract the relation name from the
   * first column's relation and no fallback value is given, so as to avoid {@link NullPointerException}s.
   */
  @Test
  public void testMissingRelationNameWithoutFallback() throws SQLException {
    // Setup
    ResultSetIterator resultSetIterator = new ResultSetIterator(emptyResultSetWithoutRelationNameFixture.getTestData());

    // Execute functionality
    // Check result
    assertEquals(ResultSetIterator.UNKNOWN_RELATION_NAME, resultSetIterator.relationName());
  }

  /**
   * Test method for {@link ResultSetIterator#columnNames()} <p/> A {@link ResultSetIterator} should
   * return the column names from the meta data.
   */
  @Test
  public void testColumnNames() throws SQLException {
    // Setup
    ResultSetIterator
      resultSetIterator =
      new ResultSetIterator(twoLinesResultSetFixture.getTestData());

    // Execute functionality
    // Check result
    assertEquals(twoLinesResultSetFixture.getExpectedColumnNames(),
      resultSetIterator.columnNames());
  }

  /**
   * Test method for {@link ResultSetIterator#close()} <p/> The database iterator should be
   * closeable. After closing the iterator, the underlying result set and statement should be
   * closed.
   */
  @Test
  public void testClose() throws Exception {
    // Setup
    ResultSet resultSetMock = twoLinesResultSetFixture.getTestData();
    Statement statementMock = mock(Statement.class);
    when(resultSetMock.getStatement()).thenReturn(statementMock);
    ResultSetIterator resultSetIterator = new ResultSetIterator(resultSetMock);

    // Execute functionality
    resultSetIterator.close();

    // Check result
    verify(resultSetMock).close();
    verify(statementMock).close();
  }

  /**
   * Test method for {@link ResultSetIterator#close()} <p/> The result set and statement underlying
   * the iterator should not be closed, if they have previously been closed.
   */
  @Test
  public void testCloseAlreadyClosed() throws Exception {
    // Setup
    ResultSet resultSetMock = twoLinesResultSetFixture.getTestData();
    when(resultSetMock.isClosed()).thenReturn(true);
    Statement statementMock = mock(Statement.class);
    when(statementMock.isClosed()).thenReturn(true);
    ResultSetIterator resultSetIterator = new ResultSetIterator(resultSetMock);

    when(resultSetMock.getStatement()).thenReturn(statementMock);

    // Execute functionality
    resultSetIterator.close();

    // Check result
    verify(resultSetMock, never()).close();
    verify(statementMock, never()).close();
  }

}
