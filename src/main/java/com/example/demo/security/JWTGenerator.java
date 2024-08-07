package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
@AllArgsConstructor
public class JWTGenerator {

    @Autowired
    HttpSession session;
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);//    private final Key signInKey = new SecretKeySpec(Base64.getDecoder().decode(SecurityConstants.JWT_SECRET), SignatureAlgorithm.HS512.getJcaName());
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        System.out.println("New token:");
        System.out.println(token);
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            session.invalidate();
            return false;
        }
    }


    private <T> T extractClaim(String token, Function<Claims, T> getClaimValue) {
        final Claims claims = getClaims(token);
        return getClaimValue.apply(claims);
    }
//
//    public boolean isTokenExpired(String token) {
//        Date expiredDate = extractClaim(token, Claims::getExpiration);
//        return new Date().after(expiredDate);
//    }
//
//    public boolean isTheSameUser(String token, UserDetails userDetails) {
//        return userDetails.getUsername().equals(getUsernameFromJWT(token));
//    }
//
//    public boolean isAccountEnable(UserDetails userDetails) {
//        return userDetails.isEnabled();
//    }
//
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        return !isTokenExpired(token) &&
//                isTheSameUser(token, userDetails) &&
//                isAccountEnable(userDetails);
//    }
}
