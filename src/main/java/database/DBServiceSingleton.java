package database;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

/**
 * Database connection for application
 * This is singleton, because we should use only one database driver and multiple sessions
 */
public class DBServiceSingleton {
    private final static DBServiceSingleton dbServiceSingleton = new DBServiceSingleton();
    private final SessionFactory sessionFactory;

    /**
     * Change parameters for your configuration
     */
    private DBServiceSingleton() {
        Configuration configuration = new Configuration.Builder()
                .uri("bolt://localhost:7678")
                .credentials("username", "password")
                .build();
        this.sessionFactory = new SessionFactory(configuration, "database.domains");
    }

    public static DBServiceSingleton getInstance() {
        return DBServiceSingleton.dbServiceSingleton;
    }

    /**
     * Create new session for working with database
     */
    public Session getSession() {
        return this.sessionFactory.openSession();
    }
}
