package com.t2s.conteiner.api.controller;

import com.t2s.conteiner.api.dto.ConteinerDTO;
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
    public ConteinerDTO criar(@RequestBody ConteinerDTO dto) {
        Conteiner entity = modelMapper.map(dto, Conteiner.class);
        entity = service.salvar(entity);
        return modelMapper.map(entity, ConteinerDTO.class);
    }
}
