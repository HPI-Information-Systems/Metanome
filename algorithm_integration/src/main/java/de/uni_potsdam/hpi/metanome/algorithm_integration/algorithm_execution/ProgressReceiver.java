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

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;

/**
 * Receives an {@link Algorithm}'s progress.
 *
 * @author Jakob Zwiener
 */
public interface ProgressReceiver {

  /**
   * Updates the {@link Algorithm}'s progress. Progress should be between 0 and 1 including bounds.
   * If the progress is invalid false is returned.
   *
   * @param progress the current progress normalized between 0 and 1 including bounds.
   * @return true iff the new progress is valid.
   */
  boolean updateProgress(float progress);
}
