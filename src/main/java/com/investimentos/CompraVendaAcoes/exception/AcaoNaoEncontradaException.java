package com.investimentos.CompraVendaAcoes.exception;

public class AcaoNaoEncontradaException extends RuntimeException {
    public AcaoNaoEncontradaException(String message){
        super(message);
    }
}
