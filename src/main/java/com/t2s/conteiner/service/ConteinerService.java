package com.t2s.conteiner.service;

import com.t2s.conteiner.model.entity.Conteiner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ConteinerService {

    Conteiner salvar(Conteiner conteiner);

    Optional<Conteiner> obterPeloId(Long id);

    void remover(Conteiner conteiner);

    Conteiner atualizar(Conteiner conteiner);

    Page<Conteiner> buscar(Conteiner filter, Pageable pageRequest);
}
