package com.investimentos.CompraVendaAcoes.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UsuarioDto(
        @NotNull(message = "O CPF é obrigatório!")
        @CPF
        @Size(min = 11, max = 11, message = "O CPF deve conter 11 número.s")
        String cpf,

        @NotNull(message = "O nome do usuário é obrigatório!")
        @Size(min = 3, max = 50, message = "O nome deve ter no mínimo 3 e no máximo 50 caracteres.")
        String nome,

        @NotNull(message = "A idade é obrigatória!")
        @Size(min = 18, max = 100, message = "A idade mínima é 18 e a máxima 100.")
        int idade,

        @NotNull(message = "O e-mail é obrigatório!")
        @Email(message = "E-mail inválido!")
        String email

) {

}


