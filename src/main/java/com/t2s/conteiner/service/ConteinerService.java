package com.t2s.conteiner.service;

import com.t2s.conteiner.model.entity.Conteiner;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ConteinerService {

    Conteiner salvar(Conteiner conteiner);

    Optional<Conteiner> obterPeloId(Long id);

    void remover(Conteiner conteiner);
}
