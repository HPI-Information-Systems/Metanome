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

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.*;

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
public class Algorithm implements Serializable, Comparable<Algorithm> {
	private static final long serialVersionUID = -3276487707781514801L;

	protected String fileName;
    protected String name;
    protected String author;
    protected String description;
    protected boolean isInd;
    protected boolean isFd;
    protected boolean isUcc;
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
     * The algorithm should have the appropriate algorithm types set, based on the implemented interfaces.
     *
     * @param fileName the file name of the algorithm jar
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
        if (algorithmInterfaces.contains(BasicStatisticsAlgorithm.class)) {
            setBasicStat(true);
        }
    }
    
    /**
     * This constructor sets all attributes as given, and sets the algorithm types based on the given
     * interfaces. If no name is specified, fileName is used for this purpose.
     * 
     * @param fileName the file name of the algorithm jar
     * @param name the name of the implemented algorithm
     * @param author name(s) of the author(s)
     * @param description any additional information on the algorithm
     * @param algorithmInterfaces the implemented interfaces
     */
    @GwtIncompatible("The algorithm interfaces are not gwt compatible.")
    public Algorithm(String fileName, String name, String author, String description, Set<Class<?>> algorithmInterfaces) {
    	this(fileName, algorithmInterfaces);
    	
    	if (name != null)
    		this.name = name;
    	else
    		this.name = fileName;
    	
    	this.author = author;
    	this.description = description;
    }

    /**
     * Stores an Algorithm in the database.
     *
     * @param algorithm the Algorithm to store
     * @throws de.uni_potsdam.hpi.metanome.results_db.EntityStorageException
     */
    @GwtIncompatible("HibernateUtil is not gwt compatible.")
    public static void store(Algorithm algorithm) throws EntityStorageException {
        HibernateUtil.store(algorithm);
    }

    /**
     * Retrieves an Algorithm from the database.
     *
     * @param fileName the Algorithm's file name
     * @return the algorithm
     * @throws de.uni_potsdam.hpi.metanome.results_db.EntityStorageException
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
    public static List<Algorithm> retrieveAll() {
        return (List<Algorithm>) HibernateUtil.executeNamedQuery("get all");
    }

    /**
     * Retrieves all algorithms stored in the database, that are of a type associated to one of the interfaces.
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
        if (interfaces.contains(BasicStatisticsAlgorithm.class)) {
            criteria.add(Restrictions.eq("basicStat", true));
        }

        List<Algorithm> algorithms = null;
        try {
            algorithms = HibernateUtil.queryCriteria(Algorithm.class, criteria.toArray(new Criterion[criteria.size()]));
        } catch (EntityStorageException e) {
            // Algorithm should implement Entity, so the exception should not occur.
            e.printStackTrace();
        }

        return algorithms;
    }

    @Id
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

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

    public boolean isBasicStat() {
        return isBasicStat;
    }

    public void setBasicStat(boolean isBasicStat) {
        this.isBasicStat = isBasicStat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Algorithm)) return false;

        Algorithm algorithm = (Algorithm) o;

        if (fileName != null ? !fileName.equals(algorithm.fileName) : algorithm.fileName != null) return false;

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
