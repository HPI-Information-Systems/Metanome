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

package de.uni_potsdam.hpi.metanome.frontend.server;

import java.io.File;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_loading.InputDataFinder;
import de.uni_potsdam.hpi.metanome.frontend.client.services.InputDataService;

public class InputDataServiceImpl extends RemoteServiceServlet implements InputDataService {

  private static final long serialVersionUID = -4303653997579507943L;

  private InputDataFinder inputDataFinder = new InputDataFinder();

  /**
   * Lists the name of csv files in the input data folder
   * 
   * @return a list of filenames (without path)
   */
  public String[] listCsvInputFiles() {
    File[] csvInputFiles = null;
    try {
      csvInputFiles = inputDataFinder.getAvailableCsvs();
    } catch (Exception e) {
      // TODO: error handling
      e.printStackTrace();
    }

    String[] csvInputFilePaths = new String[csvInputFiles.length];
    for (int i = 0; i < csvInputFiles.length; i++) {
      csvInputFilePaths[i] = csvInputFiles[i].getPath();
    }

    return csvInputFilePaths;
  }

}
