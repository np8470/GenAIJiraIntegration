package com.genai.ollamarestapi.config;

import com.genai.ollamarestapi.security.CustomUserDetailsService;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {

        /* System.out.println("******** SecurityConfig Loaded ********"); */
        log.info("******** SecurityConfig Loaded ********");

    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http

                // Authentication Provider
                .authenticationProvider(authenticationProvider())

                // CSRF
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**"))

                // URL Authorization
                .authorizeHttpRequests(auth -> auth

                        // Public Resources
                        .requestMatchers(
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico")
                        .permitAll()

                        // API
                        .requestMatchers("/api/**")
                        .hasAnyRole("ADMIN", "USER")

                        // Admin Pages
                        .requestMatchers("/admin/**")
                        .hasRole("ADMIN")

                        // User Pages
                        .requestMatchers("/user/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Dashboard
                        .requestMatchers("/", "/dashboard")
                        .authenticated()

                        // actuator health check
                        .requestMatchers("/actuator/health").permitAll()

                        .requestMatchers("/actuator/info").permitAll()

                        .requestMatchers("/actuator/**").hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()

                )

                // Login
                .formLogin(form -> form

                        .loginPage("/login")

                        .loginProcessingUrl("/login")

                        .defaultSuccessUrl("/dashboard", true)

                        .failureUrl("/login?error")

                        .permitAll()

                )

                // Remember Me
                .rememberMe(remember -> remember

                        .rememberMeParameter("remember-me")

                        .tokenValiditySeconds(7 * 24 * 60 * 60)

                        .userDetailsService(userDetailsService)

                )

                // Logout
                .logout(logout -> logout

                        .logoutUrl("/logout")

                        .logoutSuccessUrl("/login?logout")

                        .invalidateHttpSession(true)

                        .deleteCookies("JSESSIONID")

                        .clearAuthentication(true)

                        .permitAll()

                )

                // Session Management
                .sessionManagement(session -> session

                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)

                        .maximumSessions(1)

                        .maxSessionsPreventsLogin(false)

                        .expiredUrl("/login?expired")

                )

                // Access Denied
                .exceptionHandling(exception -> exception

                        .accessDeniedPage("/access-denied")

                );

        // HTTP Basic (optional for REST APIs)
        // .httpBasic(Customizer.withDefaults());

        return http.build();
    }

}