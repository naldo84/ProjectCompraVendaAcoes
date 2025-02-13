package com.investimentos.CompraVendaAcoes.dto;

import com.investimentos.CompraVendaAcoes.enums.TipoTransacao;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoDto(
        @NotNull UsuarioModel usuario,
        @NotNull AcaoModel acao,

        @NotNull
        TipoTransacao tipoTransacao,

        @NotNull
        @Positive
        int quantidade,


        @NotNull(message = "Necessário informar o preço unitário")
        @Positive
        BigDecimal precoUnitario,

        @NotNull
        LocalDateTime dataOperacao
) {
}
