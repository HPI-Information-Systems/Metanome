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

import com.google.gwt.user.client.rpc.IsSerializable;

import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.DatabaseConnectionParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.TableInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AlgorithmObj implements IsSerializable, Comparable<Algorithm> {

  @JsonProperty("fileName")
  protected String fileName;
  @JsonProperty("name")
  protected String name;
  @JsonProperty("author")
  protected String author;
  @JsonProperty("description")
  protected String description;
  @JsonProperty("isInd")
  protected boolean isInd;
  @JsonProperty("isFd")
  protected boolean isFd;
  @JsonProperty("isUcc")
  protected boolean isUcc;
  @JsonProperty("isCucc")
  protected boolean isCucc;
  @JsonProperty("isOd")
  protected boolean isOd;
  @JsonProperty("isRelationalInput")
  protected boolean isRelationalInput;
  @JsonProperty("isDatabaseConnection")
  protected boolean isDatabaseConnection;
  @JsonProperty("isTableInput")
  protected boolean isTableInput;
  @JsonProperty("isFileInput")
  protected boolean isFileInput;
  @JsonProperty("isBasicStat")
  protected boolean isBasicStat;
//  protected Set<Execution> executions;

  /**
   * Exists for hibernate serialization
   */
  protected AlgorithmObj() {

  }

  /**
   *
   * @param fileName the file name of the algorithm jar
   */
  public AlgorithmObj(String fileName) {
    this.fileName = fileName;
  }

  /**
   * The algorithm should have the appropriate algorithm types set, based on the implemented
   * interfaces.
   *
   * @param fileName            the file name of the algorithm jar
   * @param algorithmInterfaces the implemented interfaces
   */
  public AlgorithmObj(String fileName, Set<Class<?>> algorithmInterfaces) {
    this(fileName);

    if (algorithmInterfaces.contains(InclusionDependencyAlgorithm.class)) {
      setInd(true);
    }
    if (algorithmInterfaces.contains(FunctionalDependencyAlgorithm.class)) {
      setFd(true);
    }
    if (algorithmInterfaces.contains(UniqueColumnCombinationsAlgorithm.class)) {
      setUcc(true);
    }
    if (algorithmInterfaces.contains(ConditionalUniqueColumnCombinationAlgorithm.class)) {
      setCucc(true);
    }
    if (algorithmInterfaces.contains(OrderDependencyAlgorithm.class)) {
      setOd(true);
    }
    if (algorithmInterfaces.contains(BasicStatisticsAlgorithm.class)) {
      setBasicStat(true);
    }
    if (algorithmInterfaces.contains(FileInputParameterAlgorithm.class)) {
      setFileInput(true);
    }
    if (algorithmInterfaces.contains(TableInputParameterAlgorithm.class)) {
      setTableInput(true);
    }
    if (algorithmInterfaces.contains(RelationalInputParameterAlgorithm.class)) {
      setRelationalInput(true);
    }
    if (algorithmInterfaces.contains(DatabaseConnectionParameterAlgorithm.class)) {
      setDatabaseConnection(true);
    }
  }

  /**
   * This constructor sets all attributes as given, and sets the algorithm types based on the given
   * interfaces. If no name is specified, fileName is used for this purpose.
   *
   * @param fileName            the file name of the algorithm jar
   * @param name                the name of the implemented algorithm
   * @param author              name(s) of the author(s)
   * @param description         any additional information on the algorithm
   * @param algorithmInterfaces the implemented interfaces
   */
  public AlgorithmObj(String fileName, String name, String author, String description,
                   Set<Class<?>> algorithmInterfaces) {
    this(fileName, algorithmInterfaces);

    if (name != null) {
      this.name = name;
    } else {
      this.name = fileName;
    }

    this.author = author;
    this.description = description;
  }

  @Id
  public String getFileName() {
    return fileName;
  }

  public AlgorithmObj setFileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  @Column(name = "name", unique = true)
  public String getName() {
    return name;
  }

  public AlgorithmObj setName(String name) {
    this.name = name;
    return this;
  }

  public String getAuthor() {
    return author;
  }

  public AlgorithmObj setAuthor(String author) {
    this.author = author;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public AlgorithmObj setDescription(String description) {
    this.description = description;
    return this;
  }

  public boolean isInd() {
    return isInd;
  }

  public AlgorithmObj setInd(boolean isInd) {
    this.isInd = isInd;
    return this;
  }

  public boolean isFd() {
    return isFd;
  }

  public AlgorithmObj setFd(boolean isFd) {
    this.isFd = isFd;
    return this;
  }

  public boolean isUcc() {
    return isUcc;
  }

  public AlgorithmObj setUcc(boolean isUcc) {
    this.isUcc = isUcc;
    return this;
  }

  public boolean isCucc() {
    return isCucc;
  }

  public AlgorithmObj setCucc(boolean isCucc) {
    this.isCucc = isCucc;
    return this;
  }

  public boolean isOd() {
    return isOd;
  }

  public AlgorithmObj setOd(boolean isOd) {
    this.isOd = isOd;
    return this;
  }

  public boolean isRelationalInput() {
    return isRelationalInput;
  }

  public AlgorithmObj setRelationalInput(boolean isRelationalInput) {
    this.isRelationalInput = isRelationalInput;
    return this;
  }

  public boolean isDatabaseConnection() {
    return isDatabaseConnection;
  }

  public AlgorithmObj setDatabaseConnection(boolean isDatabaseConnection) {
    this.isDatabaseConnection = isDatabaseConnection;
    return this;
  }

  public boolean isTableInput() {
    return isTableInput;
  }

  public AlgorithmObj setTableInput(boolean isTableInput) {
    this.isTableInput = isTableInput;
    return this;
  }

  public boolean isFileInput() {
    return isFileInput;
  }

  public AlgorithmObj setFileInput(boolean isFileInput) {
    this.isFileInput = isFileInput;
    return this;
  }

  public boolean isBasicStat() {
    return isBasicStat;
  }

  public AlgorithmObj setBasicStat(boolean isBasicStat) {
    this.isBasicStat = isBasicStat;
    return this;
  }

//  public Set<Execution> getExecutions() {
//    return executions;
//  }
//
//  public AlgorithmObj setExecutions(Set<Execution> executions) {
//    this.executions = executions;
//    return this;
//  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;

    }
    if (!(o instanceof AlgorithmObj)) {
      return false;
    }

    AlgorithmObj algorithm = (AlgorithmObj) o;

    if (fileName != null ? !fileName.equals(algorithm.fileName) : algorithm.fileName != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return fileName != null ? fileName.hashCode() : 0;
  }

  /* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Algorithm o) {
    return this.getName().compareTo(o.getName());
  }


  @Override
  public String toString() {
    return "Algorithm ["
           +   "fileName=" + fileName
           + ", name=" + name
           + ", author=" + author
           + ", description=" + description
           + ", isInd=" + isInd
           + ", isFd=" + isFd
           + ", isUcc=" + isUcc
           + ", isCucc=" + isCucc
           + ", isOd=" + isOd
           + ", isRelationalInput=" + isRelationalInput
           + ", isDatabaseConnection=" + isDatabaseConnection
           + ", isTableInput=" + isTableInput
           + ", isFileInput=" + isFileInput
           + ", isBasicStat=" + isBasicStat
           + "]";
  }

}
