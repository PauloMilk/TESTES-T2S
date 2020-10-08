package com.t2s.conteiner.service;

import com.t2s.conteiner.model.entity.Movimentacao;
import org.springframework.stereotype.Service;

@Service
public interface MovimentacaoService {

    Movimentacao salvar(Movimentacao movimentacao);
}
