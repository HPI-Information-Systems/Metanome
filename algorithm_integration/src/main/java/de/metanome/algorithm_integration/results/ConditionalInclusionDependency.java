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
package de.metanome.algorithm_integration.results;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import java.util.Map;
import javax.xml.bind.annotation.XmlTransient;


/**
 * Represents a conditional inclusion dependency.
 *
 * @author Joana Bergsiek
 */
@JsonTypeName("ConditionalInclusionDependency")
public class ConditionalInclusionDependency implements Result {
    
    public static final String CID_SEPARATOR = "[=";
    public static final String CID_SEPARATOR_ESC = "\\[=";
    public static final String TABLEAU_SEPARATOR = "#";

    private static final long serialVersionUID = 7828486818686878686L;

    protected ColumnPermutation dependant;
    protected ColumnPermutation referenced;
    protected String patternTableau;
    
    /**
    * Exists for serialization.
    */
    protected ConditionalInclusionDependency() {
        this.referenced = new ColumnPermutation();
        this.dependant = new ColumnPermutation();
        patternTableau = "";
    }
    
    public ConditionalInclusionDependency(ColumnPermutation dependant, ColumnPermutation referenced, String patternTableau) {
        this.dependant = dependant;
        this.referenced = referenced;
        this.patternTableau = patternTableau;
    }
    
    @Override
    @XmlTransient
    public void sendResultTo(OmniscientResultReceiver resultReceiver)
        throws CouldNotReceiveResultException, ColumnNameMismatchException {
        resultReceiver.receiveResult(this);
    }
    
    @Override
    public String toString() {
        return dependant.toString() + CID_SEPARATOR + referenced.toString() + TABLEAU_SEPARATOR + patternTableau;
    }
    
    /**
   * Encodes the conditional inclusion dependency as string with the given mappings.
   * @param tableMapping the table mapping
   * @param columnMapping the column mapping
   * @return the string
   */
    public String toString(Map<String, String> tableMapping, Map<String, String> columnMapping) {
        return dependant.toString(tableMapping, columnMapping) + CID_SEPARATOR + referenced.toString(tableMapping, columnMapping) + TABLEAU_SEPARATOR + patternTableau;
    }
    
    /**
    * Creates a conditional inclusion dependency from the given string using the given mapping.
    * @param tableMapping the table mapping
    * @param columnMapping the column mapping
    * @param str the string
    * @return a conditional inclusion dependency
    */
    public static ConditionalInclusionDependency fromString(Map<String, String> tableMapping, Map<String, String> columnMapping, String str) 
            throws NullPointerException, IndexOutOfBoundsException {
        String[] parts = str.split(CID_SEPARATOR_ESC);
        ColumnPermutation dependant = ColumnPermutation.fromString(tableMapping, columnMapping, parts[0]);
        parts = parts[1].split(TABLEAU_SEPARATOR);
        ColumnPermutation referenced = ColumnPermutation.fromString(tableMapping, columnMapping, parts[0]);
        String patternTableau = parts[1];
        
        return new ConditionalInclusionDependency(dependant, referenced, patternTableau);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ConditionalInclusionDependency other = (ConditionalInclusionDependency) obj;
        if (dependant != null ? !dependant.equals(other.dependant) : other.dependant != null) return false;
        if (referenced != null ? !referenced.equals(other.referenced) : other.referenced != null) return false;
        return patternTableau != null ? patternTableau.equals(other.patternTableau) : other.patternTableau == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
            + ((dependant == null) ? 0 : dependant.hashCode());
        result = prime * result
            + ((referenced == null) ? 0 : referenced.hashCode());
        return result;
    }

    public String getPatternTableau() {
        return patternTableau;
    }

    public void setPatternTableau(String patternTableau) {
        this.patternTableau = patternTableau;
    }

    public ColumnPermutation getDependant() {
        return dependant;
    }

    public void setDependant(ColumnPermutation dependant) {
        this.dependant = dependant;
    }

    public ColumnPermutation getReferenced() {
        return referenced;
    }

    public void setReferenced(ColumnPermutation referenced) {
        this.referenced = referenced;
    }   
}