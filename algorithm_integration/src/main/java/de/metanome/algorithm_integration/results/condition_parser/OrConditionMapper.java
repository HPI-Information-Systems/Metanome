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


import de.metanome.algorithm_integration.ColumnConditionOr;
import de.metanome.algorithm_integration.ColumnCondition;

import java.util.List;

public class OrConditionMapper
    implements
    org.codehaus.jparsec.functors.Map4<Void, ColumnCondition, List<ColumnCondition>, Void, ColumnConditionOr> {

  @Override
  public ColumnConditionOr map(Void aVoid, ColumnCondition columnCondition,
                               List<ColumnCondition> columnConditions, Void aVoid2) {
    ColumnConditionOr or = new ColumnConditionOr(columnCondition);
    for (ColumnCondition cc : columnConditions)
      or.add(cc);
    return or;
  }
}
