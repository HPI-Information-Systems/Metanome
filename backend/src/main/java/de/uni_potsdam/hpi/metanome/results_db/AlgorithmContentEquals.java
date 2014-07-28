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

package de.uni_potsdam.hpi.metanome.results_db;

/**
 * Checks the equality of two algorithms based on all fields, not only the database key.
 *
 * @author Jakob Zwiener
 */
public class AlgorithmContentEquals {

  /**
   * Checks the equality of two algorithms based on all fields, not only the database key.
   *
   * @param algo1 an algorithm
   * @param algo2 an algorithm
   * @return whether the algorithms are equal
   */
  public static boolean contentEquals(Algorithm algo1, Algorithm algo2) {
    if (algo1.getFileName() != null ? !algo1.getFileName().equals(algo2.getFileName())
                                    : algo2.getFileName() != null) {
      return false;
    }
    if (algo1.getName() != null ? !algo1.getName().equals(algo2.getName())
                                : algo2.getName() != null) {
      return false;
    }
    if (algo1.getAuthor() != null ? !algo1.getAuthor().equals(algo2.getAuthor())
                                  : algo2.getAuthor() != null) {
      return false;
    }
    if (algo1.getDescription() != null ? !algo1.getDescription().equals(algo2.getDescription())
                                       : algo2.getDescription() != null) {
      return false;

    }
    if (!(algo1.isInd() == algo2.isInd())) {
      return false;
    }
    if (!(algo1.isFd() == algo2.isFd())) {
      return false;
    }
    if (!(algo1.isUcc() == algo2.isUcc())) {
      return false;
    }

    if (!(algo1.isCucc() == algo2.isCucc())) {
      return false;
    }

    return algo1.isBasicStat() == algo2.isBasicStat();

  }

}
