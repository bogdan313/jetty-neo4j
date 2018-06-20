package database.services;

import com.sun.istack.internal.NotNull;
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

    public T getByCode(@NotNull String code) {
        Filter filter = new Filter("code", ComparisonOperator.LIKE, code);
        return this.getOneByFilter(filter);
    }

    public T getByLogin(@NotNull String login) {
        Filter filter = new Filter("login", ComparisonOperator.LIKE, login);
        return this.getOneByFilter(filter);
    }

    public T getByLoginOrCode(@NotNull String login, @NotNull String code) {
        Filter loginFilter = new Filter("login", ComparisonOperator.LIKE, login);
        Filter codeFilter = new Filter("code", ComparisonOperator.LIKE, code);
        return this.getOneByFilter(loginFilter.or(codeFilter));
    }

    public boolean existsByCode(@NotNull String code) {
        Filter filter = new Filter("code", ComparisonOperator.LIKE, code);
        return this.isExists(filter);
    }

    public boolean existsByLogin(@NotNull String login) {
        Filter filter = new Filter("login", ComparisonOperator.LIKE, login);
        return this.isExists(filter);
    }

    public boolean existsByLoginOrCode(@NotNull String login, @NotNull String code) {
        Filter loginFilter = new Filter("login", ComparisonOperator.LIKE, login);
        Filter codeFilter = new Filter("code", ComparisonOperator.LIKE, code);
        return this.isExists(loginFilter.or(codeFilter));
    }

    public Collection<T> getByRole(@NotNull String role) {
        Filter filter = new Filter("role", ComparisonOperator.LIKE, role);
        return this.getByFilter(filter);
    }
}
