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

package de.metanome.frontend.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class MetanomeEntryPoint implements EntryPoint {

  @Override
  public void onModuleLoad() {
    System.out.println(GWT.getModuleBaseURL());

//
//    Resource resource = new Resource( GWT.getModuleBaseURL() + "algorithms/store/");
//
//    JSONValue request = JSONParser.parseStrict("{\"fileName\":\"example_fd_algorithm.jar\"}");
//
//    resource.post().json(request).send(
//        new JsonCallback() {
//          @Override
//          public void onFailure(Method method, Throwable throwable) {
//
//          }
//
//          @Override
//          public void onSuccess(Method method, JSONValue jsonValue) {
//
//          }
//        });
//


    // body panel
    LayoutPanel bodyPanel = new LayoutPanel();
    bodyPanel.addStyleName("body");

    // heading panel
    FlowPanel heading = new FlowPanel();
    Image metanomeImage = new Image("resources/images/Metanome.png");
    metanomeImage.addStyleName("logo");
    heading.add(metanomeImage);
    heading.addStyleName("heading");

    DockLayoutPanel p = new DockLayoutPanel(Style.Unit.EM);
    p.addNorth(heading, 4);
    p.add(bodyPanel);

    // root panel
    RootLayoutPanel root = RootLayoutPanel.get();
    root.addStyleName("root");
    root.add(p);

    bodyPanel.add(new BasePage());


  }

}
