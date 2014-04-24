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

package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 *
 */
public class InputDataFinder {

    public File[] getAvailableCsvs() throws UnsupportedEncodingException {
        String pathToFolder = Thread.currentThread().getContextClassLoader().getResource("inputData").getPath();

        return retrieveCsvFiles(pathToFolder);
    }

    /**
     * retrieves all CSV Files located directly in the given directory
     * TODO consider sharing code with algorithm finding methods
     *
     * @param pathToFolder path to the folder to be searched in
     * @return names of all CSV files located directly in the given directory (no subfolders)
     * @throws UnsupportedEncodingException
     */
    protected File[] retrieveCsvFiles(String pathToFolder) throws UnsupportedEncodingException {
        File folder = new File(URLDecoder.decode(pathToFolder, "utf-8"));
        File[] csvs = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".csv");
            }
        });

        return csvs;
    }

}
