package database.relationships;

import database.domains.Domain;
import database.domains.Person;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = CreatedByRelationship.TYPE)
public class CreatedByRelationship extends Relationship {
    public static final String TYPE = "CREATED_BY";

    @StartNode
    private Domain startNode;

    @EndNode
    private Person createdBy;

    public Domain getStartNode() {
        return this.startNode;
    }
    public void setStartNode(Domain startNode) {
        this.startNode = startNode;
    }

    public Person getCreatedBy() {
        return this.createdBy;
    }
    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }
}
