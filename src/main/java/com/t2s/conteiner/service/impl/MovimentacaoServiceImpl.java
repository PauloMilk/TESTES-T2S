package com.t2s.conteiner.service.impl;

import com.t2s.conteiner.api.dto.MovimentacaoFilterDTO;
import com.t2s.conteiner.model.entity.Movimentacao;
import com.t2s.conteiner.model.repository.ConteinerRepository;
import com.t2s.conteiner.model.repository.MovimentacaoRepository;
import com.t2s.conteiner.service.MovimentacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MovimentacaoServiceImpl implements MovimentacaoService {

    private MovimentacaoRepository repository;

    public MovimentacaoServiceImpl(MovimentacaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Movimentacao salvar(Movimentacao movimentacao) {
        return null;
    }

    @Override
    public Optional<Movimentacao> obterPeloId(Long id) {
        return Optional.empty();
    }

    @Override
    public void remover(Movimentacao movimentacao) {

    }

    @Override
    public Movimentacao atualizar(Movimentacao movimentacao) {
        return null;
    }

    @Override
    public Page<Movimentacao> buscar(MovimentacaoFilterDTO filter, Pageable pageRequest) {
        return null;
    }
}
