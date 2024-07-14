package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
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
    //    private static final Key key = new SecretKeySpec(Base64.getDecoder().decode(SecurityConstants.JWT_SECRET), SignatureAlgorithm.HS512.getJcaName());
    private final String signInKey = "7A25432A462D4A614E645266556A586E3272357538782F413F4428472B4B6250";
    public static final long JWT_EXPIRATION = 3600 * 1000;

    private SecretKey getSignInKey() {
        byte[] key = Base64.getDecoder().decode(signInKey);
        return Keys.hmacShaKeyFor(key);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(signInKey)
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
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.JWT_SECRET)
                .compact();
        System.out.println("New token:");
        System.out.println(token);
        return token;
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.JWT_SECRET)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException("JWT đã hết hạn hoặc không chính xác");
        }
    }


    private <T> T extractClaim(String token, Function<Claims, T> getClaimValue) {
        final Claims claims = getClaims(token);
        return getClaimValue.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expiredDate = extractClaim(token, Claims::getExpiration);
        return new Date().after(expiredDate);
    }

    public boolean isTheSameUser(String token, UserDetails userDetails) {
        return userDetails.getUsername().equals(getUsernameFromJWT(token));
    }

    public boolean isAccountEnable(UserDetails userDetails) {
        return userDetails.isEnabled();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        return !isTokenExpired(token) &&
                isTheSameUser(token, userDetails) &&
                isAccountEnable(userDetails);
    }
}
