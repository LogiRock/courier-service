package com.repartidores.courrierservice.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

import com.repartidores.courrierservice.dto.RegistroUsuarioRequest;
import com.repartidores.courrierservice.dto.UsuarioResponseDto;

@Component
public class SecurityClient {

    private final RestClient restClient;

    public SecurityClient(
            RestClient.Builder builder,
            @Value("${security.service.url}") String url) {

        this.restClient = builder
                .baseUrl(url)
                .build();
    }

    public UsuarioResponseDto registrarUsuario(
            RegistroUsuarioRequest request) {

        return restClient.post()
                .uri("/api/auth/register")
                .body(request)
                .retrieve()
                .body(UsuarioResponseDto.class);
    }

    public UsuarioResponseDto registrarUsuarioRepartidor(
            RegistroUsuarioRequest request) {

        JwtAuthenticationToken authentication =
                (JwtAuthenticationToken) SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String token = authentication.getToken().getTokenValue();

        return restClient.post()
                .uri("/api/auth/register/repartidor")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(request)
                .retrieve()
                .body(UsuarioResponseDto.class);
    }
}
