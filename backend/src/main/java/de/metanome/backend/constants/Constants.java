/**
 * Copyright 2014-2019 by Metanome Project
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
package de.metanome.backend.constants;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * A collection of variables frequently used throughout the backend.
 * The class offers an overview by containing them all.
 * For consistency, try to include the constants from this class.
 * @author Joana Bergsiek
 */
public class Constants {

    public static final String FILE_ENCODING = "utf-8";
    public static final String BOOTRSTAP_CLASS_TAG_NAME = "Algorithm-Bootstrap-Class";
    public static final String JAR_FILE_ENDING = ".jar";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String[] ACCEPTED_FILE_ENDINGS_ARRAY = new String[]{".csv", ".tsv"};
    public static final Stream<String> ACCEPTED_FILE_ENDINGS_STREAM = Arrays.asList(ACCEPTED_FILE_ENDINGS_ARRAY).stream();
    public static final String ALGORITHMS_RESOURCE_NAME = "algorithms";
    public static final String INPUTDATA_RESOURCE_NAME = "inputData";
    public static final String STORE_RESOURCE_PATH = "/store";
    public static final String APPLICATION_JSON_RESOURCE_PATH = "application/json";
    public static final String SUPPRESS_WARNINGS_UNCHECKED = "unchecked";
    public static final String TEMP_FILE_PATH = "temp_files";
    public static final int FILE_NAME_LENGTH = 30;
}
