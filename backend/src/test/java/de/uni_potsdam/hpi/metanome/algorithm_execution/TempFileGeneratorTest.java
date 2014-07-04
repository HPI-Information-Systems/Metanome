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

package de.uni_potsdam.hpi.metanome.algorithm_execution;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileCreationException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link TempFileGenerator}
 *
 * @author Jakob Zwiener
 */
public class TempFileGeneratorTest {

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link TempFileGenerator#getTemporaryFile()} <p/> The generator should return
   * readable, writable, empty files.
   */
  @Test
  public void testGetTemporaryFile() throws IOException, FileCreationException {
    // Setup
    TempFileGenerator tempFileGenerator = new TempFileGenerator();

    // Execute functionality
    File actualFile = tempFileGenerator.getTemporaryFile();

    // Check result
    assertTrue(actualFile.exists());
    assertTrue(actualFile.isFile());
    assertTrue(actualFile.canRead());
    assertTrue(actualFile.canWrite());
    assertEquals(0, actualFile.length());

    // Cleanup
    tempFileGenerator.close();
  }

  /**
   * Test method for {@link TempFileGenerator#getTemporaryFile()} <p/> Files should be writable and
   * the result be persistent.
   */
  @Test
  public void testGetTemporaryFileWrite() throws IOException, FileCreationException {
    // Setup
    TempFileGenerator tempFileGenerator = new TempFileGenerator();
    File actualFile = tempFileGenerator.getTemporaryFile();
    PrintWriter writer = new PrintWriter(actualFile);
    // Expected values
    String expectedFileContent = "some content";

    // Execute functionality
    // Check result
    assertEquals(0, actualFile.length());
    writer.print(expectedFileContent);
    writer.close();
    assertEquals(expectedFileContent, FileUtils.readFileToString(actualFile));

    // Cleanup
    tempFileGenerator.close();
  }

  /**
   * Test method for {@link TempFileGenerator#getTemporaryFile()} <p/> New files should be generated
   * on every call.
   */
  @Test
  public void testGetTemporaryFileDifferent() throws FileCreationException, IOException {
    // Setup
    TempFileGenerator tempFileGenerator = new TempFileGenerator();
    File actualFile1 = tempFileGenerator.getTemporaryFile();
    File actualFile2 = tempFileGenerator.getTemporaryFile();

    // Check result
    assertNotEquals(actualFile1, actualFile2);

    // Cleanup
    tempFileGenerator.close();
  }

  /**
   * Test method for {@link TempFileGenerator#close()} <p/> All files
   */
  @Test
  public void testClose() throws IOException, FileCreationException {
    // Setup
    TempFileGenerator tempFileGenerator = new TempFileGenerator();
    File actualFile1 = tempFileGenerator.getTemporaryFile();
    File actualFile2 = tempFileGenerator.getTemporaryFile();

    // Execute functionality
    tempFileGenerator.close();

    // Check result
    assertFalse(actualFile1.exists());
    assertFalse(actualFile2.exists());
  }


  /**
   * Test method for {@link TempFileGenerator#close()} <p/> All files should be delete, even if one
   * file throws an Exception and can not be deleted.
   */
  @Test
  public void testCloseWithException() throws UnsupportedEncodingException {
    // Setup
    File file1 = mock(File.class);
    File file2 = mock(File.class);
    File file3 = mock(File.class);

    TempFileGenerator tempFileGenerator = new TempFileGenerator();
    tempFileGenerator.createdFiles.add(file1);
    tempFileGenerator.createdFiles.add(file2);
    tempFileGenerator.createdFiles.add(file3);

    doThrow(new RuntimeException()).when(file2).delete();

    // Execute functionality
    tempFileGenerator.close();

    // Check result
    assertFalse(file1.exists());
    assertFalse(file3.exists());
  }

}
