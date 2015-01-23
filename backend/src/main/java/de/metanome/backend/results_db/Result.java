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


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents a Result in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
public class Result implements Serializable {

  protected long id;
  protected String fileName;
  protected Execution execution;
  protected boolean ind;
  protected boolean fd;
  protected boolean ucc;
  protected boolean cucc;
  protected boolean od;
  protected boolean basicStat;

  /**
   * Exists for hibernate serialization
   */
  protected Result() {

  }

  /**
   * @param fileName the path to the result file
   */
  public Result(String fileName) {
    this.fileName = fileName;
  }

  @Id
  @GeneratedValue
  public long getId() {
    return this.id;
  }

  public Result setId(long id) {
    this.id = id;

    return this;
  }

  @Column(name = "fileName", unique = true)
  public String getFileName() {
    return fileName;
  }

  public Result setFileName(String filePath) {
    this.fileName = filePath;

    return this;
  }


  @ManyToOne
  @JoinColumn(name = "execution")
  @XmlTransient
  public Execution getExecution() {
    return execution;
  }

  /**
   * A bidirectional association should be created with the {@link de.metanome.backend.results_db.Execution}.
   * Use {@link de.metanome.backend.results_db.Execution#addResult(Result)} to create proper
   * associations.
   *
   * @param execution the Execution to add
   */
  @XmlTransient
  public Result setExecution(Execution execution) {
    this.execution = execution;

    return this;
  }

  public boolean isInd() {
    return ind;
  }

  public Result setInd(boolean isInd) {
    this.ind = isInd;

    return this;
  }

  public boolean isFd() {
    return fd;
  }

  public Result setFd(boolean isFd) {
    this.fd = isFd;

    return this;
  }

  public boolean isUcc() {
    return ucc;
  }

  public Result setUcc(boolean isUcc) {
    this.ucc = isUcc;

    return this;
  }

  public boolean isCucc() {
    return cucc;
  }

  public Result setCucc(boolean isCucc) {
    this.cucc = isCucc;

    return this;
  }
  
  public boolean isOd() {
    return od;
  }

  public Result setOd(boolean isOd) {
    this.od = isOd;

    return this;
  }

  public boolean isBasicStat() {
    return basicStat;
  }

  public Result setBasicStat(boolean isBasicStat) {
    this.basicStat = isBasicStat;

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Result)) {
      return false;
    }

    Result result = (Result) o;

    if (fileName != null ? !fileName.equals(result.fileName) : result.fileName != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return fileName != null ? fileName.hashCode() : 0;
  }
}
