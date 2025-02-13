package com.investimentos.CompraVendaAcoes.exception.usuario;

public class UsuarioNaoEncontrado extends RuntimeException{
    public UsuarioNaoEncontrado(String message){
        super(message);
    }
}
