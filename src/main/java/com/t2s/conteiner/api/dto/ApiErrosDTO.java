package com.t2s.conteiner.api.dto;

import com.t2s.conteiner.exception.NumeroConteinerException;
import com.t2s.conteiner.exception.RecursoNaoEncontradoException;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApiErrosDTO {
    private List<String> errors;

    public ApiErrosDTO(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error -> this.errors.add(error.getDefaultMessage()));
    }


    public ApiErrosDTO(NumeroConteinerException ex) {
        this.errors = Arrays.asList(ex.getMessage());
    }
    public ApiErrosDTO(RecursoNaoEncontradoException ex) {
        this.errors = Arrays.asList(ex.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
