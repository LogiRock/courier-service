package com.repartidores.courrierservice.clients;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import feign.RequestInterceptor;

public class SecurityClientConfiguration {

    @Bean
    public RequestInterceptor securityRequestInterceptor() {
        return template -> {

            // El registro normal de usuarios no necesita token.
            if (!template.url().contains("/api/auth/register/repartidor")) {
                return;
            }

            var authentication = SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {

                String token = jwtAuthentication.getToken().getTokenValue();

                template.header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer " + token);
            }
        };
    }
}