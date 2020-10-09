package com.t2s.conteiner.service;

import com.t2s.conteiner.exception.NumeroConteinerException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import com.t2s.conteiner.model.repository.ConteinerRepository;
import com.t2s.conteiner.service.impl.ConteinerServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class ConteinerServiceTest {

    ConteinerService service;

    @MockBean
    ConteinerRepository repository;

    @BeforeEach
    void setUp() {
        this.service = new ConteinerServiceImpl(repository);
    }

    @Test
    @DisplayName("Deve salvar um conteiner")
    public void criarContainerComSucesso() {
//        CENARIO:
//          DTO:
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")


        Conteiner conteiner = getConteiner();
        Conteiner conteinerReposta = getConteinerComId();
        Mockito.when(repository.existsByNumero(conteiner.getNumero())).thenReturn(false);
        Mockito.when(repository.save(conteiner)).thenReturn(conteinerReposta);

//        EXECUCAO:

        Conteiner conteinerSalvo = service.salvar(conteiner);

//        VALIDACAO:

        assertThat(conteinerSalvo.getId()).isNotNull();
        assertThat(conteinerSalvo.getCliente()).isEqualTo(conteiner.getCliente());
        assertThat(conteinerSalvo.getNumero()).isEqualTo(conteiner.getNumero());
        assertThat(conteinerSalvo.getStatus()).isEqualTo(conteiner.getStatus());
        assertThat(conteinerSalvo.getTipo()).isEqualTo(conteiner.getTipo());
        assertThat(conteinerSalvo.getCategoria()).isEqualTo(conteiner.getCategoria());

    }

    @Test
    @DisplayName("Deve lançar erro de negocio ao tentar salvar um conteiner com numero duplicado.")
    public void erroNumeroConteinerDuplicado() {
//        CENARIO:
//          DTO:
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")
//          Conteiner Salvo:
//              id(1l)
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")

        Conteiner conteiner = getConteiner();
        Conteiner conteinerReposta = getConteinerComId();
        Mockito.when(repository.existsByNumero(conteiner.getNumero())).thenReturn(true);

//        EXECUCAO:

        Throwable exception = Assertions.catchThrowable(
                () -> service.salvar(conteiner)
        );

//        VALIDACAO:

        assertThat(exception)
                .isInstanceOf(NumeroConteinerException.class)
                .hasMessage("Número de conteiner já cadastrado");

    }

    @Test
    @DisplayName("Deve obter um conteiner pelo id")
    public void obterConteinerPeloId() {
//        CENARIO:
//          id: 1l
//          Conteiner Salvo:
//              id(1l)
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")

        Long id = 1l;
        Conteiner conteiner = getConteinerComId();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(conteiner));

//        EXECUCAO:

        Optional<Conteiner> conteinerSalvo = service.obterPeloId(id);

//        VALIDACAO:

        assertThat(conteinerSalvo.get().getId()).isEqualTo(id);
        assertThat(conteinerSalvo.get().getCliente()).isEqualTo(conteiner.getCliente());
        assertThat(conteinerSalvo.get().getNumero()).isEqualTo(conteiner.getNumero());
        assertThat(conteinerSalvo.get().getStatus()).isEqualTo(conteiner.getStatus());
        assertThat(conteinerSalvo.get().getTipo()).isEqualTo(conteiner.getTipo());
        assertThat(conteinerSalvo.get().getCategoria()).isEqualTo(conteiner.getCategoria());

    }

    @Test
    @DisplayName("Deve remover um conteiner")
    public void removerConteiner() {
//        CENARIO:
//          Conteiner:
//              id(1l)
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")

        Conteiner conteiner = getConteinerComId();

//        EXECUCAO:

        service.remover(conteiner);

//        VALIDACAO:

        verify(repository, times(1)).delete(conteiner);
    }

    @Test
    @DisplayName("Deve lançar uma exception ao tentar remover um conteiner sem id.")
    public void erroRemoverConteinerPorIdInexistente() {
//        CENARIO:
//          Conteiner:
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")

        Conteiner conteiner = getConteiner();

//        EXECUCAO:

        Throwable exception = Assertions.catchThrowable(() -> service.remover(conteiner));
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id do conteiner nulo.");

//        VALIDACAO:

        verify(repository, Mockito.never()).delete(conteiner);
    }

    @Test
    @DisplayName("Deve atualizar um conteiner")
    public void atualizarContainerComSucesso() {
//        CENARIO:
//          Conteiner Salvo:
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")
//          ConteinerResposta:
//              id(1l)
//              cliente("T2S")
//              numero("ABCD1234568")
//              tipo(40)
//              status("VAZIO")
//              categoria("IMPORTACAO")

        Conteiner conteinerSalvo = getConteinerComId();

        Mockito.when(repository.existsByNumeroAndIdNot(conteinerSalvo.getNumero(), conteinerSalvo.getId())).thenReturn(false);
        Mockito.when(repository.save(conteinerSalvo)).thenReturn(conteinerSalvo);

//        EXECUCAO:

        Conteiner conteinerAtualizado = service.atualizar(conteinerSalvo);

//        VALIDACAO:

        assertThat(conteinerAtualizado.getId()).isNotNull();
        assertThat(conteinerAtualizado.getCliente()).isEqualTo(conteinerSalvo.getCliente());
        assertThat(conteinerAtualizado.getNumero()).isEqualTo(conteinerSalvo.getNumero());
        assertThat(conteinerAtualizado.getStatus()).isEqualTo(conteinerSalvo.getStatus());
        assertThat(conteinerAtualizado.getTipo()).isEqualTo(conteinerSalvo.getTipo());
        assertThat(conteinerAtualizado.getCategoria()).isEqualTo(conteinerSalvo.getCategoria());

    }

    @Test
    @DisplayName("Deve dar erro ao tentar atualizar um conteiner com numero ja existente")
    public void erroAtualizarConteinerNumeroJaExistente() {
//        CENARIO:
//          Conteiner Salvo:
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")
//          ConteinerResposta:
//              id(1l)
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(40)
//              status("VAZIO")
//              categoria("IMPORTACAO")

        Conteiner conteiner = getConteinerComId();
        Mockito.when(repository.existsByNumeroAndIdNot(conteiner.getNumero(), conteiner.getId())).thenReturn(true);

//        EXECUCAO:

        Throwable exception = Assertions.catchThrowable(
                () -> service.atualizar(conteiner)
        );

//        VALIDACAO:

        assertThat(exception)
                .isInstanceOf(NumeroConteinerException.class)
                .hasMessage("Container já cadastrado com esse número.");

        verify(repository, Mockito.never()).delete(conteiner);

    }

    @Test
    @DisplayName("Deve dar erro ao tentar atualizar um conteiner sem id")
    public void erroAtualizarConteinerIdInexistente() {
//        CENARIO:
//          Conteiner:
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")


        Conteiner conteiner = getConteiner();
        Mockito.when(repository.existsByNumeroAndIdNot(conteiner.getNumero(), conteiner.getId())).thenReturn(false);

        Throwable exception = Assertions.catchThrowable(
                () -> service.atualizar(conteiner)
        );

//        EXECUCAO:

        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id do conteiner nulo.");

//        VALIDACAO:

        verify(repository, Mockito.never()).delete(conteiner);

    }

    @Test
    @DisplayName("Deve filtrar conteiners pelas propriedades")
    public void filtrarConteiners() {

//        CENARIO:
//          Conteiner:
//              id(1l)
//              cliente("T2S")
//              numero("ABCD1234567")
//              tipo(20)
//              status("CHEIO")
//              categoria("IMPORTACAO")

        Conteiner conteiner = getConteinerComId();

        Page<Conteiner> page = new PageImpl<Conteiner>(Arrays.asList(conteiner), PageRequest.of(0, 10), 1);
        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

//        EXECUCAO:

        Page<Conteiner> result = service.buscar(conteiner, PageRequest.of(0, 10));

//        VALIDACAO:

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).isEqualTo(Arrays.asList(conteiner));
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }


    private Conteiner getConteiner() {
        return Conteiner.builder()
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo(20)
                .status(StatusConteinerEnum.CHEIO)
                .categoria(CategoriaConteinerEnum.IMPORTACAO)
                .build();
    }

    private Conteiner getConteinerComId() {
        return Conteiner.builder()
                .id(1l)
                .cliente("T2S")
                .numero("ABCD1234567")
                .tipo(20)
                .status(StatusConteinerEnum.CHEIO)
                .categoria(CategoriaConteinerEnum.IMPORTACAO)
                .build();
    }

}
