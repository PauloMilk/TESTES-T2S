package com.t2s.conteiner.api.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConteinerDTO {
    private Long id;
    private String cliente;
    private String numero;
    private String tipo;
    private String status;
    private String categoria;
}
