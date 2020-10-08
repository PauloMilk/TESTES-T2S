package com.t2s.conteiner.api.controller;

import com.t2s.conteiner.api.dto.ConteinerDTO;
import com.t2s.conteiner.exception.RecursoNaoEncontradoException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.service.ConteinerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/conteineres")
@RequiredArgsConstructor
public class ConteinerController {

    private final ConteinerService service;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConteinerDTO criar(@RequestBody @Valid ConteinerDTO dto) {
        Conteiner entity = modelMapper.map(dto, Conteiner.class);
        entity = service.salvar(entity);
        return modelMapper.map(entity, ConteinerDTO.class);
    }

    @GetMapping("{id}")
    public ConteinerDTO obterPeloId(@PathVariable Long id) {
        Conteiner conteiner = service.obterPeloId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Container não encontrado pelo id informado."));
        return modelMapper.map(conteiner, ConteinerDTO.class);
    }
}
