package database.domains;

import com.google.gson.annotations.Expose;
import com.sun.istack.internal.NotNull;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import services.AuthenticationServiceSingleton;

@NodeEntity
public class Person extends Domain {
    public enum ROLE {
        ADMINISTRATOR("Administrator"),
        MODERATOR("Moderator");

        private final String value;

        ROLE(String role) {
            this.value = role;
        }

        public String getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }

    @Expose private String fullName;
    @Index(unique = true)
    @Expose private String login;
    private String password;
    @Expose private String role;
    @Expose private String photo;

    public String getFullName() {
        return this.fullName;
    }
    public void setFullName(@NotNull String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return this.login;
    }
    public void setLogin(@NotNull String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public String getRole() {
        return  this.role;
    }
    public void setRole(@NotNull String role) {
        this.role = role;
    }

    public String getPhoto() {
        return this.photo;
    }
    public void setPhoto(@NotNull String photo) {
        this.photo = photo;
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
        return currentPerson != null && currentPerson.equals(this);
    }

    @Override
    public boolean availableDelete(@NotNull String sessionId) {
        Person currentPerson = AuthenticationServiceSingleton.getInstance().getCurrentPerson(sessionId);
        return currentPerson != null && currentPerson.equals(this);
    }
}
