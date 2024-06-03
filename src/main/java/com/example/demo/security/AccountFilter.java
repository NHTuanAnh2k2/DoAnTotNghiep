//package com.example.demo.security;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import lombok.AllArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.WebAuthenticationDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@AllArgsConstructor
//@Component
//public class AccountFilter extends OncePerRequestFilter {
//
//    private final AccountFilterService accountFilterService;
//    private final UserDetailsService userDetailsService;
//
//    @Override
//    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws jakarta.servlet.ServletException, IOException {
//        final String header=request.getHeader("Authorization");
//        if(header==null||!header.startsWith("Bearer ")){
//            filterChain.doFilter(request,response);
//            return;
//        }
//        final String token=header.substring(7);
//        String username = null;
//        try {
//            username= accountFilterService.getUsername(token);
//        } catch (IllegalArgumentException e) {
//            filterChain.doFilter(request,response);
//            return;
//        } catch (ExpiredJwtException e) {
//            filterChain.doFilter(request,response);
//            return;
//        }
//
//        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){
//            UserDetails account=userDetailsService.loadUserByUsername(username);
//            if(accountFilterService.isTokenValid(token,account)){
//                UsernamePasswordAuthenticationToken authToken
//                        =new UsernamePasswordAuthenticationToken(account,null,account.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
//        filterChain.doFilter(request,response);
//    }
//}
