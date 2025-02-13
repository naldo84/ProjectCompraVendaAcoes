package com.investimentos.CompraVendaAcoes.model;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "tb_acao")
public class AcaoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticker;
    private String nomeEmpresa;
    private String tipoAcao;
    private String setor;
    private BigDecimal precoAtual;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(String tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public BigDecimal getPrecoAtual() {
        return precoAtual;
    }

    public void setPrecoAtual(BigDecimal precoAtual) {
        this.precoAtual = precoAtual;
    }
}
