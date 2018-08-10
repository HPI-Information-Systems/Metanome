/**
 * Copyright 2014-2016 by Metanome Project
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
import com.google.common.annotations.GwtCompatible;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;

/**
 * Represents superclass inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@GwtCompatible
public class ParameterValue implements Serializable {

    private static final long serialVersionUID = -7086702450298405009L;

    protected long id;
    protected ExecutionSetting execution;
    private String parameterValue;

    // Exists for Serialization
    public ParameterValue() {
    }

    public ParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public ParameterValue setId(long id) {
        this.id = id;

        return this;
    }

    @XmlTransient
    @ElementCollection
    @Column(columnDefinition = "LONGVARCHAR")
    public String getParameterValue() {
        return this.parameterValue;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }


    @ManyToOne
    @JoinColumn(name = "executionSetting")
    @XmlTransient
    @JsonIgnore
    public ExecutionSetting getExecutionSetting() {
        return execution;
    }

    /**
     * A bidirectional association should be created with the {@link de.metanome.backend.results_db.ExecutionSetting}.
     *
     * @param executionSetting the ExecutionSetting to add
     * @return the modified result
     */
    @XmlTransient
    @JsonIgnore
    public ParameterValue setExecutionSetting(ExecutionSetting executionSetting) {
        this.execution = executionSetting;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParameterValue)) {
            return false;
        }

        ParameterValue paramValue = (ParameterValue) o;

        return id == paramValue.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Transient
    public String getIdentifier() {
        return String.valueOf(id);
    }
}
