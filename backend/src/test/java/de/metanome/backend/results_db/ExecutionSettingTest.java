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
package de.metanome.backend.results_db;

import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.Test;

public class ExecutionSettingTest {

  /**
   * Test method for {@link de.metanome.backend.results_db.ExecutionSetting#equals(Object)} and {@link
   * ExecutionSetting#hashCode()}}.
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    int id = 42;
    ExecutionSetting executionSetting = new ExecutionSetting(null, null, null).setId(id);
    ExecutionSetting equalExecutionSetting = new ExecutionSetting(null, null, null).setId(id);
    ExecutionSetting notEqualExecutionSetting = new ExecutionSetting(null, null, null).setId(23);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<ExecutionSetting>()
      .performBasicEqualsAndHashCodeChecks(executionSetting, equalExecutionSetting, notEqualExecutionSetting);
  }

}
