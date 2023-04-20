package tech.devinhouse.personagens.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tech.devinhouse.personagens.service.PersonagemService;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest
class PersonagensControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean  // mock para dependencias da classe de controller
    private PersonagemService service;


    @Test
    @DisplayName("Quando nao há personagens registrados, deve retornar lista vazia")
    void consultar() throws Exception {
        mockMvc.perform(get("/api/personagens")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // 200
                .andExpect(jsonPath("$", is(empty())));
    }

}