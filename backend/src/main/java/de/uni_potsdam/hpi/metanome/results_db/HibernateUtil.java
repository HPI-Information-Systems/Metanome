package de.uni_potsdam.hpi.metanome.results_db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
	 
	private static SessionFactory sessionFactory = null;
	
	private HibernateUtil() {
		
	}
 
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
	
	protected static SessionFactory buildSessionFactory() {
		Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.configure().buildSessionFactory(serviceRegistry);
	}
}
