package com.t2s.conteiner.api.controller;

import com.t2s.conteiner.api.dto.ConteinerDTO;
import com.t2s.conteiner.exception.RecursoNaoEncontradoException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
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

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerPeloId(@PathVariable Long id) {
        Conteiner conteiner = service.obterPeloId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Container não encontrado pelo id informado."));
        service.remover(conteiner);
    }

    @PutMapping("{id}")
    public ConteinerDTO atualizar(@PathVariable Long id, @RequestBody @Valid ConteinerDTO dto) {
        Conteiner conteiner = service.obterPeloId(id).orElseThrow( () -> new RecursoNaoEncontradoException("Container não encontrado pelo id informado."));
        conteiner.setCliente(dto.getCliente());
        conteiner.setStatus(StatusConteinerEnum.valueOf(dto.getStatus()));
        conteiner.setCategoria(CategoriaConteinerEnum.valueOf(dto.getCategoria()));
        conteiner.setNumero(dto.getNumero());
        conteiner.setTipo(dto.getTipo());
        conteiner = service.atualizar(conteiner);
        return modelMapper.map(conteiner, ConteinerDTO.class);
    }
}
