package com.repartidores.courrierservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.repartidores.courrierservice.security.CustomAccessDeniedHandler;
import com.repartidores.courrierservice.security.CustomAuthenticationEntryPoint;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**")
                        .permitAll()

                        // El repartidor consulta sus propios datos.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/repartidores/me")
                        .hasAuthority("ROLE_REPARTIDOR")

                        // ADMIN y SHIPMENT pueden consultar repartidores.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/repartidores/*",
                                "/api/v1/repartidores/*/disponibilidad")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_SHIPMENT")

                        // ADMIN puede listar.
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/repartidores")
                        .hasAuthority("ROLE_ADMIN")

                        // ADMIN puede crear.
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/v1/repartidores")
                        .hasAuthority("ROLE_ADMIN")

                        // ADMIN puede modificar.
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/v1/repartidores/*")
                        .hasAuthority("ROLE_ADMIN")

                        // ADMIN puede actualizar el estado.
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/v1/repartidores/*/estado")
                        .hasAuthority("ROLE_ADMIN")

                        // ADMIN puede eliminar.
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/v1/repartidores/*")
                        .hasAuthority("ROLE_ADMIN")

                        .anyRequest().permitAll())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("roles");
        authoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}