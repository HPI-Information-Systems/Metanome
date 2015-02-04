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

package de.metanome.algorithm_integration.results.condition_parser;

import de.metanome.algorithm_integration.ColumnCondition;
import de.metanome.algorithm_integration.ColumnConditionAnd;

import org.codehaus.jparsec.functors.Map4;

import java.util.List;

public class AndConditionMapper implements
                                Map4<Void, ColumnCondition, List<ColumnCondition>, Void, ColumnConditionAnd> {

  @Override
  public ColumnConditionAnd map(Void aVoid, ColumnCondition columnConditionValue,
                                List<ColumnCondition> columnConditionValues, Void aVoid2) {
    ColumnConditionAnd and = new ColumnConditionAnd(columnConditionValue);
    for (ColumnCondition value : columnConditionValues)
      and.add(value);
    return and;
  }
}
