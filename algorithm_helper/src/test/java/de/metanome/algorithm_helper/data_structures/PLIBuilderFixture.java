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
package de.metanome.algorithm_helper.data_structures;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PLIBuilderFixture {

  protected ArrayList<ArrayList<String>> table = new ArrayList<>();
  protected RelationalInput input;
  int rowPosition;

  public PLIBuilderFixture() {
    table.add(new ArrayList<>(Arrays.asList("1", "1", "5", null)));
    table.add(new ArrayList<>(Arrays.asList("2", "1", "5", "2")));
    table.add(new ArrayList<>(Arrays.asList("3", "1", "3", null)));
    table.add(new ArrayList<>(Arrays.asList("4", "1", "3", "4")));
    table.add(new ArrayList<>(Arrays.asList("5", "1", "5", "5")));
  }

  public RelationalInputGenerator getInputGenerator()
    throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    RelationalInputGenerator inputGenerator = mock(RelationalInputGenerator.class);
    this.input = this.getRelationalInput();
    when(inputGenerator.generateNewCopy())
      .thenAnswer(new Answer<RelationalInput>() {
        public RelationalInput answer(InvocationOnMock invocation) throws Throwable {
          rowPosition = 0;
          return input;
        }
      });
    return inputGenerator;
  }

  protected RelationalInput getRelationalInput() throws InputIterationException {
    RelationalInput input = mock(RelationalInput.class);

    when(input.hasNext()).thenAnswer(new Answer<Boolean>() {
      public Boolean answer(InvocationOnMock invocation) throws Throwable {
        return rowPosition < table.size();
      }
    });

    when(input.next()).thenAnswer(new Answer<ArrayList<String>>() {
      public ArrayList<String> answer(InvocationOnMock invocation) throws Throwable {
        rowPosition += 1;
        return table.get(rowPosition - 1);
      }
    });

    return input;
  }


  public long getExpectedNumberOfTuples() {
    return table.size();
  }

  public List<TreeSet<String>> getExpectedDistinctSortedColumns() {
    List<TreeSet<String>> distinctSortedColumns = new LinkedList<>();

    for (int col = 0; col < table.get(0).size(); col++) {
      TreeSet<String> sortedcolumn = new TreeSet<>();
      for (int row = 0; row < table.size(); row++) {
        String value = table.get(row).get(col);
        if (value != null) {
          sortedcolumn.add(value);
        }
      }
      distinctSortedColumns.add(sortedcolumn);
    }

    return distinctSortedColumns;
  }

  public List<PositionListIndex> getExpectedPLIList(boolean nullEqualsNull) {
    List<PositionListIndex> expectedPLIList = new LinkedList<>();
    List<LongArrayList> list1 = new LinkedList<>();
    PositionListIndex PLI1 = new PositionListIndex(list1);
    expectedPLIList.add(PLI1);

    List<LongArrayList> list2 = new LinkedList<>();
    LongArrayList arrayList21 = new LongArrayList();
    arrayList21.add(0);
    arrayList21.add(1);
    arrayList21.add(2);
    arrayList21.add(3);
    arrayList21.add(4);
    list2.add(arrayList21);
    PositionListIndex PLI2 = new PositionListIndex(list2);
    expectedPLIList.add(PLI2);

    List<LongArrayList> list3 = new LinkedList<>();
    LongArrayList arrayList31 = new LongArrayList();
    LongArrayList arrayList32 = new LongArrayList();

    arrayList31.add(0);
    arrayList31.add(1);
    arrayList31.add(4);

    arrayList32.add(2);
    arrayList32.add(3);

    list3.add(arrayList31);
    list3.add(arrayList32);
    PositionListIndex PLI3 = new PositionListIndex(list3);
    expectedPLIList.add(PLI3);

    List<LongArrayList> list4 = new LinkedList<>();
    if (nullEqualsNull) {
      LongArrayList arrayList41 = new LongArrayList();

      arrayList41.add(0);
      arrayList41.add(2);

      list4.add(arrayList41);
    }

    PositionListIndex PLI4 = new PositionListIndex(list4);
    expectedPLIList.add(PLI4);
    return expectedPLIList;
  }
}
