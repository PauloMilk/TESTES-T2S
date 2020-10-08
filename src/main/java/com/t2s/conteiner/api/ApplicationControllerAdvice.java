package com.t2s.conteiner.api;

import com.t2s.conteiner.api.dto.ApiErrosDTO;
import com.t2s.conteiner.exception.NumeroConteinerException;
import com.t2s.conteiner.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrosDTO handleValidationException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ApiErrosDTO(bindingResult);
    }

    @ExceptionHandler(NumeroConteinerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrosDTO handleNumeroConteinerException(NumeroConteinerException ex) {
        return new ApiErrosDTO(ex);
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrosDTO handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        return new ApiErrosDTO(ex);
    }
}
