package com.repartidores.courrierservice.dto;

import com.repartidores.courrierservice.enums.EstadoRepartidor;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EstadoRepartidorRequestDto {
    @NotNull(message = "El estado es obligatorio")
    private EstadoRepartidor estado;
}
