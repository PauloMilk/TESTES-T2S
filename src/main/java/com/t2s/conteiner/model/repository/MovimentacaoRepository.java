package com.t2s.conteiner.model.repository;

import com.t2s.conteiner.model.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
}
