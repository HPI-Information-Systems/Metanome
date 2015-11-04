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

package de.metanome.frontend;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class AngularJsWebApp {

  public static void main(String[] args) throws Exception {

      final ResourceConfig application = new ResourceConfig()
              .packages("de.metanome.backend.resources")
              .register(de.metanome.backend.api.CORSResponseFilter.class)
              .register(de.metanome.backend.api.CORSRequestFilter.class)
              .register(JacksonFeature.class);

      ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
      context.setContextPath("/");

      Server jettyServer = new Server(8888);
      jettyServer.setHandler(context);

      ServletHolder jerseyServlet = new ServletHolder(new
              org.glassfish.jersey.servlet.ServletContainer(application));
      jerseyServlet.setInitOrder(0);

      context.addServlet(jerseyServlet, "/api/*");

      DefaultServlet defaultServlet = new DefaultServlet();
      ServletHolder holder = new ServletHolder(defaultServlet);
      holder.setInitParameter("useFileMappedBuffer", "false");
      context.addServlet(holder, "/");

      context.addEventListener(new DatabaseInitializer());

      try {
          jettyServer.start();
          jettyServer.join();
      } catch (Exception e) {
          System.out.println("Could not start server!");
          e.printStackTrace();
      } finally {
          jettyServer.destroy();
      }
  }
}
