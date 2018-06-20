package database.domains;

import com.google.gson.annotations.Expose;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Properties;

import java.lang.reflect.Field;
import java.util.*;

@NodeEntity
public class Domain {
    @Id @GeneratedValue
    @Expose private Long id;

    private boolean deleted;
    @Expose private boolean editable;
    @Expose private boolean deletable;
    @Expose private boolean readable;
    @Properties
    @Expose private Map<String, Object> properties = new HashMap<>();

    public Long getId() { return this.id; }
    public void setId(long id) {
        this.id = id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEditable() {
        return this.editable;
    }
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isDeletable() {
        return this.deletable;
    }
    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    public boolean isReadable() {
        return this.readable;
    }
    public void setReadable(boolean readable) {
        this.readable = readable;
    }

    public boolean isDeleted() {
        return this.deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Map<String, Object> getProperties() { return this.properties; }
    public void setProperty(String key, Object value) { this.getProperties().put(key, value); }
    public Object getProperty(String key) { return this.getProperties().get(key); }
    public void deleteProperty(String key) { this.getProperties().remove(key); }
    public boolean hasProperty(String key) { return this.getProperties().containsKey(key); }
    public boolean hasPropertyObject(Object object) { return this.getProperties().containsValue(object); }

    public boolean hasAttribute(String attribute) {
        List<Field> fields = new ArrayList<>();
        Domain.getAllFields(fields, this.getClass());
        return fields.stream().anyMatch(item -> item.getName().equals(attribute));
    }
    public Class<?> getAttributeType(String attribute) {
        List<Field> fields = new ArrayList<>();
        Domain.getAllFields(fields, this.getClass());
        Field field = fields.stream().filter(item -> item.getName().equals(attribute)).findFirst().orElse(null);
        return field != null ? field.getType() : null;
    }
    public void setAttribute(String attribute, Object value) {
        try {
            Field field = this.getClass().getField(attribute);
            field.setAccessible(true);
            field.set(this, value);
        }
        catch (NoSuchFieldException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public void setAttributes(Map<String, Object> attributes) {
        attributes.forEach((key, value) -> {
            if (this.hasAttribute(key))
                this.setAttribute(key, value);
        });
    }

    public boolean availableCreate(String sessionId) {
        return false;
    }
    public boolean availableRead(String sessionId) {
        return false;
    }
    public boolean availableEdit(String sessionId) {
        return false;
    }
    public boolean availableDelete(String sessionId) {
        return false;
    }

    public void setAvailability(String sessionId) {
        this.setReadable(this.availableRead(sessionId));
        this.setEditable(this.availableEdit(sessionId));
        this.setDeletable(this.availableDelete(sessionId));
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        Domain.getAllFields(result, clazz);
        return result;
    }
    private static void getAllFields(List<Field> oldFields, Class<?> clazz) {
        if (!Domain.class.isAssignableFrom(clazz)) return;
        oldFields.addAll(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != null && Domain.class.isAssignableFrom(clazz.getSuperclass()))
            Domain.getAllFields(oldFields, clazz.getSuperclass());
    }

    public boolean equals(Domain domain) {
        return this.getId() != null && domain.getId() != null && this.getId().equals(domain.getId());
    }

    public Domain() {}
}
