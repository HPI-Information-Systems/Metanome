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

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

/**
 * Represents an execution in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@IdClass(ExecutionId.class)
public class Execution {

    // TODO cascading save to children

    protected Algorithm algorithm;
    protected Date begin;
    protected Date end;
    // TODO store proper config
    protected String config;
    protected Collection<Input> inputs = new ArrayList<>();
    protected Set<Result> results = new HashSet<>();
    protected String hardwareDescription;
    protected String description;

    /**
     * Exists for hibernate serialization
     */
    protected Execution() {

    }

    /**
     * @param algorithm the executed algorithm
     * @param begin     the start time of the execution
     */
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

    @ManyToMany(fetch = FetchType.EAGER)
    @CollectionId(
            columns = @Column(name = "ExecutionInput"),
            type = @Type(type = "long"),
            generator = "sequence"
    )
    @JoinTable
    public Collection<Input> getInputs() {
        return inputs;
    }

    public void setInputs(Collection<Input> inputs) {
        this.inputs = inputs;
    }

    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "execution"
    )
    public Set<Result> getResults() {
        return results;
    }

    public void setResults(Set<Result> results) {
        this.results = results;
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

        if (algorithm != null ? !algorithm.equals(execution.algorithm) : execution.algorithm != null) return false;
        if (begin != null ? !begin.equals(execution.begin) : execution.begin != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = algorithm != null ? algorithm.hashCode() : 0;
        result = 31 * result + (begin != null ? begin.hashCode() : 0);
        return result;
    }

    /**
     * Adds an {@link de.uni_potsdam.hpi.metanome.results_db.Input} to the list of
     * {@link de.uni_potsdam.hpi.metanome.results_db.Input}s.
     *
     * @param input the Input to add.
     */
    public void addInput(Input input) {
        inputs.add(input);
    }

    /**
     * Adds a {@link de.uni_potsdam.hpi.metanome.results_db.Result} to the list of {@link de.uni_potsdam.hpi.metanome.results_db.Result}s and creates a bidirectional association.
     *
     * @param result the Result to add
     */
    public void addResult(Result result) {
        result.setExecution(this);
        results.add(result);
    }
}