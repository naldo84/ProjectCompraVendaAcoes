package com.investimentos.CompraVendaAcoes.dto;

import com.investimentos.CompraVendaAcoes.enums.TipoTransacao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransacaoDto(
        @NotNull(message = "O cpf do usuário é obrigatório!")
        String cpfUsuario,

        @NotNull(message = "O ticker é obrigatório!")
        @Size(min = 5, max = 6)
        String ticker,

        @NotNull
        TipoTransacao tipoTransacao,

        @NotNull
        @Positive(message = "A quantidade deve ser maior que um!")
        int quantidade,

        @NotNull(message = "Necessário informar o preço unitário")
        @Positive(message = "O preço unitário deve ser maior que 0.01")
        BigDecimal precoUnitario
) {
}
