package com.t2s.conteiner.api.controller;

import com.t2s.conteiner.api.dto.MovimentacaoDTO;
import com.t2s.conteiner.api.dto.MovimentacaoFilterDTO;
import com.t2s.conteiner.api.dto.MovimentacaoSalvarDTO;
import com.t2s.conteiner.exception.RecursoNaoEncontradoException;
import com.t2s.conteiner.model.entity.Conteiner;
import com.t2s.conteiner.model.entity.Movimentacao;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import com.t2s.conteiner.model.enums.TipoMovimentacaoConteiner;
import com.t2s.conteiner.service.ConteinerService;
import com.t2s.conteiner.service.MovimentacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("{id}")
    @ApiOperation("Busca um movimentacao pelo id")
    public MovimentacaoDTO obterPeloId(@PathVariable Long id) {
        Movimentacao movimentacao = service.obterPeloId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação não encontrado pelo id informado."));
        return modelMapper.map(movimentacao, MovimentacaoDTO.class);
    }

    @DeleteMapping("{id}")
    @ApiOperation("Remove uma movimentacao pelo id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerPeloId(@PathVariable Long id) {
        Movimentacao movimentacao = service.obterPeloId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação não encontrado pelo id informado."));
        service.remover(movimentacao);
    }

    @PutMapping("{id}")
    @ApiOperation("Atualiza uma movimentacao pelo id")
    public MovimentacaoDTO atualizar(@PathVariable Long id, @RequestBody @Valid MovimentacaoSalvarDTO dto) {
        Movimentacao movimentacao = service.obterPeloId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Movimentação não encontrada pelo id informado."));
        Conteiner conteiner = conteinerService.obterPeloId(dto.getConteinerId()).orElseThrow(() -> new RecursoNaoEncontradoException("Container não encontrado pelo id informado."));
        movimentacao.setNavio(dto.getNavio());
        movimentacao.setConteiner(conteiner);
        movimentacao.setDataInicio(dto.getDataInicio());
        movimentacao.setDataFim(dto.getDataFim());
        movimentacao.setTipo(TipoMovimentacaoConteiner.valueOf(dto.getTipo()));
        movimentacao = service.atualizar(movimentacao);
        return modelMapper.map(movimentacao, MovimentacaoDTO.class);
    }

    @GetMapping
    @ApiOperation("Busca movimentacoes pelo filtro")
    public PageImpl<MovimentacaoDTO> buscar( MovimentacaoFilterDTO dto, Pageable pageRequest) {
        Movimentacao filter = modelMapper.map(dto, Movimentacao.class);
        Page<Movimentacao> result = service.buscar(filter, pageRequest);
        List<MovimentacaoDTO> list = result.getContent().stream()
                .map(entity -> modelMapper.map(entity, MovimentacaoDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<MovimentacaoDTO>(list, pageRequest, result.getTotalElements());
    }
}
