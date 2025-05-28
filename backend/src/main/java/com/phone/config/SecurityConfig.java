package com.phone.config;

import com.phone.service.TokenBlocklistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.phone.security.JsonUsernamePasswordAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.phone.security.JwtUtil;
import com.phone.security.JwtAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.http.HttpMethod;
import com.phone.security.ApiAuthenticationEntryPoint;
import com.phone.security.ApiAccessDeniedHandler;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ApiAuthenticationEntryPoint apiAuthenticationEntryPoint;

    @Autowired
    private ApiAccessDeniedHandler apiAccessDeniedHandler;

    @Autowired
    private TokenBlocklistService tokenBlocklistService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JsonUsernamePasswordAuthenticationFilter jsonFilter = new JsonUsernamePasswordAuthenticationFilter(jwtUtil);
        jsonFilter.setAuthenticationManager(authenticationManager);
        jsonFilter.setFilterProcessesUrl("/api/auth/login");
        ObjectMapper objectMapper = new ObjectMapper();

        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(apiAuthenticationEntryPoint)
                .accessDeniedHandler(apiAccessDeniedHandler)
            .and()
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                
                // ASSIGNER & ADMIN: can view (GET) phones, simcards, users, assignments
                .requestMatchers(HttpMethod.GET, "/api/phones/**").hasAnyRole("ASSIGNER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/simcards/**").hasAnyRole("ASSIGNER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ASSIGNER", "ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/assignments/**").hasAnyRole("ASSIGNER", "ADMIN")
                
                // ADMIN only: can create, update, delete phones, simcards, users
                .requestMatchers(HttpMethod.POST, "/api/phones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/phones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/phones/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/simcards/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/simcards/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/simcards/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                .requestMatchers("/api/system-users/**").hasRole("ADMIN")
                
                // ASSIGNER & ADMIN: can manage assignments
                .requestMatchers(HttpMethod.POST, "/api/assignments/**").hasAnyRole("ASSIGNER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/assignments/**").hasAnyRole("ASSIGNER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/assignments/**").hasAnyRole("ASSIGNER", "ADMIN")
                
                // All other requests need authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, JsonUsernamePasswordAuthenticationFilter.class)
            .addFilterAt(jsonFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
            .formLogin()
                .loginProcessingUrl("/api/auth/login")
                .successHandler((request, response, authentication) -> {
                    response.setStatus(200);
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                });
        
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
} 