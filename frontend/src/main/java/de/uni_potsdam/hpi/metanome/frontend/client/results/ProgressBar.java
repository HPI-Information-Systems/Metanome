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

import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class ProgressBar extends Widget {

  private static final String PERCENT_PATTERN = "#,##0%";
  private static final NumberFormat percentFormat = NumberFormat.getFormat(PERCENT_PATTERN);

  private final Element progress;
  private final Element percentageLabel;
  private final double max;
  private double percentage;

  public ProgressBar(double value, double max) {
    assert max != 0;
    this.max = max;

    progress = DOM.createElement("progress");
    progress.setAttribute("max", Double.toString(max));
    progress.setAttribute("value", Double.toString(value));

    percentageLabel = DOM.createElement("span");
    percentage = value / max;
    percentageLabel.setInnerHTML(percentFormat.format(percentage));
    progress.insertFirst(percentageLabel);

    setElement(progress);
  }

  public void setProgress(double value) {
    progress.setAttribute("value", Double.toString(value));
    percentage = value / max;
    percentageLabel.setInnerHTML(percentFormat.format(percentage));
  }
}
