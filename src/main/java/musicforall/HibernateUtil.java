package musicforall;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hibernate Utility singleton with a convenient method to get Session Factory.
 */
public class HibernateUtil {

	private HibernateUtil() {
	}

	private static final SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);

	/* Create a SessionFactory from hibernate.cfg.xml. */
	static {
		try {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
					configuration.getProperties()).build();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);

		} catch (Throwable ex) {

			logger.error("Initial SessionFactory creation failed: " + ex.getMessage());
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Gets the instance of the SessionFactory.
	 * @return the SessionFactory object
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}

