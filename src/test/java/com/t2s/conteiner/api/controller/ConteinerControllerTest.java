package com.t2s.conteiner.api.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.t2s.conteiner.api.dto.ConteinerDTO;
import com.t2s.conteiner.exception.NumeroConteinerException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import com.t2s.conteiner.service.ConteinerService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = ConteinerController.class)
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

    @Test
    @DisplayName("Deve lancar um erro de validacao quando nao houver dados suficientes para criacao do conteiner.")
    public void erroDadosInsuficientes() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new ConteinerDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTEINER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(5)));

    }

    @Test
    @DisplayName("Deve lancar um erro de validacao quando informar um numero fora do padrao para criacao do conteiner.")
    public void erroNumeroFormatoInvalido() throws Exception {
        String json = new ObjectMapper().writeValueAsString(getInvalidoNumeroConteinerDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTEINER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @Test
    @DisplayName("Deve lancar um erro de validacao quando informar um tipo fora do padrao para criacao do conteiner.")
    public void erroTipoInvalido() throws Exception {
        String json = new ObjectMapper().writeValueAsString(getInvalidoTipoConteinerDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTEINER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @Test
    @DisplayName("Deve lancar um erro de validacao quando informar uma categoria fora do padrao para criacao do conteiner.")
    public void erroCategoriaInvalida() throws Exception {
        String json = new ObjectMapper().writeValueAsString(getInvalidaCategoriaConteinerDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTEINER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @Test
    @DisplayName("Deve lancar um erro de validacao quando informar um status fora do padrao para criacao do conteiner.")
    public void erroStatusInvalida() throws Exception {
        String json = new ObjectMapper().writeValueAsString(getInvalidoStatusConteinerDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTEINER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)));

    }

    @Test
    @DisplayName("Deve lancar um erro de negocio quando tentar salvar um numero de conteiner ja cadastrado.")
    public void erroConteinerJaCadastrado() throws Exception {
        ConteinerDTO conteinerDto = getConteinerDTO();
        Conteiner conteinerSaved = getConteiner();

        BDDMockito.given(service.salvar(any(Conteiner.class))).willThrow(new NumeroConteinerException("Número de conteiner já cadastrado"));

        String json = new ObjectMapper().writeValueAsString(conteinerDto);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(CONTEINER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Número de conteiner já cadastrado"));

    }

    @Test
    @DisplayName("Deve obter um conteiner pelo id")
    public void obterConteinerPeloId() throws Exception {
        Long id = 1l;
        Conteiner conteiner = getConteiner();
        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.of(conteiner));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CONTEINER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);
        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1l))
                .andExpect(jsonPath("cliente").value(conteiner.getCliente()))
                .andExpect(jsonPath("numero").value(conteiner.getNumero()))
                .andExpect(jsonPath("tipo").value(conteiner.getTipo()))
                .andExpect(jsonPath("status").value(StatusConteinerEnum.CHEIO.toString()))
                .andExpect(jsonPath("categoria").value(CategoriaConteinerEnum.IMPORTACAO.toString()));
    }

    @Test
    @DisplayName("Deve obter um conteiner pelo id")
    public void erroAoObterConteinerComIdInexistente() throws Exception {
        Long id = 1l;
        Conteiner conteiner = getConteiner();
        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CONTEINER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);
        mvc
                .perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Container não encontrado pelo id informado."));
    }

    @Test
    @DisplayName("Deve remover um conteiner pelo id")
    public void removerConteinerPeloId() throws Exception {
        Long id = 1l;
        Conteiner conteinerSaved = getConteiner();

        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.of(conteinerSaved));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CONTEINER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);
        mvc
                .perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar remover um conteiner pelo id inexistente")
    public void erroRemoverConteinerPorIdIncorreto() throws Exception {
        Long id = 1l;
        Conteiner conteinerSaved = getConteiner();

        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.empty());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(CONTEINER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);
        mvc
                .perform(request)
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Container não encontrado pelo id informado."));
    }

    @Test
    @DisplayName("Deve atualizar um conteiner.")
    public void atualizarConteiner() throws Exception {
        Long id = 1l;
        ConteinerDTO conteinerDto = getConteinerDTO();
        conteinerDto.setStatus("VAZIO");
        conteinerDto.setCliente("TESTE");
        String json = new ObjectMapper().writeValueAsString(conteinerDto);

        Conteiner containerSalvo = getConteiner();
        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.of(containerSalvo));
        Conteiner conteinerAtualizado = getConteiner();
        containerSalvo.setStatus(StatusConteinerEnum.VAZIO);
        conteinerAtualizado.setCliente("TESTE");
        BDDMockito.given(service.atualizar(containerSalvo)).willReturn(containerSalvo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CONTEINER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("cliente").value(conteinerDto.getCliente()))
                .andExpect(jsonPath("numero").value(conteinerDto.getNumero()))
                .andExpect(jsonPath("tipo").value(conteinerDto.getTipo()))
                .andExpect(jsonPath("status").value(conteinerDto.getStatus()))
                .andExpect(jsonPath("categoria").value(conteinerDto.getCategoria()));

    }

    @Test
    @DisplayName("Deve retornar erro ao tentar atualizar um conteiner pelo id inexistente.")
    public void erroAtualizarConteinerIdInexistente() throws Exception {
        Long id = 1l;
        ConteinerDTO conteinerDto = getConteinerDTO();
        String json = new ObjectMapper().writeValueAsString(conteinerDto);

        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CONTEINER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Container não encontrado pelo id informado."));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar atualizar um conteiner por um numero já cadastrado.")
    public void erroAtualizarConteinerNumeroJaCadastrado() throws Exception {
        Long id = 1l;
        ConteinerDTO conteinerDto = getConteinerDTO();
        conteinerDto.setStatus("VAZIO");
        conteinerDto.setCliente("TESTE");
        String json = new ObjectMapper().writeValueAsString(conteinerDto);

        Conteiner containerSalvo = getConteiner();
        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.of(containerSalvo));
        Conteiner conteinerAtualizado = getConteiner();
        containerSalvo.setStatus(StatusConteinerEnum.VAZIO);
        conteinerAtualizado.setCliente("TESTE");
        BDDMockito.given(service.atualizar(containerSalvo)).willThrow( new NumeroConteinerException("Container já cadastrado com esse número."));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(CONTEINER_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Container já cadastrado com esse número."));
    }

    @Test
    @DisplayName("Deve filtrar conteiners")
    public void buscarConteinerComFiltro() throws Exception {
        Long id = 1l;

        Conteiner container = getConteiner();

        BDDMockito.given( service.buscar(any(Conteiner.class), any(Pageable.class)) )
                .willReturn( new PageImpl<Conteiner>(Arrays.asList(container), PageRequest.of(0,100), 1) );

        String queryString = String.format("?cliente=%s&numero=%s&tipo=%s&status=%s&categoria=%s&page=0&size=100", container.getCliente(), container.getNumero(), container.getTipo(), container.getStatus(), container.getCategoria());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(CONTEINER_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }


    private ConteinerDTO getInvalidaCategoriaConteinerDTO() {
        return ConteinerDTO.builder()
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo(20)
                .status("CHEIO")
                .categoria("TESTE")
                .build();
    }

    private Conteiner getConteiner() {
        return Conteiner.builder()
                .id(1l)
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo(20)
                .status(StatusConteinerEnum.CHEIO)
                .categoria(CategoriaConteinerEnum.IMPORTACAO)
                .build();
    }

    private ConteinerDTO getConteinerDTO() {
        return ConteinerDTO.builder()
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo(20)
                .status("CHEIO")
                .categoria("IMPORTACAO")
                .build();
    }

    private ConteinerDTO getInvalidoStatusConteinerDTO() {
        return ConteinerDTO.builder()
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo(20)
                .status("TESTE")
                .categoria("IMPORTACAO")
                .build();
    }

    private ConteinerDTO getInvalidoNumeroConteinerDTO() {
        return ConteinerDTO.builder()
                .cliente("T2S")
                .numero("A2CD12345B7")
                .tipo(20)
                .status("CHEIO")
                .categoria("IMPORTACAO")
                .build();
    }

    private ConteinerDTO getInvalidoTipoConteinerDTO() {
        return ConteinerDTO.builder()
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo(21)
                .status("CHEIO")
                .categoria("IMPORTACAO")
                .build();
    }
}
