package com.t2s.conteiner.model.entity;

import com.t2s.conteiner.model.enums.CategoriaConteinerEnum;
import com.t2s.conteiner.model.enums.StatusConteinerEnum;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tb_conteiner")
public class Conteiner {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "cd_conteiner")
    private Long id;

    @Column(name = "nm_cliente")
    private String cliente;

    @Column(name = "cd_numero", unique = true, length = 11)
    private String numero;

    @Column(name = "cd_tipo")
    private Integer tipo;

    @Column(name = "nm_status")
    @Enumerated(EnumType.STRING)
    private StatusConteinerEnum status;

    @Column(name = "nm_categoria")
    @Enumerated(EnumType.STRING)
    private CategoriaConteinerEnum categoria;
}
