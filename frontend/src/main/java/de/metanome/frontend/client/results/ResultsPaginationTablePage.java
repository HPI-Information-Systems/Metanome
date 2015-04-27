/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.frontend.client.results;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.Result;
import de.metanome.backend.results_db.ResultType;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.results.pagination_table.BasicStatisticPaginationTable;
import de.metanome.frontend.client.results.pagination_table.ConditionalUniqueColumnCombinationPaginationTable;
import de.metanome.frontend.client.results.pagination_table.FunctionalDependencyPaginationTable;
import de.metanome.frontend.client.results.pagination_table.InclusionDependencyPaginationTable;
import de.metanome.frontend.client.results.pagination_table.OrderDependencyPaginationTable;
import de.metanome.frontend.client.results.pagination_table.UniqueColumnCombinationPaginationTable;
import de.metanome.frontend.client.services.ResultStoreRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * UI element that displays the results of an algorithm execution in a table.
 */
public class ResultsPaginationTablePage extends FlowPanel implements TabContent {

  protected ResultStoreRestService restService;
  protected TabWrapper messageReceiver;

  public ResultsPaginationTablePage() {
    this.restService = GWT.create(ResultStoreRestService.class);
  }

  /**
   * Sends a request to the backend to load the results of the execution into memory. Afterwards the
   * corresponding tables showing the results are added.
   *
   * @param execution the execution
   */
  public void showResultsFor(Execution execution) {
    this.restService.loadExecution(execution.getId(), getMethodCallbackExecution(execution));
  }

  /**
   * Sends a request to the backend to load the results of the file input into memory. Afterwards
   * the corresponding tables showing the results are added.
   *
   * @param fileInput the execution
   */
  public void showResultsFor(FileInput fileInput) {
    this.restService.loadResults(fileInput.getId(), getMethodCallbackResults(fileInput));
  }

  private MethodCallback<Void> getMethodCallbackExecution(final Execution execution) {
    return new MethodCallback<Void>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError(method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, Void aVoid) {
        addTables(execution);
      }
    };
  }

  private MethodCallback<List<String>> getMethodCallbackResults(final FileInput input) {
    return new MethodCallback<List<String>>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError(method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, List<String> types) {
        if (types.isEmpty()) {
          add(new Label("There are no results for the file input " + FilePathHelper
              .getFileName(input.getName()) + "."));
        } else {
          addTables(types);
        }
      }
    };
  }

  /**
   * Adds a table for each result type
   *
   * @param execution the execution
   */
  public void addTables(Execution execution) {
    List<String> types = new ArrayList<>();
    for (Result result : execution.getResults()) {
      types.add(result.getType().getName());
    }
    addTables(types);
  }

  /**
   * Adds a table for each result type
   *
   * @param types the names of the result types
   */
  public void addTables(List<String> types) {
    if (types.contains(ResultType.STAT.getName())) {
      addTitle(ResultType.STAT.getName());
      this.add(new BasicStatisticPaginationTable(ResultType.STAT));
    }

    if (types.contains(ResultType.UCC.getName())) {
      addTitle(ResultType.UCC.getName());
      this.add(new UniqueColumnCombinationPaginationTable(ResultType.UCC));
    }

    if (types.contains(ResultType.CUCC.getName())) {
      addTitle(ResultType.CUCC.getName());
      this.add(new ConditionalUniqueColumnCombinationPaginationTable(ResultType.CUCC));
    }

    if (types.contains(ResultType.FD.getName())) {
      addTitle(ResultType.FD.getName());
      this.add(new FunctionalDependencyPaginationTable(ResultType.FD));
    }

    if (types.contains(ResultType.IND.getName())) {
      addTitle(ResultType.IND.getName());
      this.add(new InclusionDependencyPaginationTable(ResultType.IND));
    }

    if (types.contains(ResultType.OD.getName())) {
      addTitle(ResultType.OD.getName());
      this.add(new OrderDependencyPaginationTable(ResultType.OD));
    }
  }

  private void addTitle(String title) {
    Label label = new Label(title);
    label.setStyleName("resultTable-caption");
    this.add(label);
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }


}
