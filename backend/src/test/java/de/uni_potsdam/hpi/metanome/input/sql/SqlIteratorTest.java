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

package de.uni_potsdam.hpi.metanome.input.sql;

import com.google.common.collect.ImmutableList;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test for {@link SqlIterator}
 *
 * @author Jakob Zwiener
 */
public class SqlIteratorTest {

    protected ResultSetFixture minimalResultSetFixture;
    protected ResultSetTwoLinesFixture twoLinesResultSetFixture;

    @Before
    public void setUp() throws Exception {
        // TODO initialise fixtures here

        minimalResultSetFixture = new ResultSetFixture();
        twoLinesResultSetFixture = new ResultSetTwoLinesFixture();
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * hasNext should give the information supplied by the method next on the {@link ResultSet}. Next should only be called once, though
     * on the {@link ResultSet} and the hasNext information should be stored.
     *
     * @throws SQLException
     * @throws InputIterationException
     */
    @Test
    public void testHasNext() throws SQLException, InputIterationException {
        // Setup
        ResultSet resultSet = minimalResultSetFixture.getTestData();
        SqlIterator sqlIterator = new SqlIterator(resultSet);
        // Expected values
        boolean expectedFirstNext = true;
        boolean expectedSecondNext = false;
        when(resultSet.next())
                .thenReturn(expectedFirstNext)
                        // The second call should not be made.
                .thenReturn(false);

        // Execute functionality
        // Check result
        assertEquals(expectedFirstNext, sqlIterator.hasNext());
        // This should give the cached next result.
        assertEquals(expectedFirstNext, sqlIterator.hasNext());
        // After having called next on the SqlIterator the second next call should be made to the ResultSet
        sqlIterator.next();
        assertEquals(expectedSecondNext, sqlIterator.hasNext());
    }

    /**
     * Next should return the values in the record set as an ImmutableList of Strings per row.
     *
     * @throws SQLException
     * @throws InputIterationException
     */
    @Test
    public void testNext() throws SQLException, InputIterationException {
        // Setup
        ResultSet resultSet = twoLinesResultSetFixture.getTestData();
        SqlIterator sqlIterator = new SqlIterator(resultSet);

        // Check result
        List<Boolean> expectedNextValues = twoLinesResultSetFixture.getExpectedNextValues();
        List<ImmutableList<String>> expectedRecords = twoLinesResultSetFixture.getExpectedRecords();
        for (int i = 0; i < twoLinesResultSetFixture.numberOfRows(); i++) {
            assertEquals(expectedNextValues.get(i), sqlIterator.hasNext());
            assertEquals(expectedRecords.get(i), sqlIterator.next());
        }
        assertEquals(expectedNextValues.get(twoLinesResultSetFixture.numberOfRows()), sqlIterator.hasNext());
        // Next should have been called.
        verify(resultSet, times(3)).next();

    }

    /**
     * Next should return the correct rows and call next on the result set although hasNext has not been called.
     *
     * @throws SQLException
     * @throws InputIterationException
     */
    @Test
    public void testNextWithoutHasNext() throws SQLException, InputIterationException {
        // Setup
        ResultSet resultSet = twoLinesResultSetFixture.getTestData();
        SqlIterator sqlIterator = new SqlIterator(resultSet);

        // Check result
        List<ImmutableList<String>> expectedRecords = twoLinesResultSetFixture.getExpectedRecords();
        for (int i = 0; i < twoLinesResultSetFixture.numberOfRows(); i++) {
            assertEquals(expectedRecords.get(i), sqlIterator.next());
        }
        // Next should have been called (although hasNext has not been called on sqlIterator).
        verify(resultSet, times(2)).next();
    }

    /**
     * Test method for {@link SqlIterator#numberOfColumns()}
     * <p/>
     * A {@link SqlIterator} should return the correct number of columns of the result.
     *
     * @throws SQLException
     */
    @Test
    public void testNumberOfColumns() throws SQLException {
        // Setup
        SqlIterator sqlIterator = new SqlIterator(twoLinesResultSetFixture.getTestData());

        // Check result
        assertEquals(twoLinesResultSetFixture.numberOfColumns(), sqlIterator.numberOfColumns());
    }

    /**
     * Test method for {@link SqlIterator#relationName()}
     * <p/>
     * A {@link SqlIterator} should return the table name of the first column from the meta data.
     *
     * @throws SQLException
     */
    @Test
    public void testRelationName() throws SQLException {
        // Setup
        SqlIterator sqlIterator = new SqlIterator(twoLinesResultSetFixture.getTestData());

        // Execute functionality
        // Check result
        assertEquals(twoLinesResultSetFixture.getExpectedRelationName(), sqlIterator.relationName());
    }

    /**
     * Test method for {@link SqlIterator#columnNames()}
     * <p/>
     * A {@link SqlIterator} should return the column names from the meta data.
     *
     * @throws SQLException
     */
    @Test
    public void testColumnNames() throws SQLException {
        // Setup
        SqlIterator sqlIterator = new SqlIterator(twoLinesResultSetFixture.getTestData());

        // Execute functionality
        // Check result
        assertEquals(twoLinesResultSetFixture.getExpectedColumnNames(), sqlIterator.columnNames());
    }

    /**
     * Test method for {@link SqlIterator#close()}
     * <p/>
     * The sql iterator should be closeable. After closing the iterator, the underlying result set should be closed.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testClose() throws Exception {
        // Setup
        ResultSet resultSetMock = twoLinesResultSetFixture.getTestData();
        SqlIterator sqlIterator = new SqlIterator(resultSetMock);

        // Execute functionality
        sqlIterator.close();

        // Check result
        verify(resultSetMock).close();
    }

}
