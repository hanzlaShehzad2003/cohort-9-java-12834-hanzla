package com.tenpearls.contactmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        /*
         * CSRF configuration
         *
         * The token is stored in the XSRF-TOKEN cookie.
         * JavaScript/frontend sends it back using X-XSRF-TOKEN.
         */
        CookieCsrfTokenRepository csrfTokenRepository =
                CookieCsrfTokenRepository.withHttpOnlyFalse();

        CsrfTokenRequestAttributeHandler csrfRequestHandler =
                new CsrfTokenRequestAttributeHandler();

        http

                /*
                 * CSRF protection remains ENABLED.
                 *
                 * Authentication endpoints are public through
                 * permitAll(), but they remain protected by CSRF.
                 */
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(csrfRequestHandler)
                )

                /*
                 * CORS
                 */
                .cors(cors -> cors.configurationSource(
                        corsConfigurationSource()
                ))

                /*
                 * Session-based authentication.
                 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.IF_REQUIRED
                        )
                )

                /*
                 * Persist SecurityContext in HTTP session.
                 */
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(false)
                )

                /*
                 * Authorization rules.
                 */
                .authorizeHttpRequests(auth -> auth

                        /*
                         * Public authentication APIs.
                         */
                        .requestMatchers(
                                "/api/v1/auth/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/csrf"
                        ).permitAll()

                        /*
                         * Swagger/OpenAPI.
                         */
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        /*
                         * Everything else requires authentication.
                         */
                        .anyRequest().authenticated()
                )

                /*
                 * REST logout.
                 */
                .logout(logout -> logout

                        .logoutUrl("/api/v1/auth/logout")

                        .invalidateHttpSession(true)

                        .clearAuthentication(true)

                        .deleteCookies(
                                "JSESSIONID",
                                "XSRF-TOKEN"
                        )

                        .logoutSuccessHandler(
                                new HttpStatusReturningLogoutSuccessHandler()
                        )
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "http://localhost:8080"
                )
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of(
                        "Content-Type",
                        "X-XSRF-TOKEN",
                        "X-Requested-With",
                        "Accept"
                )
        );

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}