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

    public T getByCode(String code) {
        if (code == null || code.trim().isEmpty()) return null;

        Filter filter = new Filter("code", ComparisonOperator.LIKE, code);
        return this.getOneByFilter(filter);
    }

    public T getByLogin(String login) {
        Filter filter = new Filter("login", ComparisonOperator.LIKE, login);
        return this.getOneByFilter(filter);
    }

    public T getByLoginOrCode(String login, String code) {
        Filter loginFilter = new Filter("login", ComparisonOperator.LIKE, login);
        Filter codeFilter = new Filter("code", ComparisonOperator.LIKE, code);
        return this.getOneByFilter(loginFilter.or(codeFilter));
    }

    public boolean existsByCode(String code) {
        Filter filter = new Filter("code", ComparisonOperator.LIKE, code);
        return this.isExists(filter);
    }

    public boolean existsByLogin(String login) {
        Filter filter = new Filter("login", ComparisonOperator.LIKE, login);
        return this.isExists(filter);
    }

    public boolean existsByLoginOrCode(String login, String code) {
        Filter loginFilter = new Filter("login", ComparisonOperator.LIKE, login);
        Filter codeFilter = new Filter("code", ComparisonOperator.LIKE, code);
        return this.isExists(loginFilter.or(codeFilter));
    }

    public Collection<T> getByRole(String role) {
        Filter filter = new Filter("role", ComparisonOperator.LIKE, role);
        return this.getByFilter(filter);
    }
}
