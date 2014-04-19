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

/**
 * Represents an algorithm in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
public class Algorithm {

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
     * Stores an Algorithm in the database.
     *
     * @param algorithm the Algorithm to store
     * @throws de.uni_potsdam.hpi.metanome.results_db.EntityStorageException
     */
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
    public static Algorithm retrieve(String fileName) throws EntityStorageException {
        return (Algorithm) HibernateUtil.retrieve(Algorithm.class, fileName);
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
}
