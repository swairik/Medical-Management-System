package com.mms.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()

                .requestMatchers("/auth/**").permitAll()

                // .requestMatchers("/patient/display/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/patient/**").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/patient/**").hasAnyAuthority("ADMIN", "PATIENT")
                .requestMatchers(HttpMethod.DELETE, "/patient/**").hasAuthority("ADMIN")

                // .requestMatchers("/doctor/display/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/doctor/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/doctor/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/doctor/**").hasAuthority("ADMIN")

                // .requestMatchers("/appointment/display/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/appointment/**").hasAnyAuthority("ADMIN", "PATIENT")
                .requestMatchers(HttpMethod.PUT, "/appointment/**").hasAnyAuthority("ADMIN", "PATIENT")
                .requestMatchers(HttpMethod.DELETE, "/appointment/**").hasAnyAuthority("ADMIN", "PATIENT")

                // .requestMatchers("/report/display/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/report/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PUT, "/report/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/report/**").hasAnyAuthority("ADMIN", "DOCTOR")

                // .requestMatchers("/schedule/display/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/schedule/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PUT, "/schedule/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/schedule/**").hasAnyAuthority("ADMIN")

                // .requestMatchers("/slot/display/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/slot/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PUT, "/slot/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.DELETE, "/slot/**").hasAnyAuthority("ADMIN")

                // .requestMatchers("/speciality/display/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/speciality/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PUT, "/speciality/**").hasAnyAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/speciality/**").hasAnyAuthority("ADMIN")

                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.getOrBuild();
    }

}
