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
package de.metanome.backend.result_postprocessing.result_store;

import java.util.HashMap;
import java.util.Map;

/**
 * "Static" results holder provides the option to register a result store and to clear all
 * registered storage.
 */
public final class ResultsStoreHolder {

  // Results store map
  protected static Map<String, ResultsStore<?>> resultsStoreMap = new HashMap<>();

  /**
   * Registers the given result store under given name
   *
   * @param storeName    Store name
   * @param resultsStore Results store to be registered
   */
  public static void register(String storeName, ResultsStore<?> resultsStore) {
    resultsStoreMap.put(storeName, resultsStore);
  }

  /**
   * Returns the registered store for the given name
   *
   * @param storeName Name of requested store
   * @return Returns the registered store for the given name
   */
  public static ResultsStore<?> getStore(String storeName) {
    return resultsStoreMap.get(storeName);
  }

  /**
   * Clears all registered stores
   */
  public static void clearStores() {
    for (ResultsStore<?> resultsStore : resultsStoreMap.values()) {
      resultsStore.clear();
    }
  }

}
