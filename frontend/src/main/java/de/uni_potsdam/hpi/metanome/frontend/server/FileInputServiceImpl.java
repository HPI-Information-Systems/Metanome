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

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_loading.InputDataFinder;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputService;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;
import de.uni_potsdam.hpi.metanome.results_db.FileInput;
import de.uni_potsdam.hpi.metanome.results_db.Input;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for service that lists available file inputs stored in the database or on the file system.
 */
public class FileInputServiceImpl extends RemoteServiceServlet implements FileInputService {

  private static final long serialVersionUID = -4303653997579507943L;

  private InputDataFinder inputDataFinder = new InputDataFinder();

  /**
   * @return a list of filenames (with path)
   */
  public String[] listCsvFiles() {
    File[] csvFiles = null;

    try {
      csvFiles = inputDataFinder.getAvailableCsvs();
    } catch (Exception e) {
      //TODO: error handling
      System.out.println("FAILED to FIND input CSV files");
      e.printStackTrace();
    }

    String[] csvInputFilePaths = new String[csvFiles.length];
    for (int i = 0; i < csvFiles.length; i++) {
      csvInputFilePaths[i] = csvFiles[i].getPath();
    }

    return csvInputFilePaths;
  }

  /**
   * Lists all file inputs stored in the database
   * @return a list of all currently stored file inputs
   */
  @Override
  public List<FileInput> listFileInputs() {
    try {
      List<FileInput> inputs = new ArrayList<>();
      for (Input input: FileInput.retrieveAll()) {
        inputs.add((FileInput) input);
      }
      return inputs;
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   *
   * @param id the id of the file inputs which should be returned
   * @return the file input with the given id
   */
  @Override
  public FileInput getFileInput(long id) {
    try {
      return FileInput.retrieve(id);
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Stores a file input into the database
   * @param input the file input which should be stored
   */
  @Override
  public void storeFileInput(FileInput input) {
    try {
      input.store();
    } catch (EntityStorageException e) {
      e.printStackTrace();
    }
  }

}
