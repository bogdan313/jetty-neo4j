package database.services;

import database.domains.Person;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.query.SortOrder;

import java.util.Collection;

public class PersonService<T extends Person> extends BaseService<T> {
    public PersonService(Class<T> clazz) {
        super(clazz);
    }

    public Collection<T> getAll() {
        return super.getAll(new SortOrder().add("fullName"));
    }

    public T getByLogin(String login) {
        Filter filter = new Filter("login", ComparisonOperator.LIKE, login);
        return this.getOneByFilter(filter);
    }

    public boolean existsByLogin(String login) {
        Filter filter = new Filter("login", ComparisonOperator.LIKE, login);
        return this.isExists(filter);
    }

    public Collection<T> getByRole(String role) {
        Filter filter = new Filter("role", ComparisonOperator.LIKE, role);
        return this.getByFilter(filter);
    }
}
