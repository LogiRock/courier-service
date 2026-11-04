package com.repartidores.courrierservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.repartidores.courrierservice.dto.RegistroUsuarioRequest;
import com.repartidores.courrierservice.dto.UsuarioResponseDto;

@FeignClient(
        name = "SECURITY-SERVICE",
        configuration = SecurityClientConfiguration.class)
public interface SecurityClient {

    @PostMapping("/api/auth/register")
    UsuarioResponseDto registrarUsuario(
            @RequestBody RegistroUsuarioRequest request);

    @PostMapping("/api/auth/register/repartidor")
    UsuarioResponseDto registrarUsuarioRepartidor(
            @RequestBody RegistroUsuarioRequest request);
}