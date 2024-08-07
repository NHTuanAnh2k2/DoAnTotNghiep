package com.example.demo.info.token;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserManager {
    public final Map<String, String> loggedInUsers = new ConcurrentHashMap<>();

    public final Map<String, String> userAgents = new HashMap<>();

    public void storeUserAgent(String username, String userAgent) {
        userAgents.put(username, userAgent);
    }

    public String getUserAgent(String username) {
        return userAgents.get(username);
    }

    public void remove(String username, String userAgent) {
        userAgents.remove(username, userAgent);
    }

    public void addUser(String username, String token) {
        loggedInUsers.put(username, token);
    }

    public void removeUser(String username, String token) {
        loggedInUsers.remove(username, token);
    }

    public String getToken(String username) {
        return loggedInUsers.get(username);
    }

    public boolean isUserLoggedIn(String username, String userAgent) {
        if (userAgents.containsKey(username)) {
            String token = loggedInUsers.get(username);
            if (token != null && token.equals(userAgent)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public Map<String, String> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void logoutUser(String username, String token) {
        if (loggedInUsers.containsKey(username)) {
            loggedInUsers.remove(username, token);
        }
    }
}
