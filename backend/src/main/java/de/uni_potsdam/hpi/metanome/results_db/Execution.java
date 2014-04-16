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

package de.uni_potsdam.hpi.metanome.results_db;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * TODO docs
 *
 * @author Jakob Zwiener
 */
@Entity
@IdClass(ExecutionId.class)
public class Execution {

    // TODO results
    // TODO execution inputs

    protected Algorithm algorithm;
    protected Date begin;
    protected Date end;
    // TODO store proper config
    protected String config;
    protected String hardwareDescription;
    protected String description;

    /**
     * Exists for hibernate serialization
     */
    private Execution() {

    }

    public Execution(Algorithm algorithm, Date begin) {
        this.algorithm = algorithm;
        this.begin = begin;
    }

    /**
     * Stores an execution in the database.
     *
     * @param execution the execution to store
     */
    public static void store(Execution execution) throws EntityStorageException {
        HibernateUtil.store(execution);
    }

    /**
     * Retrieves an Exectution from the database.
     *
     * @param algorithm the executed algorithm
     * @param begin     the start time of the execution
     * @return the execution
     */
    public static Execution retrieve(Algorithm algorithm, Date begin) throws EntityStorageException {
        return (Execution) HibernateUtil.retrieve(Execution.class, new ExecutionId(algorithm, begin));
    }

    @Id
    @ManyToOne(targetEntity = Algorithm.class)
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Id
    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getHardwareDescription() {
        return hardwareDescription;
    }

    public void setHardwareDescription(String hardwareDescription) {
        this.hardwareDescription = hardwareDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Execution)) return false;

        Execution execution = (Execution) o;

        if (!algorithm.equals(execution.algorithm)) return false;
        if (!begin.equals(execution.begin)) return false;
        if (config != null ? !config.equals(execution.config) : execution.config != null) return false;
        if (description != null ? !description.equals(execution.description) : execution.description != null)
            return false;
        if (end != null ? !end.equals(execution.end) : execution.end != null) return false;
        if (hardwareDescription != null ? !hardwareDescription.equals(execution.hardwareDescription) : execution.hardwareDescription != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = algorithm.hashCode();
        result = 31 * result + begin.hashCode();
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (config != null ? config.hashCode() : 0);
        result = 31 * result + (hardwareDescription != null ? hardwareDescription.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}