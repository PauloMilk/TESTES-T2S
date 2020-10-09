package com.t2s.conteiner.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.t2s.conteiner.helper.annotation.ValueOfEnum;
import com.t2s.conteiner.model.enums.TipoMovimentacaoConteiner;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MovimentacaoSalvarDTO {

    @NotEmpty
    private String navio;

    @ValueOfEnum(enumClass = TipoMovimentacaoConteiner.class)
    private String tipo;

    @NotNull
    private Long conteinerId;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataInicio;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataFim;
}
