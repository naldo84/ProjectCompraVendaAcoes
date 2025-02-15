package com.investimentos.CompraVendaAcoes.exception.transacao;

public class TransacaoZeradaException extends RuntimeException{
    public TransacaoZeradaException (String message){
        super(message);
    }
}
