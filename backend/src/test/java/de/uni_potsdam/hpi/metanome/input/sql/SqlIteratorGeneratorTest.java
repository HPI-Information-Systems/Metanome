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

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.input.sql.SqlIterator}
 *
 * @author Jakob Zwiener
 */
public class SqlIteratorGeneratorTest {


    /**
     * Test method for {@link SqlIteratorGenerator#close()}
     * <p/>
     * The sql iterator generator should be closeable. After closing the sql iterator generator the underlying db connection should be closed.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void testClose() throws SQLException {
        // Setup
        SqlIteratorGenerator sqlIteratorGenerator = new SqlIteratorGenerator();
        Connection connection = mock(Connection.class);
        sqlIteratorGenerator.dbConnection = connection;

        // Execute functionality
        sqlIteratorGenerator.close();

        // Check result
        verify(connection).close();
    }
}