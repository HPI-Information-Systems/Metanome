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

package de.metanome.frontend.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.EntityStorageException;

/**
 * Helper that performs database interaction synchronously. To be used in tests.
 *
 * @author Jakob Zwiener
 * @see TestDatabaseHelperService
 */
public class TestHelper {

  protected static TestDatabaseHelperServiceAsync
      testDatabaseHelperService =
      GWT.create(TestDatabaseHelperService.class);

  static boolean[] reset_blocked = {true};
  static boolean[] database_connection_blocked = {true};
  static boolean[] file_input_blocked = {true};
  static boolean[] algorithm_blocked = {true};

  /**
   * Resets the database synchronously.
   */
  public static void resetDatabaseSync() {
    testDatabaseHelperService.resetDatabase(new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable caught) {
        reset_blocked[0] = false;
        System.out.println(caught.getMessage());
      }

      @Override
      public void onSuccess(Void aVoid) {
        reset_blocked[0] = false;
      }
    });

    Timer rpcCheck = new Timer() {
      @Override
      public void run() {
        if (reset_blocked[0]) {
          this.schedule(100);
        }
      }
    };
    rpcCheck.schedule(100);
  }

  /**
   * Stores an algorithm synchronously.
   *
   * @param algorithm the {@link de.metanome.backend.results_db.Algorithm} to store
   */
  public static Algorithm storeAlgorithmSync(Algorithm algorithm) throws EntityStorageException {
    final Algorithm[] returnAlgorithm = {algorithm};

    testDatabaseHelperService.storeAlgorithmInDatabase(algorithm, new AsyncCallback<Algorithm>() {
      @Override
      public void onFailure(Throwable caught) {
        algorithm_blocked[0] = false;
        System.out.println("Algorithm could not be saved!");
        System.out.println(caught.getMessage());
      }

      @Override
      public void onSuccess(Algorithm a) {
        algorithm_blocked[0] = false;
        returnAlgorithm[0] = a;
      }
    });

    Timer rpcCheck = new Timer() {
      @Override
      public void run() {
        if (algorithm_blocked[0]) {
          this.schedule(100);
        }
      }
    };
    rpcCheck.schedule(100);

    return returnAlgorithm[0];
  }
}
