//package com.example.demo.security;
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Objects;
//import java.util.function.Function;
//
//@Service
//@AllArgsConstructor
//public class AccountFilterService {
//    private final String signInKey="7A25432A462D4A614E645266556A586E3272357538782F413F4428472B4B6250";
//
//    private SecretKey getSignInKey(){
//        byte[] key= Base64.getDecoder().decode(signInKey);
//        return Keys.hmacShaKeyFor(key);
//    }
//    private Claims getClaims(String token){
//        return Jwts
////                .parserBuilder()
//                .parser()
//                .setSigningKey(getSignInKey())
////                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//    private <T> T extractClaim(String token, Function<Claims, T> getClaimValue){
//        final Claims claims=getClaims(token);
//        return getClaimValue.apply(claims);
//    }
//
//    public String getUsername(String token){
//        return extractClaim(token,Claims::getSubject);
//    }
//
//    private String generateToken(HashMap<String, Objects> claims, Authentication account){
//        UserDetails userDetails = (UserDetails) account.getPrincipal();
//        return Jwts
//                .builder()
//                .setClaims(new HashMap<>())
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis()+1000*2*60*60))
//                .signWith(getSignInKey())
//                .compact();
//    }
//
//    public String generateToken(Authentication account){
//        return generateToken(new HashMap<>(),account);
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails){
//        return !isTokenExpired(token)&&
//                isTheSameUser(token,userDetails)&&
//                isAccountEnable(userDetails);
//    }
//
//    public boolean isTokenExpired(String token){
//        Date expiredDate=extractClaim(token,Claims::getExpiration);
//        return new Date().after(expiredDate);
//    }
//
//    public boolean isTheSameUser(String token ,UserDetails userDetails){
//        return userDetails.getUsername().equals(getUsername(token));
//    }
//
//    public boolean isAccountEnable(UserDetails userDetails){
//        return userDetails.isEnabled();
//    }
//
//    public String refreshToken(String token) {
//        // Kiểm tra tính hợp lệ của token trước khi làm mới
//        if (isTokenValid(token, null)) {
//            // Tạo lại token mới dựa trên thông tin trong token cũ
//            Claims claims = getClaims(token);
//            HashMap<String, Object> newClaims = new HashMap<>(claims);
//
//            // Set thời gian hết hạn mới cho token mới (ví dụ: 2 giờ sau khi làm mới)
//            long expirationTime = System.currentTimeMillis() + 1000 * 24 * 60 * 60;
//            newClaims.put("exp", new Date(expirationTime));
//
//            // Tạo token mới
//            return Jwts.builder().setClaims(newClaims).setIssuedAt(new Date()).setExpiration(new Date(expirationTime))
//                    .signWith(getSignInKey()).compact();
//        } else {
//            // Token không hợp lệ hoặc đã hết hạn, không làm mới
//            return null;
//        }
//    }
//}
