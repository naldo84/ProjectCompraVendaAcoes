package com.investimentos.CompraVendaAcoes.exception.usuario;

public class UsuarioNaoEncontradoException extends RuntimeException{
    public UsuarioNaoEncontradoException(String message){
        super(message);
    }
}
