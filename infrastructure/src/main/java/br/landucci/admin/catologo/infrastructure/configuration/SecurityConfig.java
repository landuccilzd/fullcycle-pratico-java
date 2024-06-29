package br.landucci.admin.catologo.infrastructure.configuration;

import br.landucci.admin.catologo.infrastructure.configuration.security.KeycloakJwtConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
@Profile("!dev")
public class SecurityConfig {

    private static final String ROLE_ADMIN = "CODEFLIX_ADMIN";
    private static final String ROLE_CAST_MEMBERS = "CODEFLIX_CASTMEMBERS";
    private static final String ROLE_CATEGORIES = "CODEFLIX_CATEGORIES";
    private static final String ROLE_GENRES = "CODEFLIX_GENRES";
    private static final String ROLE_VIDEOS = "CODEFLIX_VIDEOS";

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> {
                    csrf.disable();
                })
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/castmembers*").hasAnyRole(ROLE_ADMIN, ROLE_CAST_MEMBERS)
                            .requestMatchers("/categories*").hasAnyRole(ROLE_ADMIN, ROLE_CATEGORIES)
                            .requestMatchers("/genres*").hasAnyRole(ROLE_ADMIN, ROLE_GENRES)
                            .requestMatchers("/videos*").hasAnyRole(ROLE_ADMIN, ROLE_VIDEOS)
                            .anyRequest().hasRole(ROLE_ADMIN);
                })
                .oauth2ResourceServer(oauth -> {
                    oauth.jwt()
                            .jwtAuthenticationConverter(new KeycloakJwtConverter());
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .headers(headers -> {
                    headers.frameOptions().sameOrigin();
                })
                .build();
    }

}