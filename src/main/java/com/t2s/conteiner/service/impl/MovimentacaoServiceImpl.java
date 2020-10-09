package com.t2s.conteiner.service.impl;

import com.t2s.conteiner.api.dto.MovimentacaoFilterDTO;
import com.t2s.conteiner.exception.NumeroConteinerException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.entity.Movimentacao;
import com.t2s.conteiner.model.repository.ConteinerRepository;
import com.t2s.conteiner.model.repository.MovimentacaoRepository;
import com.t2s.conteiner.service.MovimentacaoService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
        return repository.save(movimentacao);
    }

    @Override
    public Optional<Movimentacao> obterPeloId(Long id) {
        return repository.findById(id);
    }

    @Override
    public void remover(Movimentacao movimentacao) {
        if(movimentacao == null || movimentacao.getId() == null) {
            throw new IllegalArgumentException("Id da movimentação nulo.");
        }
        repository.delete(movimentacao);
    }

    @Override
    public Movimentacao atualizar(Movimentacao movimentacao) {
        if (movimentacao == null || movimentacao.getId() == null) {
            throw new IllegalArgumentException("Id da movimentação nulo.");
        }
        if (movimentacao.getConteiner() == null || movimentacao.getConteiner().getId() == null) {
            throw new IllegalArgumentException("Id do conteiner nulo.");
        }

        return repository.save(movimentacao);
    }

    @Override
    public Page<Movimentacao> buscar(Movimentacao filter, Pageable pageRequest) {
        ExampleMatcher ex = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Movimentacao> example = Example.of(filter,
                ex
        );
        return repository.findAll(example, pageRequest);
    }
}
