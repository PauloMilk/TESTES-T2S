package com.t2s.conteiner.model.entity;

import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
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
    private Integer tipo;
    private String status;
    private CategoriaConteinerEnum categoria;
}
