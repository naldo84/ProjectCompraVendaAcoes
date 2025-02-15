package com.investimentos.CompraVendaAcoes.dto;

import com.investimentos.CompraVendaAcoes.enums.TipoTransacao;
import com.investimentos.CompraVendaAcoes.model.TransacaoModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoResponseDto(
        UUID id,
        Long idUsuario,
        String ticker,
        TipoTransacao tipoTransacao,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal valorTotal,
        LocalDateTime dataOeracao
) {
    public static TransacaoResponseDto fromModel(TransacaoModel transacao){
        return new TransacaoResponseDto(
                transacao.getId(),
                transacao.getUsuario().getId(),
                transacao.getAcao().getTicker(),
                transacao.getTipoTransacao(),
                transacao.getQuantidade(),
                transacao.getPrecoUnitario(),
                transacao.getValorTotal(),
                transacao.getDataOperacao()
        );
    }
}
