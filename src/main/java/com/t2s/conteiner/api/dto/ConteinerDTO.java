package com.t2s.conteiner.api.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
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
    @NotEmpty @Pattern(regexp = "[A-z]{4}[0-9]{7}")
    private String numero;
    @NotEmpty
    private String tipo;
    @NotEmpty
    private String status;
    @NotEmpty
    private String categoria;
}
