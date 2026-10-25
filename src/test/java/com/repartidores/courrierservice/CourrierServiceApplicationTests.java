package com.repartidores.courrierservice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.repartidores.courrierservice.repositories.RepartidorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:courierdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.cloud.config.enabled=false"
})
@AutoConfigureMockMvc
class CourrierServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepartidorRepository repository;

    @BeforeEach
    void limpiarBase() {
        repository.deleteAll();
    }

    @Test
    void permiteRegistrarListarModificarYActualizarEstadoDeRepartidor() throws Exception {
        MvcResult alta = mockMvc.perform(post("/api/v1/repartidores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Ana Pérez","tipoVehiculo":"Motocicleta","estado":"ACTIVO"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nombre").value("Ana Pérez"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"))
                .andReturn();

        String id = extraerId(alta.getResponse().getContentAsString());

        mockMvc.perform(get("/api/v1/repartidores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id));

        mockMvc.perform(put("/api/v1/repartidores/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Ana Pérez Actualizada","tipoVehiculo":"Bicicleta","estado":"ACTIVO"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana Pérez Actualizada"))
                .andExpect(jsonPath("$.tipoVehiculo").value("Bicicleta"))
                .andExpect(jsonPath("$.estado").value("ACTIVO"));

        mockMvc.perform(patch("/api/v1/repartidores/{id}/estado", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"estado":"EN_RUTA"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("EN_RUTA"));

        mockMvc.perform(get("/api/v1/repartidores/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana Pérez Actualizada"))
                .andExpect(jsonPath("$.estado").value("EN_RUTA"));

        mockMvc.perform(patch("/api/v1/repartidores/{id}/estado", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"estado":"INACTIVO"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("INACTIVO"));
    }

    @Test
    void rechazaDatosInvalidosAlRegistrar() throws Exception {
        mockMvc.perform(post("/api/v1/repartidores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":" ","tipoVehiculo":"Motocicleta"}
                                """))
                .andExpect(status().isBadRequest());
    }

    private String extraerId(String json) {
        Matcher matcher = Pattern.compile("\\\"id\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"").matcher(json);
        if (!matcher.find()) {
            throw new AssertionError("La respuesta de alta no contiene un id: " + json);
        }
        return matcher.group(1);
    }
}



