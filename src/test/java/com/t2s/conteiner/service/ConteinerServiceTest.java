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

import static org.assertj.core.api.Assertions.assertThat;

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
