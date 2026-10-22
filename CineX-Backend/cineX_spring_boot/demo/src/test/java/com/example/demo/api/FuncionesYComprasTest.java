package com.example.demo.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Reglas de negocio: capacidad de la sala, horarios y borrado de registros en uso. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class FuncionesYComprasTest {

    private static final String HORA = "2030-05-10T19:30:00";

    @Autowired
    private MockMvc mvc;

    private int movieId;
    private int roomId;
    private int showtimeId;

    private ResultActions postJson(String ruta, String json) throws Exception {
        return mvc.perform(post(ruta).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    private int idDe(ResultActions resultado) throws Exception {
        return JsonPath.read(resultado.andReturn().getResponse().getContentAsString(), "$.id");
    }

    @BeforeEach
    void prepararFuncionConTresSillas() throws Exception {
        movieId = idDe(postJson("/movies", "{\"title\":\"La estrategia del caracol\",\"durationMinutes\":115}")
                .andExpect(status().isCreated()));
        roomId = idDe(postJson("/rooms", "{\"name\":\"Sala pequeña\",\"capacity\":3}")
                .andExpect(status().isCreated()));
        showtimeId = idDe(postJson("/showtimes", "{\"movie\":{\"id\":" + movieId + "},\"room\":{\"id\":" + roomId
                + "},\"showDateTime\":\"" + HORA + "\",\"price\":12000}")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.movie.title").value("La estrategia del caracol"))
                .andExpect(jsonPath("$.room.capacity").value(3)));
    }

    private String compra(int sillas) {
        return "{\"showtime\":{\"id\":" + showtimeId + "},\"customerEmail\":\"ana@correo.com\",\"numberOfSeats\":" + sillas + "}";
    }

    @Test
    void noVendeMasSillasQueLaCapacidadDeLaSala() throws Exception {
        postJson("/bookings", compra(2)).andExpect(status().isCreated());

        postJson("/bookings", compra(2))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("Solo quedan 1 sillas disponibles para esta función"));

        mvc.perform(get("/showtimes/{id}/availability", showtimeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacidad").value(3))
                .andExpect(jsonPath("$.vendidas").value(2))
                .andExpect(jsonPath("$.disponibles").value(1));

        postJson("/bookings", compra(1)).andExpect(status().isCreated());
    }

    @Test
    void editarUnaCompraTieneEnCuentaSusPropiasSillas() throws Exception {
        int bookingId = idDe(postJson("/bookings", compra(2)).andExpect(status().isCreated()));

        mvc.perform(put("/bookings/{id}", bookingId).contentType(MediaType.APPLICATION_JSON).content(compra(3)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfSeats").value(3));

        mvc.perform(put("/bookings/{id}", bookingId).contentType(MediaType.APPLICATION_JSON).content(compra(4)))
                .andExpect(status().isConflict());
    }

    @Test
    void noProgramaDosFuncionesEnLaMismaSalaALaMismaHora() throws Exception {
        postJson("/showtimes", "{\"movie\":{\"id\":" + movieId + "},\"room\":{\"id\":" + roomId
                + "},\"showDateTime\":\"" + HORA + "\",\"price\":12000}")
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje", containsString("ya tiene una función")));
    }

    @Test
    void funcionConPeliculaInexistenteResponde404() throws Exception {
        postJson("/showtimes", "{\"movie\":{\"id\":999999},\"room\":{\"id\":" + roomId
                + "},\"showDateTime\":\"2030-05-11T19:30:00\",\"price\":12000}")
                .andExpect(status().isNotFound());
    }

    @Test
    void funcionSinPeliculaResponde400() throws Exception {
        postJson("/showtimes", "{\"room\":{\"id\":" + roomId + "},\"showDateTime\":\"2030-05-11T19:30:00\",\"price\":12000}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.movie").exists());
    }

    @Test
    void noEliminaRegistrosQueEstanEnUso() throws Exception {
        mvc.perform(delete("/movies/{id}", movieId)).andExpect(status().isConflict());
        mvc.perform(delete("/rooms/{id}", roomId)).andExpect(status().isConflict());

        postJson("/bookings", compra(1)).andExpect(status().isCreated());
        mvc.perform(delete("/showtimes/{id}", showtimeId)).andExpect(status().isConflict());
    }

    @Test
    void noPermiteSalasConNombreRepetido() throws Exception {
        postJson("/rooms", "{\"name\":\"sala PEQUEÑA\",\"capacity\":10}").andExpect(status().isConflict());
    }

    @Test
    void validaElCorreoYElNumeroDeSillas() throws Exception {
        postJson("/bookings", "{\"showtime\":{\"id\":" + showtimeId + "},\"customerEmail\":\"no-es-correo\",\"numberOfSeats\":0}")
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.customerEmail").exists())
                .andExpect(jsonPath("$.campos.numberOfSeats").exists());
    }
}
