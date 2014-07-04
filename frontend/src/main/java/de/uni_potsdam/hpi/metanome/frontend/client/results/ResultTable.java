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

package de.uni_potsdam.hpi.metanome.frontend.client.results;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ResultTable extends VerticalPanel {

  private FlexTable table;

  public ResultTable(String title) {
    Label label = new Label(title);
    label.setStyleName("resultTable-caption");
    this.add(label);

    this.table = new FlexTable();
    this.table.setStyleName("resultTable");
    this.add(this.table);

    this.setSpacing(5);
  }

  public void setText(int row, int column, String text) {
    this.table.setText(row, column, text);
  }

  public int getRowCount() {
    return this.table.getRowCount();
  }
}
