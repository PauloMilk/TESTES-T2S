create table if not exists tb_movimentacao(
    cd_movimentacao int auto_increment,
    nm_navio varchar(50) not null,
    cd_conteiner int not null,
    nm_tipo_movimentacao varchar(30) not null,
    dt_inicio datetime not null,
    dt_fim datetime not null,
    constraint pk_movimentacao
        primary key(cd_movimentacao),
    constraint fk_movimentacao_conteiner
        foreign key(cd_conteiner)
            references tb_conteiner(cd_conteiner)

);

