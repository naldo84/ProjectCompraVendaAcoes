package com.investimentos.CompraVendaAcoes.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;


public record AcaoDto(
        @NotNull(message = "O ticker é obrigatório!")
        @Size(min = 5, max = 6)
        String ticker,

        @NotNull(message = "O nome da empresa é obrigatório!")
        @Size(min = 3, max = 50, message = "O nome da empresa deve conter no mínimo 3 e no máximo 50 caracteres.")
        String nomeEmpresa,

        @NotNull(message = "O tipo da ação é obrigatório!")
        String tipoAcao,

        @NotNull(message = "O setor da empresa é obrigatório!")
        @Size(min = 3, max = 25, message = "O setor da empresa deve conter no mínimo 3 e no máximo 25 caracteres.")
        String setor,

        @NotNull(message = "Informar o preço atual da ação é obrigatório!")
        @Positive(message = "O preço da ação deve ser positivo!")
        @Digits(integer = 10, fraction = 2)
        BigDecimal precoAtual) {

}