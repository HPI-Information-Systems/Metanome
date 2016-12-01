/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.result_postprocessing.helper;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class HistogramTest {

  @Test
  public void computeHistogram() {
    List<String> values = new ArrayList<>();
    values.add("1");
    values.add("2");
    values.add("2");
    values.add("3");
    values.add("3");
    values.add("3");
    values.add("4");
    values.add("4");
    values.add("4");
    values.add("4");
    values.add(null);
    values.add(null);

    Histogram histogram = new Histogram();

    // Execute Functionality
    histogram.computeHistogram(values);
    long nullCount = histogram.getNullCount();
    long threeCount = histogram.histogramData.get("3");

    // Check
    assertEquals(5, histogram.histogramData.size());
    assertEquals(3, threeCount);
    assertEquals(2, nullCount);

  }

}
