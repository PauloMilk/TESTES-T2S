package com.t2s.conteiner.service;


import com.t2s.conteiner.api.dto.ConteinerDTO;
import com.t2s.conteiner.api.dto.MovimentacaoDTO;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.entity.Movimentacao;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import com.t2s.conteiner.model.enums.TipoMovimentacaoConteiner;
import com.t2s.conteiner.model.repository.ConteinerRepository;
import com.t2s.conteiner.model.repository.MovimentacaoRepository;
import com.t2s.conteiner.service.impl.ConteinerServiceImpl;
import com.t2s.conteiner.service.impl.MovimentacaoServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class MovimentacaoServiceTest {

    MovimentacaoService service;

    @MockBean
    MovimentacaoRepository repository;

    @BeforeEach
    void setUp() {
        this.service = new MovimentacaoServiceImpl(repository) {
        };
    }


    @Test
    @DisplayName("Deve salvar uma movimentacao")
    public void criarMovimentacaoComSucesso() {
//        CENARIO:
//          Movimentacao:
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)
//          Movimentacao Salva:
//              id(1l)
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Movimentacao movimentacao = getMovimentacao();
        Movimentacao movimentacaoComId = getMovimentacaoComId();
        Mockito.when(repository.save(movimentacao))
                .thenReturn(movimentacaoComId);

//        EXECUCAO

        Movimentacao movimentacaoSalva = service.salvar(movimentacao);

//        VALIDACAO

        assertThat(movimentacaoSalva.getId()).isNotNull();
        assertThat(movimentacaoSalva.getConteiner().getId()).isEqualTo(movimentacao.getConteiner().getId());
        assertThat(movimentacaoSalva.getNavio()).isEqualTo(movimentacao.getNavio());
        assertThat(movimentacaoSalva.getDataFim()).isEqualTo(movimentacaoComId.getDataFim());
        assertThat(movimentacaoSalva.getDataInicio()).isEqualTo(movimentacaoComId.getDataInicio());
        assertThat(movimentacaoSalva.getTipo()).isEqualTo(movimentacaoComId.getTipo());

    }


    @Test
    @DisplayName("Deve obter uma movimentacao pelo id")
    public void obterMovimentacaoPeloId() {
//        CENARIO:
//          id(1l)
//          Movimentacao Salva:
//              id(1l)
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Movimentacao movimentacaoComId = getMovimentacaoComId();
        Mockito.when(repository.findById(movimentacaoComId.getId()))
                .thenReturn(Optional.of(movimentacaoComId));

//        EXECUCAO

        Optional<Movimentacao> movimentacao = service.obterPeloId(movimentacaoComId.getId());

//        VALIDACAO

        assertThat(movimentacao.get().getId()).isNotNull();
        assertThat(movimentacao.get().getConteiner().getId()).isEqualTo(movimentacaoComId.getConteiner().getId());
        assertThat(movimentacao.get().getNavio()).isEqualTo(movimentacaoComId.getNavio());
        assertThat(movimentacao.get().getDataFim()).isEqualTo(movimentacaoComId.getDataFim());
        assertThat(movimentacao.get().getDataInicio()).isEqualTo(movimentacaoComId.getDataInicio());
        assertThat(movimentacao.get().getTipo()).isEqualTo(movimentacaoComId.getTipo());

    }

    @Test
    @DisplayName("Deve remover uma movimentacao")
    public void removerMovimentacao() {
//        CENARIO:
//          id(1l)
//          Movimentacao Salva:
//              id(1l)
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Movimentacao movimentacao = getMovimentacaoComId();

//        EXECUCAO:

        service.remover(movimentacao);

//        VALIDACAO:

        verify(repository, times(1)).delete(movimentacao);
    }

    @Test
    @DisplayName("Deve lançar uma exception ao tentar remover uma movimentacao sem id.")
    public void erroRemoverMovimentacaoSemId() {
//        CENARIO:
//          id(1l)
//          Movimentacao Salva:
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)

        Movimentacao movimentacao = getMovimentacao();

//        EXECUCAO:

        Throwable exception = Assertions.catchThrowable(() -> service.remover(movimentacao));
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id da movimentação nulo.");

//        VALIDACAO:

        verify(repository, never()).delete(movimentacao);
    }


    @Test
    @DisplayName("Deve atualizar uma movimentacao")
    public void atualizarMovimentacaoComSucesso() {
//        CENARIO:
//          Movimentacao:
//              id(1l)
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)
//          Movimentacao Salva:
//              id(1l)
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)
        Movimentacao movimentacao = getMovimentacaoComId();
        Movimentacao movimentacaoSalva = getMovimentacaoComId();

        Mockito.when(repository.save(movimentacao)).thenReturn(movimentacaoSalva);

//        EXECUCAO:

        Movimentacao movimentacaoAtualizada = service.atualizar(movimentacao);

//        VALIDACAO:

        assertThat(movimentacaoAtualizada.getId()).isEqualTo(movimentacao.getId());
        assertThat(movimentacaoAtualizada.getConteiner().getId()).isEqualTo(movimentacao.getConteiner().getId());
        assertThat(movimentacaoAtualizada.getNavio()).isEqualTo(movimentacao.getNavio());
        assertThat(movimentacaoAtualizada.getDataFim().toString()).isEqualTo(movimentacaoSalva.getDataFim().toString());
        assertThat(movimentacaoAtualizada.getDataInicio()).isEqualTo(movimentacaoSalva.getDataInicio().toString());
        assertThat(movimentacaoAtualizada.getTipo()).isEqualTo(movimentacao.getTipo());

    }

    @Test
    @DisplayName("Deve dar erro ao tentar atualizar uma movimentacao sem id")
    public void erroAtualizarConteinerIdInexistente() {
//        CENARIO:
//          Movimentacao:
//              id(1l)
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)
//          Movimentacao Salva:
//              id(1l)
//              navio("NORTE SA")
//              container(id(1l))
//              dataInicio(LocalDateTime.now())
//              dataFim(LocalDateTime.now().plusHours(1))
//              tipo(TipoMovimentacaoConteiner.EMBARQUE)


        Movimentacao movimentacao = getMovimentacao();

        Throwable exception = Assertions.catchThrowable(
                () -> service.atualizar(movimentacao)
        );

//        EXECUCAO:

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id da movimentação nulo.");

//        VALIDACAO:

        verify(repository, Mockito.never()).delete(movimentacao);

    }


    private Movimentacao getMovimentacao() {
        return Movimentacao.builder()
                .conteiner(getConteiner())
                .navio("NORTE SA")
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusHours(1))
                .tipo(TipoMovimentacaoConteiner.EMBARQUE)
                .build();
    }

    private Movimentacao getMovimentacaoComId() {
        return Movimentacao.builder()
                .id(1l)
                .conteiner(getConteiner())
                .navio("NORTE SA")
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusHours(1))
                .tipo(TipoMovimentacaoConteiner.EMBARQUE)
                .build();
    }

    private Conteiner getConteiner() {
        return Conteiner.builder().id(1l).build();
    }

    private MovimentacaoDTO getMovimentacaoDTO() {
        return MovimentacaoDTO.builder()
                .navio("NORTE SA")
                .tipo("EMBARQUE")
                .dataInicio(LocalDateTime.now())
                .dataFim(LocalDateTime.now().plusHours(1))
                .conteiner(getConteinerDTO())
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

//
//
//    @Test
//    @DisplayName("Deve salvar uma movimentacao")
//    public void criarContainerComSucesso() {
//
//        Conteiner conteiner = getConteiner();
//        Mockito.when(repository.existsByNumero(conteiner.getNumero())).thenReturn(false);
//        Mockito.when(repository.save(conteiner)).thenReturn(conteinerReposta);
//        Conteiner conteinerSalvo = service.salvar(conteiner);
//
//        assertThat(conteinerSalvo.getId()).isNotNull();
//        assertThat(conteinerSalvo.getCliente()).isEqualTo(conteiner.getCliente());
//        assertThat(conteinerSalvo.getNumero()).isEqualTo(conteiner.getNumero());
//        assertThat(conteinerSalvo.getStatus()).isEqualTo(conteiner.getStatus());
//        assertThat(conteinerSalvo.getTipo()).isEqualTo(conteiner.getTipo());
//        assertThat(conteinerSalvo.getCategoria()).isEqualTo(conteiner.getCategoria());
//
//    }
//
//    private Conteiner getConteiner() {
//        return Conteiner.builder()
//                .id(1l)
//                .cliente("T2S")
//                .numero("ABCD1234567")
//                .tipo(20)
//                .status(StatusConteinerEnum.CHEIO)
//                .categoria(CategoriaConteinerEnum.IMPORTACAO)
//                .build();
//    }
}
