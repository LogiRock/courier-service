package com.repartidores.courrierservice.dto;

import java.util.UUID;


public record UsuarioResponseDto(
        UUID id,
        String username,
        String rol
) {
}