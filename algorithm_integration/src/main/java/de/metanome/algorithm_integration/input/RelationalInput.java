/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithm_integration.input;

import java.util.List;

/**
 * Relational inputs can be iterated, but iterators may throw {@link InputIterationException}s when
 * iterating.
 *
 * @author Jakob Zwiener
 */
public interface RelationalInput extends AutoCloseable {

  /**
   * If the {@link RelationalInput} has another row this method returns true.
   *
   * @return the {@link RelationalInput} has another row
   * @throws de.metanome.algorithm_integration.input.InputIterationException if it could not be determined if the
   * Relational Input has another row or not
   */
  boolean hasNext() throws InputIterationException;

  /**
   * Retrieves the next row.
   *
   * @return the next row
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the next line could not be read
   */
  List<String> next() throws InputIterationException;

  /**
   * Returns the number of columns.
   *
   * @return the number of columns.
   */
  int numberOfColumns();

  /**
   * Returns the relation's name
   *
   * @return the relation's name
   */
  String relationName();

  /**
   * Returns the column names.
   *
   * @return the column names.
   */
  List<String> columnNames();

}
