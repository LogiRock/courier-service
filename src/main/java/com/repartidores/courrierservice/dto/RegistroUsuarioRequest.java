package com.repartidores.courrierservice.dto;

public record RegistroUsuarioRequest(
        String username,
        String password
) {
}