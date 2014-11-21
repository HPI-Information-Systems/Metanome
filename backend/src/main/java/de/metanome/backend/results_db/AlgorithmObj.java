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

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
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

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries(
    @NamedQuery(
        name = "get all",
        query = "from Algorithm"
    )
)
@GwtCompatible
@Entity
public class AlgorithmObj extends ResultsDbEntity implements IsSerializable, Comparable<Algorithm> {

  protected String fileName;
  protected String name;
  protected String author;
  protected String description;
  protected boolean isInd;
  protected boolean isFd;
  protected boolean isUcc;
  protected boolean isCucc;
  protected boolean isOd;
  protected boolean isRelationalInput;
  protected boolean isDatabaseConnection;
  protected boolean isTableInput;
  protected boolean isFileInput;
  protected boolean isBasicStat;
  protected Set<Execution> executions;

  /**
   * Exists for hibernate serialization
   */
  protected AlgorithmObj() {

  }

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

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @Column(name = "name", unique = true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isInd() {
    return isInd;
  }

  public void setInd(boolean isInd) {
    this.isInd = isInd;
  }

  public boolean isFd() {
    return isFd;
  }

  public void setFd(boolean isFd) {
    this.isFd = isFd;
  }

  public boolean isUcc() {
    return isUcc;
  }

  public void setUcc(boolean isUcc) {
    this.isUcc = isUcc;
  }

  public boolean isCucc() {
    return isCucc;
  }

  public void setCucc(boolean isCucc) {
    this.isCucc = isCucc;
  }

  public boolean isOd() {
    return isOd;
  }

  public void setOd(boolean isOd) {
    this.isOd = isOd;
  }

  public boolean isRelationalInput() {
    return isRelationalInput;
  }

  public void setRelationalInput(boolean isRelationalInput) {
    this.isRelationalInput = isRelationalInput;
  }

  public boolean isDatabaseConnection() {
    return isDatabaseConnection;
  }

  public void setDatabaseConnection(boolean isDatabaseConnection) {
    this.isDatabaseConnection = isDatabaseConnection;
  }

  public boolean isTableInput() {
    return isTableInput;
  }

  public void setTableInput(boolean isTableInput) {
    this.isTableInput = isTableInput;
  }

  public boolean isFileInput() {
    return isFileInput;
  }

  public void setFileInput(boolean isFileInput) {
    this.isFileInput = isFileInput;
  }

  public boolean isBasicStat() {
    return isBasicStat;
  }

  public void setBasicStat(boolean isBasicStat) {
    this.isBasicStat = isBasicStat;
  }

  @OneToMany(cascade = CascadeType.REMOVE)
  public Set<Execution> getExecutions() {
    return executions;
  }

  public void setExecutions(Set<Execution> executions) {
    this.executions = executions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;

    }
    if (!(o instanceof Algorithm)) {
      return false;
    }

    Algorithm algorithm = (Algorithm) o;

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
  @GwtIncompatible("HibernateUtil is not gwt compatible.")
  public AlgorithmObj store() throws EntityStorageException {
    HibernateUtil.store(this);

    return this;
  }
}
