package com.t2s.conteiner.model.repository;

import com.t2s.conteiner.model.entity.Conteiner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConteinerRepository extends JpaRepository<Conteiner, Long> {

    Boolean existsByNumero(String numero);
}
