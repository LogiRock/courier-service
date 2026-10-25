package com.repartidores.courrierservice.services;

import java.util.List;
import java.util.UUID;

import com.repartidores.courrierservice.dto.RepartidorRequestDto;
import com.repartidores.courrierservice.dto.RepartidorResponseDto;
import com.repartidores.courrierservice.enums.EstadoRepartidor;

/**
 * Define las operaciones de negocio para la gestión independiente de repartidores.
 */
public interface RepartidorService {

    /**
     * Registra un nuevo repartidor en el sistema.
     *
     * @param request Datos del repartidor a registrar.
     * @return DTO con la información persistida del repartidor.
     */
    RepartidorResponseDto registrarRepartidor(RepartidorRequestDto request);

    /**
     * Obtiene la información de un repartidor específico mediante su identificador.
     *
     * @param id Identificador único del repartidor.
     * @return DTO con los detalles del repartidor.
     */
    RepartidorResponseDto obtenerRepartidor(UUID id);

    /**
     * Recupera la lista completa de todos los repartidores registrados.
     *
     * @return Lista de DTOs con la información de cada repartidor.
     */
    List<RepartidorResponseDto> listarRepartidores();

    /**
     * Modifica los datos de un repartidor existente.
     *
     * @param id Identificador único del repartidor a modificar.
     * @param request Datos actualizados del repartidor.
     * @return DTO con la información actualizada.
     */
    RepartidorResponseDto modificarRepartidor(UUID id, RepartidorRequestDto request);

    /**
     * Modifica el estado operativo de un repartidor.
     *
     * @param id Identificador único del repartidor.
     * @param estado Nuevo estado operativo.
     * @return DTO con el repartidor actualizado.
     */
    RepartidorResponseDto actualizarEstado(UUID id, EstadoRepartidor estado);

    /**
     * Elimina un repartidor del sistema.
     *
     * @param id Identificador único del repartidor a eliminar.
     */
    void eliminarRepartidor(UUID id);
}
