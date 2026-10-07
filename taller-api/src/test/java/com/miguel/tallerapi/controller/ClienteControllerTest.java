package com.miguel.tallerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miguel.tallerapi.dto.cliente.ClienteRequest;
import com.miguel.tallerapi.dto.cliente.ClienteResponse;
import com.miguel.tallerapi.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

    @Test
    @WithMockUser
    void crear_deberiaDevolver201CuandoElBodyEsValido() throws Exception {
        ClienteRequest request = new ClienteRequest("Ana Lopez", "87654321B", "611222333", "ana@example.com");
        ClienteResponse response = ClienteResponse.builder()
                .id(1L).nombre("Ana Lopez").dni("87654321B").telefono("611222333").email("ana@example.com")
                .numeroVehiculos(0)
                .build();

        when(clienteService.crear(any())).thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ana Lopez"))
                .andExpect(jsonPath("$.dni").value("87654321B"));
    }

    @Test
    @WithMockUser
    void crear_deberiaDevolver400CuandoFaltaElNombre() throws Exception {
        String bodyInvalido = """
                {
                  "nombre": "",
                  "dni": "87654321B",
                  "telefono": "611222333",
                  "email": "ana@example.com"
                }
                """;

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erroresCampos.nombre").exists());
    }

    @Test
    void crear_deberiaDevolver401CuandoNoHayAutenticacion() throws Exception {
        ClienteRequest request = new ClienteRequest("Ana Lopez", "87654321B", "611222333", "ana@example.com");

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}
