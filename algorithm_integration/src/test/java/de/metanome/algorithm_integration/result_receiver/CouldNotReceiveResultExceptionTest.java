/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithm_integration.result_receiver;

import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;


public class CouldNotReceiveResultExceptionTest {


  /**
   * Test method for {@link de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException#CouldNotReceiveResultException(String)}
   * <p/>
   * The exception should store the message.
   */
  @Test
  public void testCouldNotReceiveResultExceptionString() {
    // Setup
    // Expected values
    String expectedMessage = "some message";

    // Execute functionality
    String actualMessage;
    try {
      throw new CouldNotReceiveResultException(expectedMessage);
    } catch (CouldNotReceiveResultException e) {
      actualMessage = e.getMessage();
    }

    // Check result
    assertEquals(expectedMessage, actualMessage);
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException#CouldNotReceiveResultException(String,
   * Throwable)}
   * <p/>
   * The exception should store the message and the cause.
   */
  @Test
  public void testCouldNotReceiveResultExceptionStringThrowable() {
    // Setup
    // Expected values
    String expectedMessage = "some message";
    Throwable expectedCause = new FileNotFoundException("some file was not found");

    // Execute functionality
    String actualMessage;
    Throwable actualCause;
    try {
      throw new CouldNotReceiveResultException(expectedMessage, expectedCause);
    } catch (CouldNotReceiveResultException e) {
      actualMessage = e.getMessage();
      actualCause = e.getCause();
    }

    // Check result
    assertEquals(expectedMessage, actualMessage);
    assertEquals(expectedCause, actualCause);
  }

}
