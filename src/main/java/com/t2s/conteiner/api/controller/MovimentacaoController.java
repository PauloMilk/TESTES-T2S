package com.t2s.conteiner.api.controller;

import com.t2s.conteiner.api.dto.MovimentacaoDTO;
import com.t2s.conteiner.api.dto.MovimentacaoSalvarDTO;
import com.t2s.conteiner.exception.RecursoNaoEncontradoException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.entity.Movimentacao;
import com.t2s.conteiner.service.ConteinerService;
import com.t2s.conteiner.service.MovimentacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/movimentacoes")
@RequiredArgsConstructor
@Api(tags = {"Movimentacao Controller"})
public class MovimentacaoController {


    private final ConteinerService conteinerService;
    private final MovimentacaoService service;
    private final ModelMapper modelMapper;

    @PostMapping
    @ApiOperation("Cria uma movimentacao")
    @ResponseStatus(HttpStatus.CREATED)
    public MovimentacaoDTO criar(@RequestBody @Valid MovimentacaoSalvarDTO dto) {
        Conteiner conteiner = conteinerService.obterPeloId(dto.getConteinerId()).orElseThrow(() -> new RecursoNaoEncontradoException("Container não encontrado pelo id informado."));
        Movimentacao movimentacao = modelMapper.map(dto, Movimentacao.class);
        movimentacao.setConteiner(conteiner);
        movimentacao = service.salvar(movimentacao);
        return modelMapper.map(movimentacao, MovimentacaoDTO.class);
    }
}
