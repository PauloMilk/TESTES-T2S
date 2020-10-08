package com.t2s.conteiner.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.t2s.conteiner.api.dto.ConteinerDTO;
import com.t2s.conteiner.api.dto.MovimentacaoDTO;
import com.t2s.conteiner.api.dto.MovimentacaoSalvarDTO;
import com.t2s.conteiner.api.dto.MovimentacaoSalvarTestDTO;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.entity.Movimentacao;
import com.t2s.conteiner.model.enums.TipoMovimentacaoConteiner;
import com.t2s.conteiner.service.ConteinerService;
import com.t2s.conteiner.service.MovimentacaoService;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.t2s.conteiner.api.controller.ConteinerControllerTest.CONTEINER_API;
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
        MovimentacaoSalvarTestDTO movimentacaoDTO = getMovimentacaoTestDTO();
        Movimentacao movimentacaoSalva = getMovimentacao();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        movimentacaoSalva.setDataInicio(LocalDateTime.parse(movimentacaoDTO.getDataInicio(), formatter));
        movimentacaoSalva.setDataFim(LocalDateTime.parse(movimentacaoDTO.getDataFim(), formatter));
        BDDMockito.given(conteinerService.obterPeloId(anyLong())).willReturn(Optional.of(getConteiner()));
        BDDMockito.given(service.salvar(any(Movimentacao.class))).willReturn(movimentacaoSalva);

        String json = new ObjectMapper().writeValueAsString(movimentacaoDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MOVIMENTACAO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

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
        MovimentacaoSalvarTestDTO movimentacaoDTO = getMovimentacaoTestDTO();

        BDDMockito.given(conteinerService.obterPeloId(anyLong())).willReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(movimentacaoDTO);
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MOVIMENTACAO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Container não encontrado pelo id informado."));

    }


    @Test
    @DisplayName("Deve lancar um erro de validacao quando nao houver dados suficientes para criacao de uma movimentacao.")
    public void erroDadosInsuficientes() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new MovimentacaoSalvarTestDTO());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(MOVIMENTACAO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc
                .perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(5)));

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

    private MovimentacaoDTO getMovimentacaoDTO() {
        return MovimentacaoDTO.builder()
                .navio("NORTE SA")
                .tipo("EMBARQUE")
                .conteiner(ConteinerDTO.builder().id(1l).build())
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusMinutes(20))
                .build();
    }
}
