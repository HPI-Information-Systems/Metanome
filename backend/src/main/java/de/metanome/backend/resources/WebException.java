/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.backend.resources;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class WebException extends WebApplicationException {

  private static final long serialVersionUID = 7721854828381523887L;

  /**
   * Create a HTTP 400 (Bad Request) exception.
   */
  public WebException() {
    super(Response.Status.BAD_REQUEST);
  }

  /**
   * @param throwable the exception, which should be wrapped
   * @param status    the status of the exception
   */
  public WebException(Throwable throwable, Response.Status status) {
    super(Response.status(status).
      entity(throwable.getMessage()).type("text/plain").build());
  }

  /**
   * @param message the exception message
   * @param status  the status of the exception
   */
  public WebException(String message, Response.Status status) {
    super(Response.status(status).
      entity(message).type("text/plain").build());
  }

}
