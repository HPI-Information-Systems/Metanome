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

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Represents an algorithm in the database.
 *
 * @author Jakob Zwiener
 */
@NamedQueries(
    @NamedQuery(
        name = "get all",
        query = "from Algorithm"
    )
)
@Entity
@GwtCompatible
public class Algorithm extends ResultsDbEntity implements Serializable, Comparable<Algorithm> {

  private static final long serialVersionUID = -3276487707781514801L;

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

  /**
   * Exists for hibernate serialization
   */
  protected Algorithm() {

  }

  public Algorithm(String fileName) {
    this.fileName = fileName;
  }

  /**
   * The algorithm should have the appropriate algorithm types set, based on the implemented
   * interfaces.
   *
   * @param fileName            the file name of the algorithm jar
   * @param algorithmInterfaces the implemented interfaces
   */
  @GwtIncompatible("The algorithm interfaces are not gwt compatible.")
  public Algorithm(String fileName, Set<Class<?>> algorithmInterfaces) {
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
  @GwtIncompatible("The algorithm interfaces are not gwt compatible.")
  public Algorithm(String fileName, String name, String author, String description,
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

  /**
   * Retrieves an Algorithm from the database.
   *
   * @param fileName the Algorithm's file name
   * @return the algorithm
   */
  @GwtIncompatible("HibernateUtil is not gwt compatible.")
  public static Algorithm retrieve(String fileName) throws EntityStorageException {
    return (Algorithm) HibernateUtil.retrieve(Algorithm.class, fileName);
  }

  /**
   * Retrieves all algorithms stored in the database.
   *
   * @return a list of all algorithms
   */
  @GwtIncompatible("HibernateUtil is not gwt compatible.")
  public static List<Algorithm> retrieveAll() throws EntityStorageException {
    return HibernateUtil.queryCriteria(Algorithm.class);
  }

  /**
   * Retrieves all algorithms stored in the database, that are of a type associated to one of the
   * interfaces.
   *
   * @param algorithmInterfaces algorithm interfaces specifying the expected algorithm type
   * @return a list of matching algorithms
   */
  @GwtIncompatible("HibernateUtil, Criterion and Restrictions are not gwt compatible")
  public static List<Algorithm> retrieveAll(Class<?>... algorithmInterfaces) {
    // Cannot directly use array, as some interfaces might not be relevant for query.
    ArrayList<Criterion> criteria = new ArrayList<>(algorithmInterfaces.length);

    Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(algorithmInterfaces));

    if (interfaces.contains(InclusionDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("ind", true));
    }
    if (interfaces.contains(FunctionalDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("fd", true));
    }

    if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class)) {
      criteria.add(Restrictions.eq("ucc", true));
    }

    if (interfaces.contains(ConditionalUniqueColumnCombinationAlgorithm.class)) {
      criteria.add(Restrictions.eq("cucc", true));
    }
    
    if (interfaces.contains(OrderDependencyAlgorithm.class)) {
      criteria.add(Restrictions.eq("od", true));
    }

    if (interfaces.contains(BasicStatisticsAlgorithm.class)) {
      criteria.add(Restrictions.eq("basicStat", true));
    }

    List<Algorithm> algorithms = null;
    try {
      algorithms =
          HibernateUtil
              .queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));
    } catch (EntityStorageException e) {
      // Algorithm should implement Entity, so the exception should not occur.
      e.printStackTrace();
    }

    return algorithms;
  }

  /**
   * Stores the Algorithm in the database.
   *
   * @return the Algorithm
   */
  @Override
  @GwtIncompatible("HibernateUtil is not gwt compatible.")
  public Algorithm store() throws EntityStorageException {
    HibernateUtil.store(this);

    return this;
  }

  @Id
  public String getFileName() {
    return fileName;
  }

  public Algorithm setFileName(String fileName) {
    this.fileName = fileName;

    return this;
  }

  public String getName() {
    return name;
  }

  public Algorithm setName(String name) {
    this.name = name;

    return this;
  }

  public String getAuthor() {
    return author;
  }

  public Algorithm setAuthor(String author) {
    this.author = author;

    return this;
  }

  public String getDescription() {
    return description;
  }

  public Algorithm setDescription(String description) {
    this.description = description;

    return this;
  }

  public boolean isInd() {
    return isInd;
  }

  public Algorithm setInd(boolean isInd) {
    this.isInd = isInd;

    return this;
  }

  public boolean isFd() {
    return isFd;
  }

  public Algorithm setFd(boolean isFd) {
    this.isFd = isFd;

    return this;
  }

  public boolean isUcc() {
    return isUcc;
  }

  public Algorithm setUcc(boolean isUcc) {
    this.isUcc = isUcc;

    return this;
  }

  public boolean isCucc() {
    return isCucc;
  }

  public Algorithm setCucc(boolean isCucc) {
    this.isCucc = isCucc;

    return this;
  }
  
  public boolean isOd() {
    return isOd;
  }
  
  public Algorithm setOd(boolean isOd) {
    this.isOd = isOd;
    
    return this;
  }

  public boolean isBasicStat() {
    return isBasicStat;
  }

  public Algorithm setBasicStat(boolean isBasicStat) {
    this.isBasicStat = isBasicStat;

    return this;
  }

  public boolean isRelationalInput() { return this.isRelationalInput; }

  public Algorithm setRelationalInput(boolean hasInput) {
    this.isRelationalInput = hasInput;

    return this;
  }

  public boolean isTableInput() { return this.isTableInput; }

  public Algorithm setTableInput(boolean hasInput) {
    this.isTableInput = hasInput;

    return this;
  }

  public boolean isFileInput() { return this.isFileInput; }

  public Algorithm setFileInput(boolean hasInput) {
    this.isFileInput = hasInput;

    return this;
  }

  public boolean isDatabaseConnection() { return this.isDatabaseConnection; }

  public Algorithm setDatabaseConnection(boolean hasInput) {
    this.isDatabaseConnection = hasInput;

    return this;
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
}
