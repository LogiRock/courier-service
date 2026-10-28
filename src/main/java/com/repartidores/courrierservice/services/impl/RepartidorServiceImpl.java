package com.repartidores.courrierservice.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.repartidores.courrierservice.clients.SecurityClient;
import com.repartidores.courrierservice.dto.RegistroUsuarioRequest;
import com.repartidores.courrierservice.dto.RepartidorRequestDto;
import com.repartidores.courrierservice.dto.RepartidorResponseDto;
import com.repartidores.courrierservice.dto.UsuarioResponseDto;
import com.repartidores.courrierservice.enums.EstadoRepartidor;
import com.repartidores.courrierservice.models.Repartidor;
import com.repartidores.courrierservice.repositories.RepartidorRepository;
import com.repartidores.courrierservice.services.RepartidorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepartidorServiceImpl implements RepartidorService {

    /**
     * Repositorio inyectado para la persistencia de la entidad Repartidor.
     */
    private final RepartidorRepository repartidorRepository;

    private final SecurityClient securityClient;

    // @Override
    // @Transactional
    // public RepartidorResponseDto registrarRepartidor(RepartidorRequestDto
    // request) {
    // log.info("Registrando nuevo repartidor con nombre: {}", request.getNombre());

    // Repartidor repartidor = new Repartidor();
    // repartidor.setNombre(request.getNombre());
    // repartidor.setTipoVehiculo(request.getTipoVehiculo());
    // repartidor.setEstado(request.getEstado());

    // repartidor = repartidorRepository.save(repartidor);
    // return mapearAResponseDto(repartidor);
    // }

    @Override
    @Transactional
    public RepartidorResponseDto registrarRepartidor(
            RepartidorRequestDto request) {

        log.info("Registrando nuevo repartidor con nombre: {}", request.getNombre());


        // 1. Crear la cuenta en security-service
        UsuarioResponseDto usuario = securityClient.registrarUsuarioRepartidor(
                new RegistroUsuarioRequest(
                        request.getUsername(),
                        request.getPassword()));

        log.info("Usuario creado con ID: {}", usuario.id());

        Repartidor repartidor = new Repartidor();
        repartidor.setNombre(request.getNombre());
        repartidor.setTipoVehiculo(request.getTipoVehiculo());
        repartidor.setEstado(request.getEstado());
        repartidor.setUserId(usuario.id());

        repartidor = repartidorRepository.save(repartidor);

        log.info("Repartidor guardado con ID: {}, userId: {}",
                repartidor.getId(), repartidor.getUserId());

        return mapearAResponseDto(repartidor);
    }

    @Override
    @Transactional(readOnly = true)
    public RepartidorResponseDto obtenerRepartidor(UUID id) {
        Repartidor repartidor = buscarRepartidorPorId(id);
        return mapearAResponseDto(repartidor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RepartidorResponseDto> listarRepartidores() {
        return repartidorRepository.findAll()
                .stream()
                .map(this::mapearAResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RepartidorResponseDto modificarRepartidor(UUID id, RepartidorRequestDto request) {
        log.info("Modificando repartidor con ID: {}", id);

        Repartidor repartidor = buscarRepartidorPorId(id);
        repartidor.setNombre(request.getNombre());
        repartidor.setTipoVehiculo(request.getTipoVehiculo());
        repartidor.setEstado(request.getEstado());

        repartidor = repartidorRepository.save(repartidor);
        return mapearAResponseDto(repartidor);
    }

    /**
     * Actualiza el estado operativo del repartidor.
     *
     * @param id     Identificador único del repartidor.
     * @param estado Nuevo estado operativo.
     * @return Datos actualizados del repartidor.
     */
    @Override
    @Transactional
    public RepartidorResponseDto actualizarEstado(UUID id, EstadoRepartidor estado) {
        log.info("Actualizando estado del repartidor {} a {}", id, estado);
        Repartidor repartidor = buscarRepartidorPorId(id);
        repartidor.setEstado(estado);
        return mapearAResponseDto(repartidorRepository.save(repartidor));
    }

    @Override
    @Transactional
    public void eliminarRepartidor(UUID id) {
        log.info("Eliminando repartidor con ID: {}", id);

        Repartidor repartidor = buscarRepartidorPorId(id);
        repartidorRepository.delete(repartidor);
    }

    /**
     * Método auxiliar privado para buscar un repartidor y lanzar excepción
     * estandarizada si no existe.
     *
     * @param id Identificador único del repartidor.
     * @return Entidad Repartidor localizada.
     */
    private Repartidor buscarRepartidorPorId(UUID id) {
        return repartidorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró ningún repartidor con el ID: " + id));
    }

    /**
     * Método auxiliar privado para convertir la entidad en un DTO de respuesta.
     *
     * @param repartidor Entidad a transformar.
     * @return DTO resultante.
     */
    private RepartidorResponseDto mapearAResponseDto(Repartidor repartidor) {
        return new RepartidorResponseDto(
                repartidor.getId(),
                repartidor.getNombre(),
                repartidor.getTipoVehiculo(),
                repartidor.getEstado());
    }

    @Override
    @Transactional(readOnly = true)
    public RepartidorResponseDto obtenerPorUserId(UUID userId) {
        Repartidor repartidor = repartidorRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No existe un repartidor asociado al usuario autenticado"));

        return mapearAResponseDto(repartidor);
    }
}
