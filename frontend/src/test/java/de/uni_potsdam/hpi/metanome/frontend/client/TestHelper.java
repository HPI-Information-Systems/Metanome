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

package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.results_db.Algorithm;


/**
 * Helper that performs database interaction synchronously. To be used in tests.
 *
 * @author Jakob Zwiener
 * @see de.uni_potsdam.hpi.metanome.frontend.client.TestDatabaseHelperService
 */
public class TestHelper {

  protected static TestDatabaseHelperServiceAsync
      testDatabaseHelperService =
      GWT.create(TestDatabaseHelperService.class);

  /**
   * Resets the database synchronously.
   */
  public static void resetDatabaseSync() {
    final boolean[] blocked = {true};
    testDatabaseHelperService.resetDatabase(new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable throwable) {
      }

      @Override
      public void onSuccess(Void aVoid) {
        blocked[0] = false;
      }
    });

    Timer rpcCheck = new Timer() {
      @Override
      public void run() {
        if (blocked[0]) {
          this.schedule(100);
        }
      }
    };
    rpcCheck.schedule(100);
  }

  /**
   * Stores an algorithm synchronously.
   *
   * @param algorithm the {@link de.uni_potsdam.hpi.metanome.results_db.Algorithm} to store
   */
  public static void storeAlgorithmSync(Algorithm algorithm) {
    final boolean[] blocked = {true};
    testDatabaseHelperService.storeAlgorithmInDatabase(algorithm, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable throwable) {
      }

      @Override
      public void onSuccess(Void aVoid) {
        blocked[0] = false;
      }
    });

    Timer rpcCheck = new Timer() {
      @Override
      public void run() {
        if (blocked[0]) {
          this.schedule(100);
        }
      }
    };
    rpcCheck.schedule(100);
  }
}
