package com.t2s.conteiner.service;

import com.t2s.conteiner.api.dto.MovimentacaoFilterDTO;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.entity.Movimentacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface MovimentacaoService {

    Movimentacao salvar(Movimentacao movimentacao);

    Optional<Movimentacao> obterPeloId(Long id);

    void remover(Movimentacao movimentacao);

    Movimentacao atualizar(Movimentacao movimentacao);

    Page<Movimentacao> buscar(Movimentacao filter, Pageable pageRequest);
}
