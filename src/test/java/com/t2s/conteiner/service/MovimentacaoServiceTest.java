package com.t2s.conteiner.service;


import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import com.t2s.conteiner.model.repository.ConteinerRepository;
import com.t2s.conteiner.model.repository.MovimentacaoRepository;
import com.t2s.conteiner.service.impl.ConteinerServiceImpl;
import com.t2s.conteiner.service.impl.MovimentacaoServiceImpl;
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
public class MovimentacaoServiceTest {

    MovimentacaoService service;

    @MockBean
    MovimentacaoRepository repository;

    @BeforeEach
    void setUp() {
        this.service = new MovimentacaoServiceImpl(repository) {
        };
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
