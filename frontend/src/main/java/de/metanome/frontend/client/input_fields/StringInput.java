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

package de.metanome.frontend.client.input_fields;

import com.google.gwt.user.client.ui.TextBox;

/**
 * A wrapper for a text box that can contain a remove button. If the remove button is clicked, the
 * checkbox is removed from the parent widget.
 *
 * @author Claudia Exeler
 */
public class StringInput extends InputField {

  public TextBox textbox;

  /**
   * @param optional If true, a remove button will be rendered, to remove this widget from its
   *                 parent.
   */
  public StringInput(boolean optional) {
    super(optional);

    this.textbox = new TextBox();
    this.add(this.textbox);
  }

  /**
   * @return the value of its text box
   */
  public String getValue() {
    return this.textbox.getValue();
  }

  /**
   * Sets the value of the text box
   *
   * @param value the value which should be set
   */
  public void setValue(String value) {
    this.textbox.setText(value);
  }
}
