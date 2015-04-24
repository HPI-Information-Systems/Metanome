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


import com.google.gwt.user.client.ui.FlowPanel;

import de.metanome.backend.results_db.ResultType;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.results.pagination_table.BasicStatisticPaginationTable;
import de.metanome.frontend.client.results.pagination_table.ConditionalUniqueColumnCombinationPaginationTable;
import de.metanome.frontend.client.results.pagination_table.FunctionalDependencyPaginationTable;
import de.metanome.frontend.client.results.pagination_table.InclusionDependencyPaginationTable;
import de.metanome.frontend.client.results.pagination_table.OrderDependencyPaginationTable;
import de.metanome.frontend.client.results.pagination_table.UniqueColumnCombinationPaginationTable;
import de.metanome.frontend.client.services.AlgorithmExecutionRestService;


/**
 * UI element that displays the results of an algorithm execution in a table.
 */
public class ResultsPaginationTablePage extends FlowPanel implements TabContent {

  protected AlgorithmExecutionRestService executionService;

  protected FlowPanel resultsPanel;
  protected TabWrapper messageReceiver;

  public ResultsPaginationTablePage(long executionId) {

    this.resultsPanel = new FlowPanel();
    this.resultsPanel.addStyleName("left");
    this.add(resultsPanel);

    this.add(new InclusionDependencyPaginationTable(executionId, ResultType.IND));
    this.add(new UniqueColumnCombinationPaginationTable(executionId, ResultType.UCC));
    this.add(new ConditionalUniqueColumnCombinationPaginationTable(executionId, ResultType.CUCC));
    this.add(new FunctionalDependencyPaginationTable(executionId, ResultType.FD));
    this.add(new OrderDependencyPaginationTable(executionId, ResultType.OD));
    this.add(new BasicStatisticPaginationTable(executionId, ResultType.STAT));
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {

  }
}
