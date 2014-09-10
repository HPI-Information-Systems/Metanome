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

package de.metanome.algorithm_integration;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Abstract super class for the composite pattern used for column conditions.
 *
 * @author Jens Ehrlich
 */
public interface ColumnCondition extends IsSerializable, Comparable<ColumnCondition> {

  public static final String OPEN_BRACKET = "[";
  public static final String CLOSE_BRACKET = "]";
  public static final String AND = "^";
  public static final String OR = "v";
  public static final String NOT = "\u00AC";

  public String toString();

  public ColumnCondition add(ColumnCondition value);

  public float getCoverage();
}
