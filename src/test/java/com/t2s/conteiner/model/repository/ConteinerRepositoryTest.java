package com.t2s.conteiner.model.repository;

import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class ConteinerRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    ConteinerRepository repository;

    @Test
    @DisplayName("Deve retornar verdadeiro quando existir um conteiner na base com numero informado.")
    public void verdadeiroQuandoExistir() {

        String numero = "ABCD1234567";
        Conteiner conteiner = getConteiner();
        entityManager.persist(conteiner);
        boolean exists = repository.existsByNumero(numero);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando nao existir um conteiner na base com numero informado.")
    public void falsoQuandoNaoExistir() {

        String numero = "ABCD1234567";
        boolean exists = repository.existsByNumero(numero);

        assertThat(exists).isFalse();
    }


    @Test
    @DisplayName("Deve retornar verdadeiro quando nao existir um conteiner na base com numero informado e id.")
    public void verdadeiroQuandoExistirNumeroEId() {

        String numero = "ABCD1234567";
        Conteiner conteiner = getConteiner();
        Long id = (Long) entityManager.persistAndGetId(conteiner);
        boolean exists = repository.existsByNumeroAndIdNot(numero, id+1);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar falso quando existir um conteiner na base com numero informado e id.")
    public void falsoQuandoNaoExistirNumeroEId() {

        String numero = "ABCD1234567";
        boolean exists = repository.existsByNumeroAndIdNot(numero, 1l);

        assertThat(exists).isFalse();
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

}
