package com.t2s.conteiner.api.dto;

public class MovimentacaoSalvarTestDTO {
    private Long id;
    private String navio;
    private String tipo;
    private Long conteinerId;

    private String dataInicio;
    private String dataFim;

    public MovimentacaoSalvarTestDTO(Long id, String navio, String tipo, Long conteinerId, String dataInicio, String dataFim) {
        this.id = id;
        this.navio = navio;
        this.tipo = tipo;
        this.conteinerId = conteinerId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public MovimentacaoSalvarTestDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNavio() {
        return navio;
    }

    public void setNavio(String navio) {
        this.navio = navio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getConteinerId() {
        return conteinerId;
    }

    public void setConteinerId(Long conteinerId) {
        this.conteinerId = conteinerId;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
}
