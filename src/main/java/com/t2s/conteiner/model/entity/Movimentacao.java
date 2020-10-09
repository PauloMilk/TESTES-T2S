package com.t2s.conteiner.model.entity;

import com.t2s.conteiner.model.enums.TipoMovimentacaoConteiner;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "cd_movimentacao")
    private Long id;

    @Column(name = "nm_navio")
    private String navio;

    @ManyToOne()
    @JoinColumn(name = "cd_conteiner", referencedColumnName = "cd_conteiner")
    private Conteiner conteiner;

    @Enumerated(EnumType.STRING)
    @Column(name = "nm_tipo_movimentacao")
    private TipoMovimentacaoConteiner tipo;

    @Column(name = "dt_inicio")
    private LocalDateTime dataInicio;

    @Column(name = "dt_fim")
    private LocalDateTime dataFim;

}
