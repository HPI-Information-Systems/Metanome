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
import javax.persistence.ManyToOne;

/**
 * Represents a Result in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
public class Result {

    protected String filePath;
    protected Execution execution;
    protected boolean isInd;
    protected boolean isFd;
    protected boolean isUcc;
    protected boolean isBasicStat;

    /**
     * Exists for hibernate serialization
     */
    protected Result() {

    }

    /**
     * @param filePath the path to the result file
     */
    public Result(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Stores a result in the database.
     *
     * @param result the result to store
     */
    public static void store(Result result) throws EntityStorageException {
        HibernateUtil.store(result);
    }

    /**
     * Retrieves a result from the databse.
     *
     * @param filePath the result's file path.
     * @return the result
     */
    public static Result retrieve(String filePath) throws EntityStorageException {
        return (Result) HibernateUtil.retrieve(Result.class, filePath);
    }

    @Id
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @ManyToOne(targetEntity = Execution.class)
    public Execution getExecution() {
        return execution;
    }

    /**
     * A bidirectional association should be created with the {@link de.uni_potsdam.hpi.metanome.results_db.Execution}.
     * Use {@link de.uni_potsdam.hpi.metanome.results_db.Execution#addResult(Result)} to create proper associations.
     *
     * @param execution the Execution to add
     */
    public void setExecution(Execution execution) {
        this.execution = execution;
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
        if (!(o instanceof Result)) return false;

        Result result = (Result) o;

        if (filePath != null ? !filePath.equals(result.filePath) : result.filePath != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return filePath != null ? filePath.hashCode() : 0;
    }
}
