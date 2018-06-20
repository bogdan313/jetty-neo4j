package database.services;

import com.sun.istack.internal.NotNull;
import database.DBServiceSingleton;
import database.domains.Domain;
import helpers.Constants;
import helpers.DateHelper;
import helpers.ParseParametersHelper;
import org.neo4j.ogm.cypher.ComparisonOperator;
import org.neo4j.ogm.cypher.Filter;
import org.neo4j.ogm.cypher.Filters;
import org.neo4j.ogm.cypher.query.Pagination;
import org.neo4j.ogm.cypher.query.SortOrder;
import org.neo4j.ogm.session.Session;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Database service for operations with objects (vertexes and edges) of T class
 * @param <T> T is child from Domain. All operations will be with T class objects
 */
public class BaseService<T extends Domain> {
    /**
     * This needs to know which objects are used
     */
    private final Class<T> clazz;

    /**
     * This should help when we need to connect two or more services
     */
    private Session session;

    /**
     * By the {@link services.AuthenticationServiceSingleton} we store users with their SessionId
     * This parameter refers to the Person and his (her) rules
     */
    private String sessionId;

    BaseService(Class<T> clazz) {
        this.session = DBServiceSingleton.getInstance().getSession();
        this.clazz = clazz;
    }

    public void setSession(@NotNull Session session) {
        this.session = session;
    }
    public Session getSession() {
        return this.session;
    }

    public void setSessionId(@NotNull String sessionId) {
        this.sessionId = sessionId;
    }
    public String getSessionId() {
        return this.sessionId;
    }

    /**
     * This function is for saving objects (new or existing) to database
     * @param object of T (Domain) class which should be stored
     * @return is operation success
     */
    public boolean save(@NotNull T object) {
        if (object.getId() != null && object.availableEdit(this.getSessionId()) ||
                object.getId() == null && object.availableCreate(this.getSessionId())) {
            this.getSession().save(object);
            return true;
        }
        return false;
    }

    /**
     * Get all records with T class (label on Neo4j vertex)
     * @return collection of records
     */
    public Collection<T> getAll() {
        return this.getAll(new SortOrder().add("id"));
    }

    /**
     * Parse parameters from requestParameters, make filters, sort conditions, page number, objects to page and neighbours
     * Then make request to database and return collection of appropriate records
     * @param requestParameters is {@link HttpServletRequest#getParameterMap()}
     * @return collection of records
     */
    @SuppressWarnings("unchecked")
    public Collection<T> getAll(@NotNull Map<String, String[]> requestParameters) {
        Map<String, Object> parsedParameters = ParseParametersHelper.parse(requestParameters);
        Filters filters = new Filters();
        SortOrder sortOrder = new SortOrder();
        int depth = 1;
        int pageNumber = 1;
        int pageSize = Constants.ELEMENTS_PER_PAGE;

        if (parsedParameters.containsKey("sort") && parsedParameters.get("sort") instanceof Map) {
            Map<String, ? extends Map> sortParameters = (Map<String, ? extends Map>) parsedParameters.get("sort");

            sortParameters.values().forEach(item -> {
                if (item.containsKey("parameter") && item.containsKey("direction")) {
                    String sortParameter = item.getOrDefault("parameter", "id").toString().trim();
                    String direction = item.getOrDefault("direction", SortOrder.Direction.DESC).toString().toUpperCase().trim();
                    SortOrder.Direction sortDirection = SortOrder.Direction.DESC;
                    if (direction.isEmpty() || direction.equals("ASC"))
                        sortDirection = SortOrder.Direction.ASC;
                    sortOrder.add(sortDirection, sortParameter);
                }
            });
        }

        if (parsedParameters.containsKey("filter") && parsedParameters.get("filter") instanceof Map) {
            Map<String, ? extends Map> filterParameters = (Map<String, ? extends Map>) parsedParameters.get("filter");
            try {
                T elt = this.clazz.newInstance();
                filterParameters.values().forEach(item -> {
                    String filterParameter = item.getOrDefault("parameter", "").toString().trim();
                    String comparisonOperator = item.getOrDefault("operator", ComparisonOperator.EQUALS).toString().trim();
                    String value = item.getOrDefault("value", "").toString().trim();

                    if (elt.hasAttribute(filterParameter)) {
                        Class<?> type = elt.getAttributeType(filterParameter);
                        if (type != null && (type.isPrimitive() || type == String.class)) {
                            Object castedValue = null;
                            try {
                                if (type == Double.TYPE) {
                                    castedValue = Double.parseDouble(value);
                                }
                                else if (type == Integer.TYPE) {
                                    castedValue = Integer.parseInt(value);
                                }
                                else if (type == Float.TYPE) {
                                    castedValue = Float.parseFloat(value);
                                }
                                else if (type == Long.TYPE) {
                                    if (filterParameter.equals("datetime") || filterParameter.equals("createdDate") ||
                                            filterParameter.equals("updatedDate")) {
                                        try {
                                            castedValue = DateHelper.parse(value);
                                        }
                                        catch (ParseException e) {
                                            castedValue = 0L;
                                        }
                                    }
                                    else castedValue = Long.parseLong(value);
                                }
                                else if (type == Boolean.TYPE) {
                                    castedValue = Boolean.parseBoolean(value);
                                }
                                else {
                                    castedValue = value;
                                }
                                filters.or(new Filter(filterParameter, ComparisonOperator.valueOf(comparisonOperator), castedValue));
                            }
                            catch (NumberFormatException e) {}
                        }
                    }
                });
            }
            catch (InstantiationException|IllegalAccessException e) {}
        }

        try {
            pageNumber = Integer.parseInt(parsedParameters.getOrDefault("pageNumber", "1").toString().trim());
            if (pageNumber < -1) pageNumber = -1;

            pageSize = Integer.parseInt(parsedParameters.getOrDefault("pageSize", Constants.ELEMENTS_PER_PAGE).toString().trim());
            if (pageSize < -1) pageSize = Constants.ELEMENTS_PER_PAGE;

            depth = Integer.parseInt(parsedParameters.getOrDefault("depth", 1).toString().trim());
            if (depth < -1) depth = -1;
        }
        catch (NumberFormatException e) {}

        return this.getAll(filters, sortOrder, pageNumber, pageSize, depth);
    }

    /**
     * Get all records only by page
     * @param page
     * @return collection of records
     */
    public Collection<T> getAll(int page) {
        return this.getAll(new SortOrder().add("id"), page, 1);
    }

    /**
     * Get all records by page and with depth (level of loaded relations)
     * @param page
     * @param depth means distance to neighbours
     * @return collection of records
     */
    public Collection<T> getAll(int page, int depth) {
        return this.getAll(new SortOrder().add("id"), page, depth);
    }

    /**
     * Get all sorted records
     * @param order
     * @return collection of records
     */
    public Collection<T> getAll(@NotNull SortOrder order) {
        return this.getAll(order, -1, 1);
    }

    /**
     * Get all ordered records from page
     * @param order
     * @param page
     * @return collection of records
     */
    public Collection<T> getAll(@NotNull SortOrder order, int page) {
        return this.getAll(order, page, 1);
    }

    /**
     * Get all ordered records from page and with neighbours (depth)
     * @param order
     * @param page
     * @param depth
     * @return collection of records
     */
    public Collection<T> getAll(@NotNull SortOrder order, int page, int depth) {
        return this.getAll(new Filters(), order, page, depth);
    }

    /**
     * Get all ordered records with filter condition from page and with neighbours (depth)
     * @param filter
     * @param sortOrder
     * @param page
     * @param depth
     * @return collection of records
     */
    public Collection<T> getAll(@NotNull Filter filter, @NotNull SortOrder sortOrder, int page, int depth) {
        return this.getAll(new Filters().add(filter), sortOrder, page, depth);
    }

    /**
     * Get all ordered records with filters condition from page and with neighbours (depth)
     * @param filters
     * @param sortOrder
     * @param page
     * @param depth
     * @return collection of records
     */
    public Collection<T> getAll(@NotNull Filters filters, @NotNull SortOrder sortOrder, int page, int depth) {
        return this.getAll(filters, sortOrder, page, Constants.ELEMENTS_PER_PAGE, depth);
    }

    /**
     * Get all records with condition of all available parameters: filtered, sorted, from page and count of records, with neighbours
     * @param filters
     * @param sortOrder
     * @param pageNumber
     * @param pageSize
     * @param depth
     * @return collection of records
     */
    public Collection<T> getAll(@NotNull Filters filters, @NotNull SortOrder sortOrder, int pageNumber, int pageSize, int depth) {
        Collection<T> result = null;
        if (pageNumber == -1) result = this.getSession().loadAll(this.clazz, filters, sortOrder, depth);
        else result = this.getSession().loadAll(this.clazz, filters, sortOrder, new Pagination(pageNumber - 1, pageSize), depth);

        result.removeIf(item -> !item.availableRead(this.getSessionId()));
        result.forEach(item -> item.setAvailability(this.getSessionId()));

        return result;
    }

    /**
     * Get one record by ID
     * @param id
     * @return T object
     */
    public T getById(long id) {
        return this.getById(id, 1);
    }

    /**
     * Get one record with neighbours (depth)
     * @param id
     * @param depth
     * @return T object
     */
    public T getById(long id, int depth) {
        T object = this.getSession().load(this.clazz, id, depth);
        object.setAvailability(this.getSessionId());
        return object.availableRead(this.getSessionId()) ? object : null;
    }

    /**
     * Get one record with filter condition
     * @param filter
     * @return T object
     */
    public T getOneByFilter(@NotNull Filter filter) {
        return this.getOneByFilter(new Filters().add(filter));
    }

    /**
     * Get one record with filters condition
     * @param filters
     * @return T object
     */
    public T getOneByFilter(@NotNull Filters filters) {
        Collection<T> collection = this.getSession().loadAll(this.clazz, filters);
        Optional<T> object = collection.stream().findFirst();
        if (object.isPresent()) {
            object.get().setAvailability(this.getSessionId());
            return object.get().availableRead(this.getSessionId()) ? object.get() : null;
        }
        return null;
    }

    /**
     * Get all records with filter condition
     * @param filter
     * @return collection of records
     */
    public Collection<T> getByFilter(@NotNull Filter filter) {
        return this.getByFilter(new Filters().add(filter));
    }

    /**
     * Get all records with filters condition
     * @param filters
     * @return collection of records
     */
    public Collection<T> getByFilter(@NotNull Filters filters) {
        return this.getByFilter(filters, 1);
    }

    /**
     * Get all records with filters condition and with neighbours
     * @param filters
     * @param depth
     * @return collection of records
     */
    public Collection<T> getByFilter(@NotNull Filters filters, int depth) {
        Collection<T> result = this.getSession().loadAll(this.clazz, filters, depth);
        result.removeIf(item -> item.availableRead(this.getSessionId()));
        result.forEach(item -> item.setAvailability(this.getSessionId()));
        return result;
    }

    /**
     * Check is record with this parameter exists
     * @param parameter
     * @param value
     * @return is some records exists
     */
    public boolean isExists(@NotNull String parameter, @NotNull Object value) {
        return this.isExists(new Filter(parameter, ComparisonOperator.EQUALS, value));
    }

    /**
     * Check is record with this filter condition exists
     * @param filter
     * @return is some records exists
     */
    public boolean isExists(@NotNull Filter filter) {
        return this.isExists(new Filters().add(filter));
    }

    /**
     * Check is record with this filters condition exists
     * @param filters
     * @return is some records exists
     */
    public boolean isExists(@NotNull Filters filters) {
        return this.getSession().count(this.clazz, filters) > 0;
    }

    /**
     * Number of records with T label
     * @return number of records
     */
    public long countAll() {
        return this.getSession().countEntitiesOfType(this.clazz);
    }

    /**
     * Count how many records fits parameter condition
     * @param parameter
     * @param value
     * @return number of records
     */
    public long count(@NotNull String parameter, @NotNull Object value) {
        return this.count(new Filter(parameter, ComparisonOperator.EQUALS, value));
    }

    /**
     * Count how many records fits filter
     * @param filter
     * @return number of records
     */
    public long count(@NotNull Filter filter) {
        return this.count(new Filters().add(filter));
    }

    /**
     * Count how many records fits filters
     * @param filters
     * @return number of records
     */
    public long count(@NotNull Filters filters) {
        return this.getSession().count(this.clazz, filters);
    }

    /**
     * Delete object from database
     * @param object
     * @return is available to delete object and if is deleted
     */
    public boolean delete(@NotNull T object) {
        if (object.availableDelete(this.getSessionId()) && !object.isDeleted()) {
            this.getSession().delete(object);
            return true;
        }
        return false;
    }

    /**
     * Find object by ID and delete it
     * @param id
     * @return is available to delete object and if is deleted
     */
    public boolean deleteById(long id) {
        T object = this.getById(id);
        if (object != null && object.availableDelete(this.getSessionId()) && !object.isDeleted()) {
            this.delete(object);
            return true;
        }
        return false;
    }

    /**
     * Delete all object with T label from database
     */
    public void deleteAll() {
        this.getSession().deleteAll(this.clazz);
    }

    /**
     * Clear cached records from session
     */
    public void clearSession() {
        this.getSession().clear();
    }
}
