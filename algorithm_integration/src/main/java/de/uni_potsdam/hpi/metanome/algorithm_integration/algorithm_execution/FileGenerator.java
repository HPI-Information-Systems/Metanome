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

package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution;

import java.io.Closeable;
import java.io.File;

/**
 * Generates temporary files.
 *
 * @author Jakob Zwiener
 */
public interface FileGenerator extends Closeable {

  /**
   * Returns a temporary file that will be deleted on close.
   *
   * @return temporary file
   * @throws FileCreationException if the file cannot be created
   */
  File getTemporaryFile() throws FileCreationException;
}
