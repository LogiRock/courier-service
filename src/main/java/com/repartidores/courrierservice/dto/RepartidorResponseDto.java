package com.repartidores.courrierservice.dto;

import java.util.UUID;
import com.repartidores.courrierservice.enums.EstadoRepartidor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RepartidorResponseDto {
    private UUID id;
    private String nombre;
    private String tipoVehiculo;
    private EstadoRepartidor estado;
}
