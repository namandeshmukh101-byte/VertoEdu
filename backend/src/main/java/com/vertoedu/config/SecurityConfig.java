package com.vertoedu.config;

import com.vertoedu.security.JwtAuthenticationFilter;
import com.vertoedu.security.OAuth2LoginFailureHandler;
import com.vertoedu.security.OAuth2LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vertoedu.dto.ApiResponse;

/**
 * SecurityConfig — Full Spring Security configuration with Google OAuth2 + JWT.
 * Replaces the temporary permit-all config from Prompt 1.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Enable CORS with existing CorsConfig bean
            .cors(cors -> cors.configurationSource(corsConfigurationSource))

            // Disable CSRF (using JWT tokens for auth)
            .csrf(csrf -> csrf.disable())

            // Stateless session — JWT handles session
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                    "/auth/**",
                    "/oauth2/**",
                    "/login/**",
                    "/error",
                    "/actuator/health"
                ).permitAll()

                // Role-based endpoints (will be expanded in future prompts)
                .requestMatchers("/admin/**", "/ocr/**", "/ai/**", "/approval/**").hasRole("ADMIN")
                .requestMatchers("/teacher/**").hasRole("TEACHER")
                .requestMatchers("/parent/**").hasRole("PARENT")

                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            // Google OAuth2 login configuration
            .oauth2Login(oauth2 -> oauth2
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2LoginFailureHandler)
            )

            // Exception handling for unauthenticated requests
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    
                    ApiResponse<Void> apiResponse = ApiResponse.error("Authentication required");
                    new ObjectMapper().writeValue(response.getOutputStream(), apiResponse);
                })
            )

            // Add JWT filter before Spring's auth filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
