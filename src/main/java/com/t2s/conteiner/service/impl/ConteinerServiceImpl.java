package com.t2s.conteiner.service.impl;

import com.t2s.conteiner.exception.NumeroConteinerException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.repository.ConteinerRepository;
import com.t2s.conteiner.service.ConteinerService;

import java.util.Optional;

public class ConteinerServiceImpl implements ConteinerService {

    private ConteinerRepository repository;

    public ConteinerServiceImpl(ConteinerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Conteiner salvar(Conteiner conteiner) {
        if (repository.existsByNumero(conteiner.getNumero())) {
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
}
