package services;

import com.sun.istack.internal.NotNull;
import database.domains.Person;

import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationServiceSingleton {
    private final static AuthenticationServiceSingleton authenticationServiceSingleton = new AuthenticationServiceSingleton();
    private final ConcurrentHashMap<String, Person> authenticatedPersons = new ConcurrentHashMap<>();

    public static AuthenticationServiceSingleton getInstance() {
        return AuthenticationServiceSingleton.authenticationServiceSingleton;
    }

    public boolean isAuthenticated(@NotNull String sessionId) {
        return !sessionId.isEmpty() && this.authenticatedPersons.containsKey(sessionId);
    }

    public Person getCurrentPerson(@NotNull String sessionId) {
        return this.isAuthenticated(sessionId)
                ? this.authenticatedPersons.get(sessionId)
                : null;
    }

    boolean signInPerson(@NotNull String sessionId, @NotNull Person person) {
        if (sessionId.trim().isEmpty()) return false;

        this.authenticatedPersons.put(sessionId, person);
        return true;
    }

    boolean logoutPerson(@NotNull String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) return false;
        if (this.authenticatedPersons.containsKey(sessionId)) {
            this.authenticatedPersons.remove(sessionId);
            return true;
        }
        return false;
    }
}
