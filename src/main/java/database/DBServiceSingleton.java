package database;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

public class DBServiceSingleton {
    private final static DBServiceSingleton dbServiceSingleton = new DBServiceSingleton();
    private Configuration configuration;
    private SessionFactory sessionFactory;

    private DBServiceSingleton() {
        this.configuration = new Configuration.Builder()
                .uri("bolt://localhost:7678")
                .credentials("root", "12345")
                .build();
        this.sessionFactory = new SessionFactory(configuration, "database.domains");
    }

    public static DBServiceSingleton getInstance() {
        return DBServiceSingleton.dbServiceSingleton;
    }

    public Session getSession() {
        return this.sessionFactory.openSession();
    }
}
