package com.t2s.conteiner.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.t2s.conteiner.api.dto.ConteinerDTO;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.service.ConteinerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers =  ConteinerController.class)
@AutoConfigureMockMvc
public class ConteinerControllerTest {

    static String CONTEINER_API = "/api/conteineres";

    @Autowired
    MockMvc mvc;

    @MockBean
    ConteinerService service;

    @Test
    @DisplayName("Deve criar um conteiner com sucesso.")
    public void criarContainerComSucesso() throws Exception {
        ConteinerDTO conteinerDto = getConteinerDTO();
        Conteiner conteinerSaved = getConteiner();

        BDDMockito.given(service.salvar(any(Conteiner.class))).willReturn(conteinerSaved);

        String json = new ObjectMapper().writeValueAsString(conteinerDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTEINER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("cliente").value(conteinerDto.getCliente()))
                .andExpect(jsonPath("numero").value(conteinerDto.getNumero()))
                .andExpect(jsonPath("tipo").value(conteinerDto.getTipo()))
                .andExpect(jsonPath("status").value(conteinerDto.getStatus()))
                .andExpect(jsonPath("categoria").value(conteinerDto.getCategoria()));

    }

    private Conteiner getConteiner() {
        return Conteiner.builder()
                .id(1l)
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo("20")
                .status("cheio")
                .categoria("importacao")
                .build();
    }

    private ConteinerDTO getConteinerDTO() {
        return ConteinerDTO.builder()
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo("20")
                .status("cheio")
                .categoria("importacao")
                .build();
    }
}
