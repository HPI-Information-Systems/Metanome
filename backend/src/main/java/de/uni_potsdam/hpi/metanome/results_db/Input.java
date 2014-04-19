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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Represents superclass inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
public class Input {

    protected int id;

    public static void store(Input input) throws EntityStorageException {
        HibernateUtil.store(input);
    }

    public static Input retrieve(int id) throws EntityStorageException {
        return (Input) HibernateUtil.retrieve(Input.class, id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Input)) return false;

        Input input = (Input) o;

        if (id != input.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
