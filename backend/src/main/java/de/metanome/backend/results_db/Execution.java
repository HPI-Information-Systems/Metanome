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

package de.metanome.backend.results_db;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * Represents an execution in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@Table(name = "execution",
       uniqueConstraints = @UniqueConstraint(columnNames = {"algorithm", "begin"}))
public class Execution implements Serializable, Comparable<Execution> {

  // TODO cascading save to children
  // TODO store proper config

  protected long id;
  protected Algorithm algorithm;
  protected long begin;
  protected long end;
  protected String identifier;
  protected String config;
  protected List<Input> inputs = new ArrayList<>();
  protected Set<Result> results = new HashSet<>();
  protected String hardwareDescription;
  protected String description;
  protected long executionSettingId; //Todo: use fill in -maybe use execution Setting id - same with algorithm?!

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
    this(algorithm, new Date().getTime());
  }

  /**
   * @param algorithm the executed algorithm
   * @param begin     the start time of the execution
   */
  public Execution(Algorithm algorithm, long begin) {
    this.algorithm = algorithm;
    this.begin = begin;
  }

  @Id
  @GeneratedValue
  public long getId() { return id; }

  public Execution setId(long id) {
    this.id = id;

    return this;
  }

  public String getIdentifier() {
    return identifier;
  }

  public Execution setIdentifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

  @ManyToOne
  @JoinColumn(name = "algorithm")
  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public Execution setAlgorithm(Algorithm algorithm) {
    this.algorithm = algorithm;

    return this;
  }

  @Column(name = "begin")
  public long getBegin() {
    return begin;
  }

  public Execution setBegin(long begin) {
    this.begin = begin;

    return this;
  }

  public long getEnd() {
    return end;
  }

  public Execution setEnd(long end) {
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

  public Execution setInputs(List<Input> inputs) {
    this.inputs = inputs;

    return this;
  }

  @OneToMany(
      fetch = FetchType.EAGER,
      mappedBy = "execution",
      cascade = CascadeType.ALL
  )
  @OnDelete(action = OnDeleteAction.CASCADE)
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
    Timestamp timeBegin = new Timestamp(begin);
    Timestamp otherTimeBegin = new Timestamp(execution.begin);
    return timeBegin.equals(otherTimeBegin);
  }

  @Override
  public int hashCode() {
    int result = algorithm != null ? algorithm.hashCode() : 0;
    Timestamp timeBegin = new Timestamp(begin);
    result = 31 * result + (timeBegin.hashCode());
    return result;
  }

  @Override
  public int compareTo(Execution other) {
    if (this.begin < other.getBegin())
      return 1;
    else if (this.begin > other.getBegin())
      return -1;

    // begin is equal
    if (other.getAlgorithm() != null)
      return this.algorithm.compareTo(other.getAlgorithm());
    else
      return 1;
  }

  /**
   * Adds an {@link de.metanome.backend.results_db.Input} to the list of {@link
   * de.metanome.backend.results_db.Input}s.
   *
   * @param input the Input to add.
   */
  public Execution addInput(Input input) {
    inputs.add(input);

    return this;
  }

  /**
   * Adds a {@link de.metanome.backend.results_db.Result} to the list of {@link
   * de.metanome.backend.results_db.Result}s and creates a bidirectional association.
   *
   * @param result the Result to add
   */
  public Execution addResult(Result result) {
    result.setExecution(this);
    results.add(result);

    return this;
  }
}
