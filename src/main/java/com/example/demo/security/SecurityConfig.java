package com.example.demo.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    @Autowired
    private CustomerUserDetailService userDetailService;
    @Autowired
    JwtAuthEntryPoint jwtAuthEntryPoint;
    @Autowired
    AccessDeniedHandler accessDeniedHandler;

    private static final String[] NHANVIEN_LINK = {
            "/hoa-don/ban-hang"
    };
    private static final String[] QUANLY_LINK = {
            "/khachhang"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
                .csrf(csrf -> csrf.disable())
               .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint)
                       .accessDeniedHandler(accessDeniedHandler)
                )
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
               .authorizeHttpRequests(auth -> auth
                        .requestMatchers(QUANLY_LINK).hasAuthority("ROLE_QUANLY")
                        .requestMatchers("/api/khachhang").hasAuthority("ROLE_QUANLY")
                        .requestMatchers("/hoa-don/ban-hang").authenticated()
                        .anyRequest().permitAll()
                )
//               .formLogin(form -> form
//                       .loginPage("/admin/account")
//                       .successHandler(successHandler)
//                       .permitAll()
//               )
               .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).build();

    }


//    @Bean
//    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable())
////                .exceptionHandling((exception) -> exception.authenticationEntryPoint(authEntryPoint))
//                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeRequests()
//                .requestMatchers("/khachhang").authenticated()
//                .anyRequest().permitAll()
//                .and()
//                .formLogin(form -> form
//                        .loginPage("/account")
//                        .permitAll()
//                )
//                .httpBasic(Customizer.withDefaults());
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }

//    @Bean
//    public AuthenticationProvider getProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder());
//        provider.setUserDetailsService(userDetailService);
//        return provider;
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
