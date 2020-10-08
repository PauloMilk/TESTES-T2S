package com.t2s.conteiner.model.entity;

import com.t2s.conteiner.model.enums.TipoMovimentacaoConteiner;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movimentacao {
    private Long id;
    private String navio;
    private Conteiner conteiner;
    private TipoMovimentacaoConteiner tipo;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;

}
