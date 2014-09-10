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

package de.metanome.frontend.server;

import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertThat;


public class InputServiceImplTest {

  /**
   * Test method for {@link de.metanome.frontend.server.InputServiceImpl#listInputs()}
   */
  @Test
  public void testListInputs() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    InputServiceImpl inputService = new InputServiceImpl();

    // Expected values
    FileInput expectedFileInput1 = new FileInput();
    expectedFileInput1.setFileName("file1");

    FileInput expectedFileInput2 = new FileInput();
    expectedFileInput2.setFileName("file2");

    Input[] expectedFileInputs = {expectedFileInput1, expectedFileInput2};

    for (Input input : expectedFileInputs) {
      input.store();
    }

    // Execute functionality
    List<Input> actualFileInputs = inputService.listInputs();

    // Check result
    assertThat(actualFileInputs,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedFileInputs));

    // Cleanup
    HibernateUtil.clear();
  }

}
