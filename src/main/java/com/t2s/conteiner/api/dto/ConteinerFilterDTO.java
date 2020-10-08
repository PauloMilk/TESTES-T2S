package com.t2s.conteiner.api.dto;

import com.t2s.conteiner.helper.annotation.TipoConteinerPattern;
import com.t2s.conteiner.helper.annotation.ValueOfEnum;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import lombok.*;

import javax.validation.constraints.Pattern;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConteinerFilterDTO {

    private String cliente;

    private String numero;

    private Integer tipo;

    private String status;

    private String categoria;
}
