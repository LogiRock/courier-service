package com.repartidores.courrierservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/** Resultado de consultar si un repartidor puede recibir un envío. */
@Schema(description = "Disponibilidad operativa de un repartidor")
public record DisponibilidadRepartidorResponseDto(
        @Schema(description = "Indica si puede recibir envíos", example = "true")
        boolean disponible,
        @Schema(description = "Motivo por el que no está disponible", example = "El repartidor está INACTIVO.", nullable = true)
        String motivo) {
}
