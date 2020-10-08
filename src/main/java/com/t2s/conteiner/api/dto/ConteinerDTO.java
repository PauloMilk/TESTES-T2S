package com.t2s.conteiner.api.dto;

import com.t2s.conteiner.helper.annotation.TipoConteinerPattern;
import com.t2s.conteiner.helper.annotation.ValueOfEnum;
import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConteinerDTO {
    private Long id;

    @NotEmpty
    private String cliente;

    @NotEmpty
    @Pattern(regexp = "[A-z]{4}[0-9]{7}")
    private String numero;

    @NotNull
    @TipoConteinerPattern()
    private Integer tipo;

    @NotEmpty
    private String status;

    @ValueOfEnum(enumClass = CategoriaConteinerEnum.class)
    private String categoria;
}
