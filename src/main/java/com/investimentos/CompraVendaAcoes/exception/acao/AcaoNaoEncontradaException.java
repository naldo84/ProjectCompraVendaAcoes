package com.investimentos.CompraVendaAcoes.exception.acao;

public class AcaoNaoEncontradaException extends RuntimeException {
    public AcaoNaoEncontradaException(String message){
        super(message);
    }
}
