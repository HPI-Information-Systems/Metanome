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

package de.metanome.backend.results_db;

import com.google.common.annotations.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * All Entities in the results db should extend ResultsDbEntity. The entities should be storeable
 * and deleteable.
 *
 * @author Jakob Zwiener
 */
public abstract class ResultsDbEntity implements IsSerializable {

  /**
   * Stores a ResultsDbEntity in the database.
   *
   * @return the stored entity
   */
  @GwtIncompatible("Storing objects in the database is not gwt compatible.")
  public abstract Object store() throws EntityStorageException;

  /**
   * Deletes the entity from the database.
   */
  @GwtIncompatible("HibernateUtil is not gwt compatible.")
  public void delete() {
    try {
      HibernateUtil.delete(this);
    } catch (EntityStorageException e) {
      // The exception would only be thrown if the Algorithm did not have the Entity annotation.
    }
  }
}
