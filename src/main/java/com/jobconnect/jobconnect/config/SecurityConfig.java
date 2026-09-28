
package com.jobconnect.jobconnect.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jobconnect.jobconnect.security.JwtAuthenticationFilter;
import com.jobconnect.jobconnect.service.CustomUserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.jobconnect.jobconnect.security.JwtAccessDeniedHandler;
import com.jobconnect.jobconnect.security.JwtAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            // =========================================
            // DISABLE CSRF
            // =========================================
            .csrf(csrf -> csrf.disable())

            // =========================================
            // JWT = STATELESS
            // =========================================
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // =========================================
                // PUBLIC FRONTEND
                // =========================================
                .requestMatchers(
                    "/",
                    "/index.html",
                    "/login.html",
                    "/register.html",
                    "/profile.html",
                    "/edit-profile.html",
                    "/apply.html",
                    "/my-applications.html",
                    "/job-seeker-auth.html",
                    "/employer-auth.html",
                    "/about.html",
                    "/job-seeker-dashboard.html",
                    "/employer-dashboard.html",
                    "/applications-management.html",
                    "/application-details.html",
                    "/edit-application.html",
                    "/error",
                     "/manage-jobs.html",
                     "/employer-register.html",
                     "/admin-dashboard.html",
                     "/admin-applications.html",
                     "/admin-users.html",
                     "/admin-jobs.html",
                      "/post-job.html",
                      "/jobs.html",
                    "/candidates.html",
                    "/candidate-details.html",
                   "/edit-job.html",
                  "/change-password.html",
                    "/style.css",
                    "/app.js",
                    "/favicon.ico",
                    "/css/**",
                    "/js/**"
                ).permitAll()

                // =========================================
                // PUBLIC REGISTRATION
                // =========================================
                .requestMatchers(
                	    HttpMethod.POST,
                	    "/users/register",
                	    "/users/register-employer"
                	).permitAll()

                // =========================================
                // PUBLIC LOGIN
                // =========================================
                .requestMatchers(
                    "/auth/login"
                ).permitAll()
                
             // =========================================
             // JOBS
             // =========================================

             // Anyone can view available jobs
             // JOBS

                .requestMatchers(
                    HttpMethod.GET,
                    "/jobs"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.GET,
                    "/jobs/my"
                ).hasRole("EMPLOYER")

                .requestMatchers(
                    HttpMethod.GET,
                    "/jobs/*"
                ).permitAll()

                .requestMatchers(
                    HttpMethod.POST,
                    "/jobs"
                ).hasRole("EMPLOYER")
                .requestMatchers(
                	    HttpMethod.PUT,
                	    "/jobs/*"
                	).hasRole("EMPLOYER")
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/jobs/*"
                ).hasRole("EMPLOYER")

             // Employer can view their own jobs
             .requestMatchers(
                 HttpMethod.GET,
                 "/jobs/my"
             ).hasRole("EMPLOYER")

             // Employer can delete their own job
             .requestMatchers(
                 HttpMethod.DELETE,
                 "/jobs/*"
             ).hasRole("EMPLOYER")
             

                // =========================================
                // JOB SEEKER
                // =========================================

                // Submit application
             .requestMatchers(
            		    HttpMethod.POST,
            		    "/applications"
            		).hasAuthority("ROLE_JOB_SEEKER")
                // View own applications
                .requestMatchers(
                    HttpMethod.GET,
                    "/applications/my"
                ).hasRole("JOB_SEEKER")

                // =========================================
                // EMPLOYER + ADMIN
                // =========================================

                // View all applications
                .requestMatchers(
                    HttpMethod.GET,
                    "/applications"
                ).hasAnyRole("ADMIN", "EMPLOYER")

                // Accept application
                .requestMatchers(
                    HttpMethod.PUT,
                    "/applications/*/accept"
                ).hasAnyRole("ADMIN", "EMPLOYER")

                // Reject application
                .requestMatchers(
                    HttpMethod.PUT,
                    "/applications/*/reject"
                ).hasAnyRole("ADMIN", "EMPLOYER")

                // =========================================
                // EVERYTHING ELSE
                // =========================================
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .exceptionHandling(exception -> exception
            	    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            	    .accessDeniedHandler(jwtAccessDeniedHandler)
            	)
            // =========================================
            // JWT FILTER
            // =========================================
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }

    // =========================================
    // PASSWORD ENCODER
    // =========================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // =========================================
    // AUTHENTICATION PROVIDER
    // =========================================

    @Bean
    public AuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder
        );

        return provider;
    }

    // =========================================
    // AUTHENTICATION MANAGER
    // =========================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }
}
