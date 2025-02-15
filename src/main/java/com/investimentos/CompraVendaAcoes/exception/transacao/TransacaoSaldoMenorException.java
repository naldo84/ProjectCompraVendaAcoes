package com.investimentos.CompraVendaAcoes.exception.transacao;

public class TransacaoSaldoMenorException extends RuntimeException{
    public TransacaoSaldoMenorException(String message){
        super(message);
    }
}
