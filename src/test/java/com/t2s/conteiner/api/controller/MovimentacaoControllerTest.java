package com.t2s.conteiner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t2s.conteiner.api.dto.*;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.entity.Movimentacao;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import com.t2s.conteiner.model.enums.TipoMovimentacaoConteiner;
import com.t2s.conteiner.service.ConteinerService;
import com.t2s.conteiner.service.MovimentacaoService;
import com.t2s.conteiner.service.MovimentacaoServiceTest;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = MovimentacaoController.class)
@AutoConfigureMockMvc
public class MovimentacaoControllerTest {

    static String MOVIMENTACAO_API = "/api/movimentacoes";

    @Autowired
    MockMvc mvc;

    @MockBean
    MovimentacaoService service;

    @MockBean
    ConteinerService conteinerService;


    @Test
    @DisplayName("Deve criar uma movimentacao com sucesso.")
    public void criarMovimentacaoComSucesso() throws Exception {

//        CENARIO:
//          DTO:
//              navio("NORTE SA")
//              tipo("EMBARQUE")
//              conteinerId(1l)
//              dataInicio("2019-02-03 10:08:02")
//              dataFim("2019-02-03 10:08:02")
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        MovimentacaoSalvarTestDTO movimentacaoDTO = getMovimentacaoTestDTO();
        Movimentacao movimentacaoSalva = getMovimentacao();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        movimentacaoSalva.setDataInicio(LocalDateTime.parse(movimentacaoDTO.getDataInicio(), formatter));
        movimentacaoSalva.setDataFim(LocalDateTime.parse(movimentacaoDTO.getDataFim(), formatter));
        BDDMockito.given(conteinerService.obterPeloId(anyLong())).willReturn(Optional.of(getConteiner()));
        BDDMockito.given(service.salvar(any(Movimentacao.class))).willReturn(movimentacaoSalva);

        String json = new ObjectMapper().writeValueAsString(movimentacaoDTO);

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MOVIMENTACAO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

//        VALIDACAO:

        mvc
                .perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("navio").value(movimentacaoSalva.getNavio()))
                .andExpect(jsonPath("tipo").value(movimentacaoSalva.getTipo().toString()))
                .andExpect(jsonPath("dataInicio").value(movimentacaoDTO.getDataInicio()))
                .andExpect(jsonPath("dataFim").value(movimentacaoDTO.getDataFim()))
                .andExpect(jsonPath("conteiner.id").value(movimentacaoSalva.getConteiner().getId()));

    }

    @Test
    @DisplayName("Deve dar erro ao tentar fazer uma movimentacao com conteiner de id errado.")
    public void erroCriarMovimentacaoConteinerIdInvalido() throws Exception {
//        CENARIO:
//          DTO:
//              navio("NORTE SA")
//              tipo("EMBARQUE")
//              conteinerId(1l)
//              dataInicio("2019-02-03 10:08:02")
//              dataFim("2019-02-03 10:08:02")

        MovimentacaoSalvarTestDTO movimentacaoDTO = getMovimentacaoTestDTO();

        BDDMockito.given(conteinerService.obterPeloId(anyLong())).willReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(movimentacaoDTO);

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MOVIMENTACAO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

//        VALIDACAO:

        mvc
                .perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Container não encontrado pelo id informado."));

    }


    @Test
    @DisplayName("Deve lancar um erro de validacao quando nao houver dados suficientes para criacao de uma movimentacao.")
    public void erroDadosInsuficientes() throws Exception {
//        CENARIO:
//          DTO:
//              nulo

        String json = new ObjectMapper().writeValueAsString(new MovimentacaoSalvarTestDTO());

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MOVIMENTACAO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

//        VALIDACAO:

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(5)));

    }

    @Test
    @DisplayName("Deve obter uma movimentacao pelo id")
    public void obterMovimentacaoPeloId() throws Exception {
//        CENARIO:
//          id: 1l
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Long id = 1l;
        Movimentacao movimentacaoSalva = getMovimentacao();
        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.of(movimentacaoSalva));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(MOVIMENTACAO_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

//        VALIDACAO:

        mvc
                .perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("navio").value(movimentacaoSalva.getNavio()))
                .andExpect(jsonPath("tipo").value(movimentacaoSalva.getTipo().toString()))
                .andExpect(jsonPath("dataInicio").value(movimentacaoSalva.getDataInicio().format(formatter)))
                .andExpect(jsonPath("dataFim").value(movimentacaoSalva.getDataFim().format(formatter)))
                .andExpect(jsonPath("conteiner.id").value(movimentacaoSalva.getConteiner().getId()));
    }


    @Test
    @DisplayName("Deve remover uma movimentacao pelo id")
    public void removerMovimentacaoPeloId() throws Exception {
//        CENARIO:
//          id: 1l
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Long id = 1l;
        Movimentacao movimentacaoSaved = getMovimentacao();

        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.of(movimentacaoSaved));

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(MOVIMENTACAO_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

//        VALIDACAO:

        mvc
                .perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar remover uma movimentacao pelo id inexistente")
    public void erroRemoverMovimentacaoPorIdIncorreto() throws Exception {
//        CENARIO:
//          id: 1l

        Long id = 1l;
        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.empty());

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(MOVIMENTACAO_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

//        VALIDACAO:

        mvc
                .perform(request)
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Movimentação não encontrado pelo id informado."));
    }

    @Test
    @DisplayName("Deve atualizar uma movimentacao.")
    public void atualizarMovimentacao() throws Exception {
//        CENARIO:
//          id: 1l
//          DTO:
//              navio("TESTE")
//              tipo("GATEIN")
//              conteinerId(1l)
//              dataInicio("2019-02-03 10:08:02")
//              dataFim("2019-02-03 10:08:02")
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Long id = 1l;
        MovimentacaoSalvarTestDTO movimentacaoDto = getMovimentacaoTestDTO();
        movimentacaoDto.setNavio("TESTE");
        movimentacaoDto.setTipo("GATEIN");

        String json = new ObjectMapper().writeValueAsString(movimentacaoDto);

        Movimentacao movimentacaoSalva = getMovimentacao();

        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.of(movimentacaoSalva));

        BDDMockito.given(conteinerService.obterPeloId(movimentacaoDto.getConteinerId())).willReturn(Optional.of(getConteiner()));

        Movimentacao movimentacaoAtualizada = movimentacaoSalva;
        movimentacaoAtualizada.setNavio(movimentacaoDto.getNavio());
        movimentacaoAtualizada.setTipo(TipoMovimentacaoConteiner.GATEIN);

        BDDMockito.given(service.atualizar(movimentacaoSalva)).willReturn(movimentacaoAtualizada);

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(MOVIMENTACAO_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

//        VALIDACAO:

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("navio").value(movimentacaoDto.getNavio()))
                .andExpect(jsonPath("tipo").value(movimentacaoDto.getTipo()))
                .andExpect(jsonPath("dataInicio").value(movimentacaoDto.getDataInicio()))
                .andExpect(jsonPath("dataFim").value(movimentacaoDto.getDataFim()))
                .andExpect(jsonPath("conteiner.id").value(movimentacaoDto.getConteinerId()));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar atualizar uma movimentacao pelo id inexistente.")
    public void erroAtualizarMovimentacaoIdInexistente() throws Exception {
//        CENARIO:
//          id: 1l
//          DTO:
//              navio("NORTE SA")
//              tipo("EMBARQUE")
//              conteinerId(1l)
//              dataInicio("2019-02-03 10:08:02")
//              dataFim("2019-02-03 10:08:02")
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(id(2l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Long id = 1l;
        MovimentacaoSalvarTestDTO movimentacaoDto = getMovimentacaoTestDTO();
        String json = new ObjectMapper().writeValueAsString(movimentacaoDto);


        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.empty());

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(MOVIMENTACAO_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

//        VALIDACAO:

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Movimentação não encontrada pelo id informado."));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar atualizar uma movimentacao pelo conteiner id inexistente.")
    public void erroAtualizarMovimentacaoConteinerIdInexistente() throws Exception {
//        CENARIO:
//          id: 1l
//          DTO:
//              navio("NORTE SA")
//              tipo("EMBARQUE")
//              conteinerId(1l)
//              dataInicio("2019-02-03 10:08:02")
//              dataFim("2019-02-03 10:08:02")
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(nulo)
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Long id = 1l;
        MovimentacaoSalvarTestDTO movimentacaoDto = getMovimentacaoTestDTO();
        String json = new ObjectMapper().writeValueAsString(movimentacaoDto);
        Movimentacao movimentacaoSalva = getMovimentacao();


        BDDMockito.given(service.obterPeloId(id)).willReturn(Optional.of(movimentacaoSalva));
        BDDMockito.given(conteinerService.obterPeloId(id)).willReturn(Optional.empty());

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(MOVIMENTACAO_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

//        VALIDACAO:

        mvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Container não encontrado pelo id informado."));
    }

    @Test
    @DisplayName("Deve filtrar movimentacoes")
    public void buscarMovimentacoesComFiltro() throws Exception {
//        CENARIO:
//          QUERY:
//              navio("NORTE SA")
//              tipoMovimentacao("EMBARQUE")
//              conteinerNumero("ABCD1234567")
//              conteinerCliente("t2s")
//              conteinerTipo(20)
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Long id = 1l;

        Movimentacao movimentacao = getMovimentacao();
        movimentacao.setConteiner(Conteiner.builder().id(1l).tipo(20).numero("ABCD1234567").cliente("t2s").categoria(CategoriaConteinerEnum.EXPORTACAO).status(StatusConteinerEnum.VAZIO).build());
        BDDMockito.given(service.buscar(any(MovimentacaoFilterDTO.class), any(Pageable.class)))
                .willReturn(new PageImpl<Movimentacao>(Arrays.asList(movimentacao), PageRequest.of(0, 100), 1));

        String queryString = String.format("?navio=%s&tipoMovimentacao=%s&conteinerNumero=%s&conteinerCliente=%s&conteinerTipo=%s&page=0&size=100", movimentacao.getNavio(), movimentacao.getTipo().toString(), movimentacao.getConteiner().getNumero(), movimentacao.getConteiner().getCliente(), movimentacao.getConteiner().getTipo());

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(MOVIMENTACAO_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

//        VALIDACAO:

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    @Test
    @DisplayName("Deve filtrar movimentacoes sem filtro")
    public void buscarMovimentacoesSemFiltro() throws Exception {
//        CENARIO:
//          QUERY:
//              Nulo
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Long id = 1l;

        Movimentacao movimentacao = getMovimentacao();
        movimentacao.setConteiner(Conteiner.builder().id(1l).tipo(20).numero("ABCD1234567").cliente("t2s").categoria(CategoriaConteinerEnum.EXPORTACAO).status(StatusConteinerEnum.VAZIO).build());
        BDDMockito.given(service.buscar(any(MovimentacaoFilterDTO.class), any(Pageable.class)))
                .willReturn(new PageImpl<Movimentacao>(Arrays.asList(movimentacao), PageRequest.of(0, 100), 1));

        String queryString = String.format("?page=0&size=100");

//        EXECUCAO:

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(MOVIMENTACAO_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

//        VALIDACAO:

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    private MovimentacaoSalvarTestDTO getMovimentacaoTestDTO() {
        return new MovimentacaoSalvarTestDTO(1l, "NORTE SA", "EMBARQUE", 1L, "2019-02-03 10:08:02", "2019-02-03 10:08:02");
    }

    private Conteiner getConteiner() {
        return Conteiner.builder().id(1l).build();
    }

    private MovimentacaoSalvarDTO getMovimentacaoSalvarDTO() {
        return MovimentacaoSalvarDTO.builder()
                .conteinerId(1l)
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusHours(1))
                .navio("NORTE SA")
                .tipo("EMBARQUE")
                .build();
    }

    private Movimentacao getMovimentacao() {
        return Movimentacao.builder()
                .id(1l)
                .conteiner(getConteiner())
                .navio("NORTE SA")
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusHours(1))
                .tipo(TipoMovimentacaoConteiner.EMBARQUE)
                .build();
    }


}
