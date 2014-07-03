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

package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import com.google.common.collect.ImmutableList;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInput;

import it.unimi.dsi.fastutil.longs.LongArrayList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PLIBuilderFixture {

  protected List<ArrayList<String>> columns = new LinkedList<>();

  public PLIBuilderFixture() {
    ArrayList<String> column1 = new ArrayList<>();
    column1.add("1");
    column1.add("2");
    column1.add("3");
    column1.add("4");
    column1.add("5");
    columns.add(column1);
    ArrayList<String> column2 = new ArrayList<>();
    column2.add("1");
    column2.add("1");
    column2.add("1");
    column2.add("1");
    column2.add("1");
    columns.add(column2);
    ArrayList<String> column3 = new ArrayList<>();
    column3.add("5");
    column3.add("5");
    column3.add("3");
    column3.add("3");
    column3.add("5");
    columns.add(column3);
    ArrayList<String> column4 = new ArrayList<>();
    column4.add("");
    column4.add("2");
    column4.add("");
    column4.add("4");
    column4.add("5");
    columns.add(column4);
  }

  public RelationalInput getSimpleRelationalInput() throws InputIterationException {
    RelationalInput simpleRelationalInput = mock(RelationalInput.class);

    when(simpleRelationalInput.next())
        .thenReturn(ImmutableList
                        .of(columns.get(0).get(0), columns.get(1).get(0), columns.get(2).get(0),
                            columns.get(3).get(0)))
        .thenReturn(ImmutableList
                        .of(columns.get(0).get(1), columns.get(1).get(1), columns.get(2).get(1),
                            columns.get(3).get(1)))
        .thenReturn(ImmutableList
                        .of(columns.get(0).get(2), columns.get(1).get(2), columns.get(2).get(2),
                            columns.get(3).get(2)))
        .thenReturn(ImmutableList
                        .of(columns.get(0).get(3), columns.get(1).get(3), columns.get(2).get(3),
                            columns.get(3).get(3)))
        .thenReturn(ImmutableList
                        .of(columns.get(0).get(4), columns.get(1).get(4), columns.get(2).get(4),
                            columns.get(3).get(4)));

    when(simpleRelationalInput.hasNext())
        .thenReturn(true)
        .thenReturn(true)
        .thenReturn(true)
        .thenReturn(true)
        .thenReturn(true)
        .thenReturn(false);

    return simpleRelationalInput;
  }

  public List<TreeSet<String>> getExpectedDistinctSortedColumns() {
    List<TreeSet<String>> distinctSortedColumns = new LinkedList<>();

    for (ArrayList<String> columnValues : columns) {
      distinctSortedColumns.add(new TreeSet<>(columnValues));
    }

    return distinctSortedColumns;
  }

  public List<PositionListIndex> getExpectedPLIList() {
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
    LongArrayList arrayList41 = new LongArrayList();

    arrayList41.add(0);
    arrayList41.add(2);

    list4.add(arrayList41);
    PositionListIndex PLI4 = new PositionListIndex(list4);
    expectedPLIList.add(PLI4);

    return expectedPLIList;
  }
}
