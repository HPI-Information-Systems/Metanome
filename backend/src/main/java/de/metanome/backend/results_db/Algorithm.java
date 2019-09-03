/**
 * Copyright 2014-2017 by Metanome Project
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
public class Algorithm implements Serializable, Comparable<Algorithm> {

  private static final long serialVersionUID = -8836911135785214586L;

  protected long id;
  protected String fileName;
  protected String name;
  protected String author;
  protected String description;

  // OUTPUT
  //Todo: Introduce types Hashset instead of booleans - after finding way around Hibernate problems
  //See AlgorithmResource > listAlgorithms for example why these variables are needed
  protected HashSet<AlgorithmType> algorithmTypes = new HashSet<>();
  protected boolean ind;
  protected boolean fd;
  protected boolean md;
  protected boolean cfd;
  protected boolean ucc;
  protected boolean cucc;
  protected boolean od;
  protected boolean mvd;
  protected boolean basicStat;
  protected boolean dc;
  protected boolean cid;

  // INPUT
  protected boolean relationalInput;
  protected boolean dbConnection;
  protected boolean tableInput;
  protected boolean fileInput;
  protected List<Execution> executions = new ArrayList<>();

  /**
   * Exists for hibernate serialization
   */
  protected Algorithm() {

  }

  /**
   * @param fileName the file name of the algorithm jar
   */
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
  public Algorithm(String fileName, Set<Class<?>> algorithmInterfaces) {
    this(fileName);
    
    this.algorithmTypes = AlgorithmType.asStream()
            .filter( type -> algorithmInterfaces.contains(type.getAlgorithmClass()))
            .collect(Collectors.toCollection(HashSet::new));
    
    // INPUT
    this.ind = algorithmInterfaces.contains(AlgorithmType.IND.getAlgorithmClass());
    this.fd = algorithmInterfaces.contains(AlgorithmType.FD.getAlgorithmClass());
    this.cid = algorithmInterfaces.contains(AlgorithmType.CID.getAlgorithmClass());
    this.md = algorithmInterfaces.contains(AlgorithmType.MD.getAlgorithmClass());
    this.cfd = algorithmInterfaces.contains(AlgorithmType.CFD.getAlgorithmClass());
    this.ucc = algorithmInterfaces.contains(AlgorithmType.UCC.getAlgorithmClass());
    this.cucc = algorithmInterfaces.contains(AlgorithmType.CUCC.getAlgorithmClass());
    this.od = algorithmInterfaces.contains(AlgorithmType.OD.getAlgorithmClass());
    this.mvd = algorithmInterfaces.contains(AlgorithmType.MVD.getAlgorithmClass());
    this.basicStat = algorithmInterfaces.contains(AlgorithmType.BASIC_STAT.getAlgorithmClass());
    this.dc = algorithmInterfaces.contains(AlgorithmType.DC.getAlgorithmClass());
    // OUTPUT
    this.fileInput = algorithmInterfaces.contains(AlgorithmType.FILE_INPUT.getAlgorithmClass());
    this.tableInput = algorithmInterfaces.contains(AlgorithmType.TABLE_INPUT.getAlgorithmClass());
    this.relationalInput = algorithmInterfaces.contains(AlgorithmType.RELATIONAL_INPUT.getAlgorithmClass());
    this.dbConnection = algorithmInterfaces.contains(AlgorithmType.DB_CONNECTION.getAlgorithmClass());
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

  @Id
  @GeneratedValue
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Column(name = "fileName", unique = true)
  public String getFileName() {
    return fileName;
  }

  public Algorithm setFileName(String fileName) {
    this.fileName = fileName;
    return this;
  }

  @Column(name = "name", unique = true)
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
    return ind;
  }

  public Algorithm setInd(boolean isInd) {
    this.ind = isInd;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.IND, isInd);
    return this;
  }

  public boolean isFd() {
    return fd;
  }

  public Algorithm setFd(boolean isFd) {
    this.fd = isFd;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.FD, isFd);
    return this;
  }
  
   public boolean isCid() {
    return cid;
  }

  public Algorithm setCid(boolean isCid) {
    this.cid = isCid;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.CID, isCid);
    return this;
  }

  public boolean isMd() {
    return md;
  }

  public Algorithm setMd(boolean isMd) {
    this.md = isMd;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.MD, isMd);
    return this;
  }

  public boolean isCfd() {
    return cfd;
  }

  public Algorithm setCfd(boolean isCfd) {
    this.cfd = isCfd;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.CFD, isCfd);
    return this;
  }

  public boolean isUcc() {
    return ucc;
  }

  public Algorithm setUcc(boolean isUcc) {
    this.ucc = isUcc;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.UCC, isUcc);
    return this;
  }

  public boolean isCucc() {
    return cucc;
  }

  public Algorithm setCucc(boolean isCucc) {
    this.cucc = isCucc;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.CUCC, isCucc);
    return this;
  }

  public boolean isOd() {
    return od;
  }

  public Algorithm setOd(boolean isOd) {
    this.od = isOd;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.OD, isOd);
    return this;
  }
  
  public boolean isMvd() {
    return mvd;
  }

  public Algorithm setMvd(boolean isMvd) {
    this.mvd = isMvd;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.MVD, isMvd);
    return this;
  }
  
  public boolean isBasicStat() {
    return basicStat;
  }

  public Algorithm setBasicStat(boolean isBasicStat) {
    this.basicStat = isBasicStat;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.BASIC_STAT, isBasicStat);
    return this;
  }
  
  public boolean isDc() {
    return dc;
  }

  public Algorithm setDc(boolean isDc) {
    this.dc = isDc;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.DC, isDc);
    return this;
  }

  public boolean isRelationalInput() {
    return relationalInput;
  }

  public Algorithm setRelationalInput(boolean isRelationalInput) {
    this.relationalInput = isRelationalInput;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.RELATIONAL_INPUT, isRelationalInput);
    return this;
  }

  public boolean isDbConnection() {
    return dbConnection;
  }

  public Algorithm setDbConnection(boolean isDbConnection) {
    this.dbConnection = isDbConnection;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.DB_CONNECTION, isDbConnection);
    return this;
  }

  public boolean isTableInput() {
    return tableInput;
  }

  public Algorithm setTableInput(boolean isTableInput) {
    this.tableInput = isTableInput;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.TABLE_INPUT, isTableInput);
    return this;
  }

  public boolean isFileInput() {
    return fileInput;
  }

  public Algorithm setFileInput(boolean isFileInput) {
    this.fileInput = isFileInput;
    setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType.FILE_INPUT, isFileInput);
    return this;
  }

  public HashSet<AlgorithmType> getAlgorithmTypes() {
    return algorithmTypes;
  }

  public void setAlgorithmTypes(HashSet<AlgorithmType> algorithmTypes) {
    this.algorithmTypes = algorithmTypes;
  }
  
  public boolean hasAlgorithmType(AlgorithmType algorithmType) {
    return this.algorithmTypes.contains(algorithmType);
  }
  
  public Algorithm setAlgorithmType (AlgorithmType algorithmType, boolean hasAlgorithmType) {
    setAlgorithmTypeToAlgorithmTypeSet(algorithmType, hasAlgorithmType);
    setPropertiesAccordingToAlgorithmTypeSet();
    return this;
  }
  
  private void setAlgorithmTypeProperty(AlgorithmType algorithmType, boolean hasAlgorithmType) {
      if (algorithmType.equals(AlgorithmType.IND)) {
          setInd(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.FD)) {
          setFd(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.CID)) {
          setCid(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.MD)) {
          setMd(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.CFD)) {
          setCfd(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.UCC)) {
          setUcc(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.CUCC)) {
          setCucc(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.OD)) {
          setOd(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.MVD)) {
          setMvd(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.DC)) {
          setDc(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.BASIC_STAT)) {
          setBasicStat(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.FILE_INPUT)) {
          setFileInput(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.DB_CONNECTION)) {
          setDbConnection(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.TABLE_INPUT)) {
          setTableInput(hasAlgorithmType);
      }
      if (algorithmType.equals(AlgorithmType.RELATIONAL_INPUT)) {
          setRelationalInput(hasAlgorithmType);
      }
  }
  
  private void setAlgorithmTypeToAlgorithmTypeSet(AlgorithmType algorithmType, boolean hasAlgorithmType) {
      if (hasAlgorithmType) {
        this.algorithmTypes.add(algorithmType);
      } else {
        this.algorithmTypes.remove(algorithmType);
      }
  }
  
  /**
   * Iterates through every AlgorithmType and sets this class' properties
   * accordingly to whether algorithmTypes contains a certain AlgorithmType or not.
   */ 
  private void setPropertiesAccordingToAlgorithmTypeSet() {
        for (AlgorithmType type : AlgorithmType.values()) {
            if (hasAlgorithmType(type)) {
                setAlgorithmTypeProperty(type, true);
            } else {
                setAlgorithmTypeProperty(type, false);
            }
        }
    }
  

  @XmlTransient
  @JsonIgnore
  @OneToMany(
    fetch = FetchType.EAGER,
    mappedBy = "algorithm",
    cascade = CascadeType.ALL
  )
  @OnDelete(action = OnDeleteAction.CASCADE)
  public List<Execution> getExecutions() {
    return executions;
  }

  @XmlTransient
  @JsonIgnore
  public Algorithm setExecutions(List<Execution> executions) {
    this.executions = executions;
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


  @Override
  public String toString() {
    String algorithmListing =
        "Algorithm ["
        + "fileName=" + fileName
        + ", name=" + name
        + ", author=" + author
        + ", description=" + description;
    
    algorithmListing = AlgorithmType.asStream()
            .map( type -> ", " + type.getAbbreviation() + " = " + hasAlgorithmType(type))
            .reduce(algorithmListing, String::concat);
    
    algorithmListing += "]";
    
    return algorithmListing;
  }
}