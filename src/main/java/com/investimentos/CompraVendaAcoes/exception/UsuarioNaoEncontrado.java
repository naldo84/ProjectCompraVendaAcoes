package com.investimentos.CompraVendaAcoes.exception;

public class UsuarioNaoEncontrado extends RuntimeException{
    public UsuarioNaoEncontrado(String message){
        super(message);
    }
}
