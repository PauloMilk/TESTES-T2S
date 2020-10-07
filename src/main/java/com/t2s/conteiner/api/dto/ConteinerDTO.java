package com.t2s.conteiner.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

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
    private String numero;
    @NotEmpty
    private String tipo;
    @NotEmpty
    private String status;
    @NotEmpty
    private String categoria;
}
