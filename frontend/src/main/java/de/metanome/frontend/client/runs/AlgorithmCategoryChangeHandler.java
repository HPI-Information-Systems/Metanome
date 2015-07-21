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

package de.metanome.frontend.client.runs;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;


public class AlgorithmCategoryChangeHandler implements ChangeHandler {

  @Override
  public void onChange(ChangeEvent event) {
    ListBox box = (ListBox) event.getSource();
    String selectedCategory = box.getValue(box.getSelectedIndex());

    AlgorithmChooser jarChooser = (AlgorithmChooser) box.getParent();
    jarChooser.messageReceiver.clearErrors();
    jarChooser.setCurrentCategory(selectedCategory);
    jarChooser.updateAlgorithmListBox();
    ((RunConfigurationPage) jarChooser.getParent()).removeParameterTable();
  }
}
