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

package de.metanome.backend.resources;

import de.metanome.backend.algorithm_execution.ProgressCache;
import de.metanome.backend.result_receiver.ResultCache;
import de.metanome.backend.result_receiver.ResultCounter;
import de.metanome.backend.result_receiver.ResultPrinter;

import java.util.HashMap;

public class AlgorithmExecutionCache {

  static public HashMap<String, ResultCache> currentResultCache = new HashMap<>();
  static public HashMap<String, ResultPrinter> currentResultPrinter = new HashMap<>();
  static public HashMap<String, ResultCounter> currentResultCounter = new HashMap<>();
  static public HashMap<String, ProgressCache> currentProgressCaches = new HashMap<>();

  static void add(String identifier, ResultCache resultCache) {
    currentResultCache.put(identifier, resultCache);
  }

  static void add(String identifier, ProgressCache progressCache) {
    currentProgressCaches.put(identifier, progressCache);
  }

  static void add(String identifier, ResultPrinter resultPrinter) {
    currentResultPrinter.put(identifier, resultPrinter);
  }

  static void add(String identifier, ResultCounter resultCounter) {
    currentResultCounter.put(identifier, resultCounter);
  }

  static ResultCache getResultCache(String identifier) {
    return currentResultCache.get(identifier);
  }

  static ProgressCache getProgressCache(String identifier) {
    return currentProgressCaches.get(identifier);
  }

  static ResultCounter getResultCounter(String identifier) {
    return currentResultCounter.get(identifier);
  }

  static ResultPrinter getResultPrinter(String identifier) {
    return currentResultPrinter.get(identifier);
  }

}
