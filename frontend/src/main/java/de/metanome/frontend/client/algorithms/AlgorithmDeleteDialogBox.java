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

package de.metanome.frontend.client.algorithms;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;

import de.metanome.backend.results_db.Algorithm;


/**
 * Dialog box, which is shown, if the user wants to delete an algorithm.
 */
public class AlgorithmDeleteDialogBox extends DialogBox {

  public AlgorithmDeleteDialogBox(final AlgorithmsPage page, final Algorithm algorithm) {
    this.setTitle("Deleting algorithm");
    this.setText("If you are going to delete this algorithm, all executions of this algorithm"
                 + "are going to be deleted too."
                 + "Do you still want to delete this algorithm?");

    Button deleteButton = new Button("Delete");
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        page.deleteAlgorithm(algorithm);
        AlgorithmDeleteDialogBox.this.hide();
      }
    });

    Button cancelButton = new Button("Cancel");
    cancelButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        AlgorithmDeleteDialogBox.this.hide();
      }
    });

    Panel content = new FlowPanel();
    content.add(cancelButton);
    content.add(deleteButton);

    this.setWidget(content);
  }
}
