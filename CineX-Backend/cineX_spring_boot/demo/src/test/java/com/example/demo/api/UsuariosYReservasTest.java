package com.example.demo.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Usuarios, productos y reservas: los recursos que usa el panel de administración. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsuariosYReservasTest {

    @Autowired
    private MockMvc mvc;

    private int crear(String ruta, String json) throws Exception {
        String cuerpo = mvc.perform(post(ruta).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(cuerpo, "$.id");
    }

    @Test
    void reservaCompletaLosDatosDelUsuarioYElProducto() throws Exception {
        int usuarioId = crear("/usuarios", "{\"nombre\":\"Ana\",\"email\":\"ana@correo.com\"}");
        int productoId = crear("/productos", "{\"nombre\":\"Crispetas\",\"precio\":12000}");

        int reservaId = crear("/reservas", "{\"usuario\":{\"id\":" + usuarioId + "},\"producto\":{\"id\":" + productoId
                + "},\"tmdbMovieId\":278,\"movieTitle\":\"Sueño de fuga\"}");

        mvc.perform(get("/reservas/{id}", reservaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usuario.nombre").value("Ana"))
                .andExpect(jsonPath("$.producto.nombre").value("Crispetas"))
                .andExpect(jsonPath("$.movieTitle").value("Sueño de fuga"));
    }

    @Test
    void reservaConUsuarioInexistenteResponde404() throws Exception {
        mvc.perform(post("/reservas").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"usuario\":{\"id\":999999},\"tmdbMovieId\":278}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void usuarioCrudYValidacionDeCorreo() throws Exception {
        int id = crear("/usuarios", "{\"nombre\":\"Daniel\",\"email\":\"daniel@correo.com\"}");

        mvc.perform(put("/usuarios/{id}", id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Daniel J.\",\"email\":\"daniel@correo.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Daniel J."));

        mvc.perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Sin correo\",\"email\":\"correo-invalido\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.email").value("El correo no tiene un formato válido"));

        mvc.perform(delete("/usuarios/{id}", id)).andExpect(status().isNoContent());
        mvc.perform(delete("/usuarios/{id}", id)).andExpect(status().isNotFound());
    }
}
