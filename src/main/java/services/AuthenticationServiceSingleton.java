package services;

import database.domains.Person;

import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationServiceSingleton {
    private final static AuthenticationServiceSingleton authenticationServiceSingleton = new AuthenticationServiceSingleton();
    private final ConcurrentHashMap<String, Person> authenticatedPersons = new ConcurrentHashMap<>();

    public static AuthenticationServiceSingleton getInstance() {
        return AuthenticationServiceSingleton.authenticationServiceSingleton;
    }

    public boolean isAuthenticated(String sessionId) {
        return sessionId != null && !sessionId.isEmpty() &&
                this.authenticatedPersons.containsKey(sessionId);
    }

    public Person getCurrentPerson(String sessionId) {
        return this.isAuthenticated(sessionId)
                ? this.authenticatedPersons.get(sessionId)
                : null;
    }

    boolean signInPerson(String sessionId, Person person) {
        if (sessionId == null || sessionId.trim().isEmpty() || person == null)
            return false;

        this.authenticatedPersons.put(sessionId, person);
        return true;
    }

    boolean logoutPerson(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) return false;
        if (this.authenticatedPersons.containsKey(sessionId)) {
            this.authenticatedPersons.remove(sessionId);
            return true;
        }
        return false;
    }
}
