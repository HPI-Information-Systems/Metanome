/**
 * Copyright 2014, 2016 by Metanome Project
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
package de.metanome.backend.results_db;

/**
 * @author Jakob Zwiener
 */
public class EntityStorageException extends Exception {

  private static final long serialVersionUID = -174040542991104845L;

  protected EntityStorageException() {
    super();
  }

  public EntityStorageException(String message) {
    super(message);
  }

  public EntityStorageException(String message, Throwable cause) {
    super(message, cause);
  }

}
