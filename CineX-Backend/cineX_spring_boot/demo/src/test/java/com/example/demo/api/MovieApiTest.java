package com.example.demo.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Pruebas de la API de películas: CRUD completo, errores 404 y validaciones. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MovieApiTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void crearConsultarActualizarYEliminar() throws Exception {
        MvcResult creada = mvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Encanto\",\"genre\":\"Animación\",\"durationMinutes\":102,\"tmdbId\":568124}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Encanto"))
                .andReturn();
        int id = JsonPath.read(creada.getResponse().getContentAsString(), "$.id");

        mvc.perform(get("/movies/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.durationMinutes").value(102));

        mvc.perform(put("/movies/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Encanto (doblada)\",\"genre\":\"Animación\",\"durationMinutes\":102}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Encanto (doblada)"));

        mvc.perform(delete("/movies/{id}", id)).andExpect(status().isNoContent());

        mvc.perform(get("/movies/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.estado").value(404))
                .andExpect(jsonPath("$.mensaje").value("Película con id " + id + " no existe"));
    }

    @Test
    void rechazaDatosInvalidosConElCampoQueFallo() throws Exception {
        mvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"\",\"durationMinutes\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.title").value("El título es obligatorio"))
                .andExpect(jsonPath("$.campos.durationMinutes").value("La duración debe ser mayor que cero"));
    }

    @Test
    void rechazaJsonMalFormado() throws Exception {
        mvc.perform(post("/movies").contentType(MediaType.APPLICATION_JSON).content("{titulo sin comillas"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El cuerpo de la solicitud no es un JSON válido"));
    }
}
