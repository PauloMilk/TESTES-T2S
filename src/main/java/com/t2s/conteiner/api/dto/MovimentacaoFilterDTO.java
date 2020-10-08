package com.t2s.conteiner.api.dto;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovimentacaoFilterDTO {

    private String navio;

    private String tipoMovimentacao;

    private String conteinerNumero;

    private String conteinerCliente;

    private Integer conteinerTipo;

}
