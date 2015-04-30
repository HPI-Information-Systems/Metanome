/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.metanome.backend.algorithm_execution;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public final class ProcessExecutor {

  private ProcessExecutor() {
  }

  public static Process exec(Class myClass, String algorithmId, String executionIdentifier,
                             String memory) throws IOException,
                                                   InterruptedException {
    String javaHome = System.getProperty("java.home");
    String javaBin = javaHome +
                     File.separator + "bin" +
                     File.separator + "java";
    String myPath = System.getProperty("java.class.path");
    String className = myClass.getCanonicalName();
    try {
      URL baseUrl = myClass.getProtectionDomain().getCodeSource().getLocation();
      File file = new File(baseUrl.toURI());
      String parent = file.getAbsoluteFile().getParent();
      String
          classesFolder =
          file.getAbsoluteFile().getParentFile().getParent() + File.separator + "classes";
      String parentPathWildCard = parent + File.separator + "*";
      myPath += File.pathSeparator + parentPathWildCard + File.pathSeparator + classesFolder;
    } catch (URISyntaxException ex) {
      // Deal with exception
    }

    ProcessBuilder builder = new ProcessBuilder(
        javaBin, "-classpath", myPath, className, algorithmId, executionIdentifier);
    if (!memory.equals("")) {
      builder = new ProcessBuilder(
          javaBin, "-Xmx" + memory + "m", "-Xms" + memory + "m", "-classpath", myPath, className,
          algorithmId, executionIdentifier);
    }
    builder.redirectErrorStream(true);
    return builder.start();
  }

}
