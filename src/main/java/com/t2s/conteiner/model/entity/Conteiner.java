package com.t2s.conteiner.model.entity;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Conteiner {
    private Long id;
    private String cliente;
    private String numero;
    private String tipo;
    private String status;
    private String categoria;
}
