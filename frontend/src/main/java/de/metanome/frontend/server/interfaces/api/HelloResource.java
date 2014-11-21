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

package de.metanome.frontend.server.interfaces.api;

import de.metanome.frontend.shared.domain.Hello;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("hellos")
public class HelloResource {

  Map<String, Hello> database;
  public HelloResource() {
    database = new HashMap<String, Hello>();
    Hello hello1 = new Hello("1", "ronan");
    Hello hello2 = new Hello("2", "john");

    database.put(hello1.getId(), hello1);
    database.put(hello2.getId(), hello2);
  }

  @GET
  @Produces("application/json")
  public Collection<Hello> get() {
    return database.values();
  }

  @GET
  @Path("/{id}")
  @Produces("application/json")
  public Hello getHello(@PathParam("id") String id) {
    return database.get(id);
  }
}
