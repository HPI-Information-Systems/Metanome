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

package de.metanome.frontend.client.results;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.VisualizationRestService;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;


/**
 * @author Marie Schäffer
 */
public class UCCVisualizationPage extends FlowPanel implements TabContent {

  private TabWrapper messageReceiver;


  private long executionID;

  Frame visualizationFrame;

  private ListBox listBox;

  Image loadingIcon;

  public UCCVisualizationPage(long exID) {
    final Label clickLabel = new Label("Click here to load Visualization");
    clickLabel.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        remove(clickLabel);
        loadingIcon = new Image("ajax-loader.gif");
        add(loadingIcon);
        visualizationFrame = new Frame();
        VisualizationRestService restService = GWT.create(VisualizationRestService.class);
        MethodCallback<Void> callback = getUCCclustersCallback();
        restService.createUCCDiagrams(executionID, callback);
      }
    });
    this.add(clickLabel);
    this.executionID = exID;
    this.getElement().setId("uccVisualizationTab");

  }

  private MethodCallback<Void> getUCCclustersCallback() {
    return new MethodCallback<Void>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {

      }

      @Override
      public void onSuccess(Method method, Void aVoid) {
        remove(loadingIcon);
        listBox = new ListBox();
        listBox.addItem("uccPlotter", "uccPlotter.html");
        listBox.addItem("uccClusterBubbles", "clusterBubbles.html");
        listBox.addChangeHandler(new ChangeHandler() {
          @Override
          public void onChange(ChangeEvent changeEvent) {
            int selectedIndex = listBox.getSelectedIndex();
            String selectedValue = listBox.getValue(selectedIndex);
            if(!visualizationFrame.getUrl().equals(selectedValue)){
              visualizationFrame.setUrl(selectedValue);
            }
          }
        });
        add(listBox);

        //set attributes needed to make sunburst appear in iFrame
        visualizationFrame.getElement().setAttribute("id", "visualizationFrame");
        visualizationFrame.setWidth("98%");
        visualizationFrame.setHeight("93%");
        add(visualizationFrame);
        visualizationFrame.setUrl("uccPlotter.html");

      }
    };
  }


  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }

}
