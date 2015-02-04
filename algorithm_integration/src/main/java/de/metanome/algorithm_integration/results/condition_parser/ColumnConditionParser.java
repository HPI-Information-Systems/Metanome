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
import de.metanome.algorithm_integration.ColumnConditionOr;
import de.metanome.algorithm_integration.ColumnConditionValue;

import org.codehaus.jparsec.Parser;
import org.codehaus.jparsec.Parsers;
import org.codehaus.jparsec.Scanners;
import org.codehaus.jparsec.pattern.Patterns;

import java.util.List;

public class ColumnConditionParser {

//  S -> EXPR
//  EXPR -> V | OR | AND | NOT
//  NOT -> not EXPR
//  V -> terminal
//  OR -> EXPR or EXPR
//  AND -> EXPR and EXPR

  protected Parser<Void> andOperator = Scanners.string(" ^ ");
  protected Parser<Void> orOperator = Scanners.string(" v ");
  protected Parser<Void> notOperator = Scanners.string("¬");
  protected Parser<Void> openBracketOperator = Scanners.isChar('[');
  protected Parser<Void> closeBracketOperator = Scanners.isChar(']');
  protected Parser<ColumnConditionValue>
      COND =
      Scanners.pattern(Patterns.regex("\\w+\\.\\w+=\\s\\w+"), "value").source().map(new TerminalMapper());

  Parser.Reference<ColumnCondition> EXPR_REF = Parser.newReference();

  protected Parser<List<ColumnCondition>> AND_MANY = andOperator.next(EXPR_REF.lazy()).many1();
  protected Parser<ColumnConditionAnd> AND = Parsers.sequence(openBracketOperator, EXPR_REF.lazy(), AND_MANY, closeBracketOperator, new AndConditionMapper());

  protected Parser<List<ColumnCondition>> OR_MANY = orOperator.next(EXPR_REF.lazy()).many1();
  protected Parser<ColumnConditionOr> OR = Parsers.sequence(openBracketOperator, EXPR_REF.lazy(), OR_MANY,
                                                            closeBracketOperator,
                                                            new OrConditionMapper());


  Parser.Reference<ColumnCondition> NOT_EXPR_REF = Parser.newReference();
  protected Parser<ColumnCondition> EXPR = Parsers.or(OR, AND, COND, NOT_EXPR_REF.lazy());

  {
    EXPR_REF.set(EXPR);
  }

  protected Parser<ColumnCondition> NOT_EXPR = Parsers.sequence(notOperator, EXPR, new NotConditionMapper());

  {
    NOT_EXPR_REF.set(NOT_EXPR);
  }


  public ColumnCondition parse(String s) {
    return EXPR.parse(s);
  }
}
