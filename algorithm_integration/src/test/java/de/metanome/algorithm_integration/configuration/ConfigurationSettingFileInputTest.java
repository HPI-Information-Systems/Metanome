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

package de.metanome.algorithm_integration.configuration;

import de.metanome.test_helper.GwtSerializationTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link ConfigurationSettingFileInput}
 *
 * @author Jakob Zwiener
 */
public class ConfigurationSettingFileInputTest {

  /**
   * Test method for {@link ConfigurationSettingFileInput#ConfigurationSettingFileInput(String,
   * boolean, char, char, char, boolean, boolean, int, boolean, boolean, String)}
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    String expectedFileName = "some file name";
    boolean expectedIsAdvanced = true;
    char expectedSeparator = ',';
    char expectedQuote = '"';
    char expectedEscape = '\\';
    boolean expectedIsStrictQuotes = false;
    boolean expectedIsIgnoreLeadingWhitespace = true;
    int expectedLines = 2;
    boolean expectedHeader = true;
    boolean expectedDifferingLines = false;
    String expectedNullValue = "";

    // Execute functionality
    ConfigurationSettingFileInput
        actualSetting =
        new ConfigurationSettingFileInput(expectedFileName, expectedIsAdvanced, expectedSeparator,
                                        expectedQuote, expectedEscape, expectedIsStrictQuotes,
                                        expectedIsIgnoreLeadingWhitespace, expectedLines,
                                        expectedHeader, expectedDifferingLines, expectedNullValue);

    // Check result
    assertEquals(expectedFileName, actualSetting.getFileName());
    assertEquals(expectedIsAdvanced, actualSetting.isAdvanced());
    assertEquals(expectedSeparator, actualSetting.getSeparatorAsChar());
    assertEquals(expectedQuote, actualSetting.getQuoteCharAsChar());
    assertEquals(expectedEscape, actualSetting.getEscapeCharAsChar());
    assertEquals(expectedIsStrictQuotes, actualSetting.isStrictQuotes());
    assertEquals(expectedIsIgnoreLeadingWhitespace, actualSetting.isIgnoreLeadingWhiteSpace());
    assertEquals(expectedLines, actualSetting.getSkipLines());
    assertEquals(expectedNullValue, actualSetting.getNullValue());
  }

  /**
   * Tests that the instances of {@link ConfigurationSettingFileInput}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(
        new ConfigurationSettingFileInput("fileName", true, ',', '"', '\\', true, true, 2, true,
                                        true, ""));
  }
}
