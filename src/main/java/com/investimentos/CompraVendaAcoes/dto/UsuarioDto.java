package com.investimentos.CompraVendaAcoes.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioDto(
        @NotNull(message = "O CPF é obrigatório!")
        @CPF
        @Size(min = 11, max = 11, message = "O CPF deve conter 11 números")
        String cpf,

        @NotNull(message = "O nome do usuário é obrigatório!")
        @Size(min = 3, max = 50, message = "O nome deve ter no mínimo 3 e no máximo 50 caracteres.")
        String nome,

        @NotNull(message = "A idade é obrigatória!")
        @Min(18)
        @Max(120)
        int idade,

        @NotNull(message = "O e-mail é obrigatório!")
        @Email(message = "E-mail inválido!")
        String email) {
}