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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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

        Conteiner conteiner = getConteiner();
        Conteiner conteinerReposta = getConteinerComId();
        Mockito.when(repository.existsByNumero(conteiner.getNumero())).thenReturn(false);
        Mockito.when(repository.save(conteiner)).thenReturn(conteinerReposta);
        Conteiner conteinerSalvo = service.salvar(conteiner);

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

        Conteiner conteiner = getConteiner();
        Conteiner conteinerReposta = getConteinerComId();
        Mockito.when(repository.existsByNumero(conteiner.getNumero())).thenReturn(true);
        Throwable exception = Assertions.catchThrowable(
                () -> service.salvar(conteiner)
        );

        assertThat(exception)
                .isInstanceOf(NumeroConteinerException.class)
                .hasMessage("Número de conteiner já cadastrado");

    }

    @Test
    @DisplayName("Deve obter um conteiner pelo id")
    public void obterConteinerPeloId() {
        Long id = 1l;
        Conteiner conteiner = getConteinerComId();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(conteiner));
        Optional<Conteiner> conteinerSalvo = service.obterPeloId(id);

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
        Conteiner conteiner = getConteinerComId();
        service.remover(conteiner);

        verify(repository, times(1)).delete(conteiner);
    }

    @Test
    @DisplayName("Deve lançar uma exception ao tentar remover um conteiner sem id.")
    public void erroRemoverConteinerPorIdInexistente() {
        Conteiner conteiner = getConteiner();
        Throwable exception = Assertions.catchThrowable(() -> service.remover(conteiner));
        assertThat(exception)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Id do conteiner nulo.");

        verify(repository, Mockito.never()).delete(conteiner);
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
