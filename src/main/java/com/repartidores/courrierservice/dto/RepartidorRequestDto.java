package com.repartidores.courrierservice.dto;

import com.repartidores.courrierservice.enums.EstadoRepartidor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepartidorRequestDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo de vehículo es obligatorio")
    private String tipoVehiculo;

    @NotNull(message = "El estado es obligatorio")
    private EstadoRepartidor estado;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(max = 100)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 72)
    private String password;
}
