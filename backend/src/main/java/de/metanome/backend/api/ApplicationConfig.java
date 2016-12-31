/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.api;



import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

// Imports for Multi-Part file Upload in Jersey


public class ApplicationConfig extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    final Set<Class<?>> classes = new HashSet<>();
    classes.add(CORSResponseFilter.class);
    classes.add(CORSRequestFilter.class);

    //Classes for Multipart Upload
    classes.add(MultiPartFeature.class);
    classes.add(LoggingFilter.class);
    return classes;
  }
}
