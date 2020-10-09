package com.t2s.conteiner.api.dto;

import lombok.*;

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
