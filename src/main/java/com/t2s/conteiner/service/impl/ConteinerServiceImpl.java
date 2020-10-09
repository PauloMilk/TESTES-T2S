package com.t2s.conteiner.service.impl;

import com.t2s.conteiner.exception.NumeroConteinerException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.repository.ConteinerRepository;
import com.t2s.conteiner.service.ConteinerService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConteinerServiceImpl implements ConteinerService {

    private ConteinerRepository repository;

    public ConteinerServiceImpl(ConteinerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Conteiner salvar(Conteiner conteiner) {
        boolean existsByNumero = repository.existsByNumero(conteiner.getNumero());
        if (existsByNumero) {
            throw new NumeroConteinerException("Número de conteiner já cadastrado");
        }
        return repository.save(conteiner);
    }

    @Override
    public Optional<Conteiner> obterPeloId(Long id) {
        return repository.findById(id);
    }

    @Override
    public void remover(Conteiner conteiner) {
        if (conteiner == null || conteiner.getId() == null) {
            throw new IllegalArgumentException("Id do conteiner nulo.");
        }
        repository.delete(conteiner);
    }

    @Override
    public Conteiner atualizar(Conteiner conteiner) {
        if (conteiner == null || conteiner.getId() == null) {
            throw new IllegalArgumentException("Id do conteiner nulo.");
        }
        boolean existsByNumeroAndIdNot = repository.existsByNumeroAndIdNot(conteiner.getNumero(), conteiner.getId());
        if (existsByNumeroAndIdNot) {
            throw new NumeroConteinerException("Container já cadastrado com esse número.");
        }

        return repository.save(conteiner);
    }

    @Override
    public Page<Conteiner> buscar(Conteiner filter, Pageable pageRequest) {
        ExampleMatcher ex = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Conteiner> example = Example.of(filter,
                ex
        );
        return repository.findAll(example, pageRequest);
    }
}
