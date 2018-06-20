package database.relationships;

import database.domains.Domain;
import database.domains.Person;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = UpdatedByRelationship.TYPE)
public class UpdatedByRelationship extends Relationship {
    public static final String TYPE = "UPDATED_BY";

    @StartNode
    private Domain startNode;

    @EndNode
    private Person updatedBy;

    public Domain getStartNode() {
        return this.startNode;
    }
    public void setStartNode(Domain startNode) {
        this.startNode = startNode;
    }

    public Person getUpdatedBy() {
        return this.updatedBy;
    }
    public void setUpdatedBy(Person updatedBy) {
        this.updatedBy = updatedBy;
    }
}
