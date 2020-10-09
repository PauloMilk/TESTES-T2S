CREATE TABLE if not exists tb_conteiner (
    cd_conteiner int auto_increment,
    cd_numero varchar(11) not null,
    nm_cliente varchar(200) not null,
    nm_status char(10) not null,
    nm_categoria char(12) not null,
    cd_tipo int not null,
    constraint pk_conteiner
		primary key(cd_conteiner),
	constraint uk_numero_conteiner
		unique key(cd_numero)
);