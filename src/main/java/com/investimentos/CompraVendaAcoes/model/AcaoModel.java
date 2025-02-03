package com.investimentos.CompraVendaAcoes.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "tb_acao")
public class AcaoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    @Size(min = 5, max = 6)
    private String ticker;

    @NotNull
    @Size(min = 1, max = 100)
    private String nome;

    @NotNull
    @Size(min = 3, max = 15)
    private String setor;

    @NotNull
    private double preco;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public @NotNull @Size(min = 5, max = 6) String getTicker() {
        return ticker;
    }

    public void setTicker(@NotNull @Size(min = 5, max = 6) String ticker) {
        this.ticker = ticker;
    }

    public @NotNull @Size(min = 1, max = 100) String getNome() {
        return nome;
    }

    public void setNome(@NotNull @Size(min = 1, max = 100) String nome) {
        this.nome = nome;
    }

    public @NotNull @Size(min = 3, max = 15) String getSetor() {
        return setor;
    }

    public void setSetor(@NotNull @Size(min = 3, max = 15) String setor) {
        this.setor = setor;
    }

    @NotNull
    public double getPreco() {
        return preco;
    }

    public void setPreco(@NotNull double preco) {
        this.preco = preco;
    }
}
