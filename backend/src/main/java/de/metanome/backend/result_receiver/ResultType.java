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

package de.metanome.backend.result_receiver;

import de.metanome.backend.algorithm_loading.AlgorithmType;

import java.io.Serializable;

public enum ResultType implements Serializable {

    stat("_stats"),
    fd("_fds"),
    ucc("_uccs"),
    cucc("_cuccs"),
    ind("_inds"),
    od("_ods");

    private String ending;

    ResultType(String ending){
      this.ending = ending;
    }

    public String getEnding() { return this.ending; }
 }

