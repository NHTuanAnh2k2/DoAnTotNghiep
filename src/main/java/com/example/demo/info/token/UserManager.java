package com.example.demo.info.token;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserManager {
    public final Map<Integer, String> loggedInUsers = new ConcurrentHashMap<>();

    public final Map<Integer, String> userAgents = new HashMap<>();

    public void storeUserAgent(Integer userId, String userAgent) {
        userAgents.put(userId, userAgent);
    }

    public String getUserAgent(Integer userId) {
        return userAgents.get(userId);
    }

    public void remove(Integer userId, String userAgent) {
        userAgents.remove(userId, userAgent);
    }

    public void addUser(Integer userId, String token) {
        loggedInUsers.put(userId, token);
    }

    public void removeUser(Integer userId, String token) {
        loggedInUsers.remove(userId, token);
    }

    public String getToken(Integer userId) {
        return loggedInUsers.get(userId);
    }

    public boolean isUserLoggedIn(Integer userId, String userAgent) {
        if (userAgents.containsKey(userId)) {
            String token = loggedInUsers.get(userId);
            if (token != null && token.equals(userAgent)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public Map<Integer, String> getLoggedInUsers() {
        return loggedInUsers;
    }

    public void logoutUser(Integer userId, String token) {
        if (loggedInUsers.containsKey(userId)) {
            loggedInUsers.remove(userId, token);
        }
    }
}
