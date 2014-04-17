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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * TODO docs
 *
 * @author Jakob Zwiener
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory = null;

    /**
     * @return the singleton {@link SessionFactory}
     */
	public static synchronized SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = buildSessionFactory();
		}

        return sessionFactory;
	}

    /**
	 * @return a fresh db session
	 */
	public static Session openNewSession() {
		return getSessionFactory().openSession();
	}

    /**
     * Stores an entity in the database.
     *
     * @param entity the entity to store
     */
    public static void store(Object entity) throws EntityStorageException {
        if (!entity.getClass().isAnnotationPresent(Entity.class)) {
            throw new EntityStorageException("Entity to store is missing the Entity annotation.");
        }

        Session session = openNewSession();

        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();

        session.close();
    }

    /**
     * Retrieves an entity of the given class and with the given id from the database.
     *
     * @param clazz the class of the entity to retrieve
     * @param id the id of the entity to retrieve
     * @return the requested entity
     */
    public static Object retrieve(Class clazz, Serializable id) throws EntityStorageException {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            throw new EntityStorageException("Queried class is missing the Entity annotation.");
        }

        Session session = openNewSession();

        Object value = session.get(clazz, id);

        session.close();

        return value;
    }

    public static void shutdown() {
        getSessionFactory().close();
        sessionFactory = null;
    }

    protected static SessionFactory buildSessionFactory() {
		Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.configure().buildSessionFactory(serviceRegistry);
	}
}
