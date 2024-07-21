package com.example.demo.security;

import java.util.Base64;

public class SecurityConstants {
//    public static final long JWT_EXPIRATION = 3600 * 1000;
    public static final long JWT_EXPIRATION = 30000;
    public static final String JWT_SECRET;
    static {
        byte[] secretKey = new byte[64];
        new java.security.SecureRandom().nextBytes(secretKey);
        JWT_SECRET = Base64.getEncoder().encodeToString(secretKey);
    }

}
