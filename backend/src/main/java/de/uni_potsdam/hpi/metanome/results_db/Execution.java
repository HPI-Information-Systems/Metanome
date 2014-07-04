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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
  protected Timestamp begin;
  protected Timestamp end;
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
   * Generates an Execution with the current time as start time.
   *
   * @param algorithm the executed algorithm
   */
  public Execution(Algorithm algorithm) {
    this(algorithm, new Timestamp(new Date().getTime()));
  }

  /**
   * @param algorithm the executed algorithm
   * @param begin     the start time of the execution
   */
  public Execution(Algorithm algorithm, Timestamp begin) {
    this.algorithm = algorithm;
    this.begin = begin;
  }

  /**
   * Retrieves an Exectution from the database.
   *
   * @param algorithm the executed algorithm
   * @param begin     the start time of the execution
   * @return the execution
   */
  public static Execution retrieve(Algorithm algorithm, Timestamp begin)
      throws EntityStorageException {
    return (Execution) HibernateUtil.retrieve(Execution.class, new ExecutionId(algorithm, begin));
  }

  /**
   * Retrieves all executions stored in the database
   *
   * @return a list of all executions
   */
  public static List<Execution> retrieveAll() {
    List<Execution> executions = null;

    try {
      executions = HibernateUtil.queryCriteria(Execution.class);
    } catch (EntityStorageException e) {
      // Algorithm should implement Entity, so the exception should not occur.
      e.printStackTrace();
    }

    return executions;
  }

  /**
   * Stores the Execution in the database.
   *
   * @return the Execution
   */
  public Execution store() throws EntityStorageException {
    HibernateUtil.store(this);

    return this;
  }

  @Id
  @ManyToOne(targetEntity = Algorithm.class)
  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public Execution setAlgorithm(Algorithm algorithm) {
    this.algorithm = algorithm;

    return this;
  }

  @Id
  public Timestamp getBegin() {
    return begin;
  }

  public Execution setBegin(Timestamp begin) {
    this.begin = begin;

    return this;
  }

  public Timestamp getEnd() {
    return end;
  }

  public Execution setEnd(Timestamp end) {
    this.end = end;

    return this;
  }

  public String getConfig() {
    return config;
  }

  public Execution setConfig(String config) {
    this.config = config;

    return this;
  }

  @ManyToMany(fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SELECT)
  @CollectionId(
      columns = @Column(name = "ExecutionInput"),
      type = @Type(type = "long"),
      generator = "sequence"
  )
  @JoinTable
  public Collection<Input> getInputs() {
    return inputs;
  }

  public Execution setInputs(Collection<Input> inputs) {
    this.inputs = inputs;

    return this;
  }

  @OneToMany(
      fetch = FetchType.EAGER,
      mappedBy = "execution"
  )
  @Fetch(value = FetchMode.SELECT)
  public Set<Result> getResults() {
    return results;
  }

  public Execution setResults(Set<Result> results) {
    this.results = results;

    return this;
  }

  public String getHardwareDescription() {
    return hardwareDescription;
  }

  public Execution setHardwareDescription(String hardwareDescription) {
    this.hardwareDescription = hardwareDescription;

    return this;
  }

  public String getDescription() {
    return description;
  }

  public Execution setDescription(String description) {
    this.description = description;

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Execution)) {
      return false;
    }

    Execution execution = (Execution) o;

    if (algorithm != null ? !algorithm.equals(execution.algorithm) : execution.algorithm != null) {
      return false;
    }
    if (begin != null ? !begin.equals(execution.begin) : execution.begin != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = algorithm != null ? algorithm.hashCode() : 0;
    result = 31 * result + (begin != null ? begin.hashCode() : 0);
    return result;
  }

  /**
   * Adds an {@link de.uni_potsdam.hpi.metanome.results_db.Input} to the list of {@link
   * de.uni_potsdam.hpi.metanome.results_db.Input}s.
   *
   * @param input the Input to add.
   */
  public Execution addInput(Input input) {
    inputs.add(input);

    return this;
  }

  /**
   * Adds a {@link de.uni_potsdam.hpi.metanome.results_db.Result} to the list of {@link
   * de.uni_potsdam.hpi.metanome.results_db.Result}s and creates a bidirectional association.
   *
   * @param result the Result to add
   */
  public Execution addResult(Result result) {
    result.setExecution(this);
    results.add(result);

    return this;
  }
}
