package database.relationships;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;

public class Relationship {
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
