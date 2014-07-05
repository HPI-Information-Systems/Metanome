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

package de.uni_potsdam.hpi.metanome.test_helper;

import com.google.gwt.rpc.client.ast.HasValues;
import com.google.gwt.rpc.client.ast.ReturnCommand;
import com.google.gwt.rpc.client.impl.HasValuesCommandSink;
import com.google.gwt.rpc.server.CommandServerSerializationStreamWriter;
import com.google.gwt.rpc.server.HostedModeClientOracle;
import com.google.gwt.user.client.rpc.SerializationException;

import static org.junit.Assert.fail;

public class GwtSerializationTester {

  public static void checkGwtSerializability(Object o) {
    HostedModeClientOracle hmco = new HostedModeClientOracle();
    HasValues command = new ReturnCommand();
    HasValuesCommandSink hvcs = new HasValuesCommandSink(command);
    CommandServerSerializationStreamWriter
        out =
        new CommandServerSerializationStreamWriter(hmco, hvcs);

    try {
      out.writeObject(o);
    } catch (SerializationException e) {
      fail("Object not serializable: " + o + " Caused by: " + e.getMessage());
    }

    try {
      o.getClass().getDeclaredConstructor();
    } catch (NoSuchMethodException e) {
      fail("Object not serializable: " + o + " Caused by: " + e.getMessage());
    }
  }

}
