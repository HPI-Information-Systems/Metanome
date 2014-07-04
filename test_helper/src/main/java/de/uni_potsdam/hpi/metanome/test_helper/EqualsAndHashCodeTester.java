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

package de.uni_potsdam.hpi.metanome.test_helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Test helper for equals and hashCode.
 *
 * @author Jakob Zwiener
 */
public class EqualsAndHashCodeTester<T> {

  /**
   * Performs basic tests for the methods equals and hashCode, based on a given instance, an equal
   * instance and a non-equal instance.
   */
  public void performBasicEqualsAndHashCodeChecks(T base, T equal, T... notEquals) {
    assertEquals(base, base);
    assertEquals(base.hashCode(), base.hashCode());

    assertNotSame(base, equal);
    assertEquals(base, equal);
    assertEquals(base.hashCode(), equal.hashCode());

    assertNotEquals(base, null);

    for (T notEqual : notEquals) {
      assertNotEquals(base, notEqual);
      assertNotEquals(base.hashCode(), notEqual.hashCode());
    }
  }
}
