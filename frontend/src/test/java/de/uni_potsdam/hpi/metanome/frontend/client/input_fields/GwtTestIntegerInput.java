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

package de.uni_potsdam.hpi.metanome.frontend.client.input_fields;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;

public class GwtTestIntegerInput extends GWTTestCase {

  /**
   * Test method for {@link IntegerInput#getValue()} and for
   * {@link de.uni_potsdam.hpi.metanome.frontend.client.input_fields.IntegerInput#setValue(Integer)}
   * @throws InputValidationException
   */
  public void testGetSetValues() throws InputValidationException {
    IntegerInput input = new IntegerInput(false);

    input.setValue(4);
    assertEquals(4, input.getValue());
  }

  @Override
  public String getModuleName() {
      return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
    }

}
