package database.domains;

import com.google.gson.annotations.Expose;
import com.sun.istack.internal.NotNull;
import database.relationships.CreatedByRelationship;
import database.relationships.UpdatedByRelationship;
import helpers.DateHelper;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import services.AuthenticationServiceSingleton;

import java.text.ParseException;

@NodeEntity
public class DomainWithAuthor extends Domain {
    @Relationship(type = CreatedByRelationship.TYPE, direction = Relationship.OUTGOING)
    @Expose private Person createdBy;
    @Relationship(type = UpdatedByRelationship.TYPE, direction = Relationship.OUTGOING)
    @Expose private Person updatedBy;
    @Expose private long createdDate;
    @Expose private long updatedDate;

    public Person getCreatedBy() {
        return this.createdBy;
    }
    public void setCreatedBy(@NotNull Person createdBy) { this.createdBy = createdBy; }

    public Person getUpdatedBy() {
        return this.updatedBy;
    }
    public void setUpdatedBy(@NotNull Person updatedBy) {
        this.updatedBy = updatedBy;
    }

    public long getCreatedDate() {
        return this.createdDate;
    }
    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }
    public void setCreatedDate(@NotNull String createdDate) throws ParseException {
        this.createdDate = DateHelper.parse(createdDate);
    }
    public void setCreatedDate() {
        this.createdDate = System.currentTimeMillis();
    }
    public void setCreatedDateOrCurrent(String createdDate) {
        try {
            this.setCreatedDate(createdDate);
        }
        catch (ParseException e) {
            this.setCreatedDate();
        }
    }

    public long getUpdatedDate() {
        return this.updatedDate;
    }
    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }
    public void setUpdatedDate(@NotNull String updatedDate) throws ParseException {
        this.setUpdatedDate(DateHelper.parse(updatedDate));
    }
    public void setUpdatedDate() {
        this.updatedDate = System.currentTimeMillis();
    }
    public void setUpdatedDateOrCurrent(String updatedDate) {
        try {
            this.setUpdatedDate(updatedDate);
        }
        catch (ParseException e) {
            this.setUpdatedDate();
        }
    }

    @Override
    public boolean availableCreate(String sessionId) {
        return true;
    }

    @Override
    public boolean availableRead(String sessionId) {
        return true;
    }

    @Override
    public boolean availableEdit(@NotNull String sessionId) {
        Person currentPerson = AuthenticationServiceSingleton.getInstance().getCurrentPerson(sessionId);
        if (currentPerson == null) return false;
        return this.getCreatedBy() != null && this.getCreatedBy().equals(currentPerson);
    }

    @Override
    public boolean availableDelete(@NotNull String sessionId) {
        Person currentPerson = AuthenticationServiceSingleton.getInstance().getCurrentPerson(sessionId);
        if (currentPerson == null) return false;
        return this.getCreatedBy() != null && this.getCreatedBy().equals(currentPerson);
    }
}
