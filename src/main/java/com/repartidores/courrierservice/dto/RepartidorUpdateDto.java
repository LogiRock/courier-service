package com.repartidores.courrierservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Datos editables del repartidor; su estado se modifica exclusivamente por PATCH. */
@Getter
@Setter
@NoArgsConstructor
public class RepartidorUpdateDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    private String tipoVehiculo;
}
