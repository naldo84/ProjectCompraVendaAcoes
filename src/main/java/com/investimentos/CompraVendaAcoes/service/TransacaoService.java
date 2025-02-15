package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.TransacaoDto;
import com.investimentos.CompraVendaAcoes.dto.TransacaoResponseDto;
import com.investimentos.CompraVendaAcoes.enums.TipoTransacao;
import com.investimentos.CompraVendaAcoes.exception.transacao.TransacaoSaldoMenorException;
import com.investimentos.CompraVendaAcoes.exception.transacao.TransacaoZeradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.model.TransacaoModel;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.repository.AcaoRepository;
import com.investimentos.CompraVendaAcoes.repository.TransacaoRepository;
import com.investimentos.CompraVendaAcoes.repository.UsuarioRepository;

import com.investimentos.CompraVendaAcoes.service.util.AcaoUtilService;
import com.investimentos.CompraVendaAcoes.service.util.UsuarioUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class TransacaoService {
    @Autowired
    TransacaoRepository transacaoRepository;

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioUtilService usuarioUtilService;

    @Autowired
    AcaoUtilService acaoUtilService;

    public TransacaoModel realizarCompra(TransacaoDto transacaoDto) {
        UsuarioModel usuario = usuarioUtilService.pesquisarSeUsuarioExiste(transacaoDto.cpfUsuario());
        AcaoModel acao = acaoUtilService.pesquisarSeAcaoExiste(transacaoDto.ticker());

        TransacaoModel transacao = new TransacaoModel();
        transacao.setUsuario(usuario);
        transacao.setAcao(acao);
        transacao.setTipoTransacao(transacaoDto.tipoTransacao());
        transacao.setQuantidade(transacaoDto.quantidade());
        transacao.setPrecoUnitario(transacaoDto.precoUnitario());
        BigDecimal valorTotal = transacao.getPrecoUnitario().multiply(BigDecimal.valueOf(transacao.getQuantidade()));
        transacao.setValorTotal(valorTotal);
        transacao.setDataOperacao(LocalDateTime.now());

        return transacaoRepository.save(transacao);
    }

    public TransacaoModel realizarVenda(TransacaoDto transacaoDto) {
        UsuarioModel usuario = usuarioUtilService.pesquisarSeUsuarioExiste(transacaoDto.cpfUsuario());
        AcaoModel acao = acaoUtilService.pesquisarSeAcaoExiste(transacaoDto.ticker());

        //consulta das ações de um usuário
        List<TransacaoModel> transacoesDoUsuario = transacaoRepository.findByUsuarioAndAcao(usuario, acao);

        //calculo de ações disponíveis para venda

        //Cálculo do total de ações Compradas
        int totalAcoesCompradas = transacoesDoUsuario.stream()
                .filter(transacao -> transacao.getTipoTransacao() == TipoTransacao.COMPRA)
                .mapToInt(TransacaoModel::getQuantidade)
                .sum();

        //Cálculo do total de ações vendidas
        int totalAcoesVendidas = transacoesDoUsuario.stream()
                .filter(transacao -> transacao.getTipoTransacao() == TipoTransacao.VENDA)
                .mapToInt(TransacaoModel::getQuantidade)
                .sum();

        int saldoAcoes = totalAcoesCompradas - totalAcoesVendidas;

        if (transacaoDto.quantidade() > saldoAcoes){
            throw new TransacaoSaldoMenorException("O usuário não possui ações suficientes para vender!");
        }

        TransacaoModel transacao = new TransacaoModel();
        transacao.setUsuario(usuario);
        transacao.setAcao(acao);
        transacao.setTipoTransacao(TipoTransacao.VENDA);
        transacao.setQuantidade(transacaoDto.quantidade());
        transacao.setPrecoUnitario(transacaoDto.precoUnitario());
        BigDecimal valorTotal = transacao.getPrecoUnitario().multiply(BigDecimal.valueOf(transacao.getQuantidade()));
        transacao.setValorTotal(valorTotal);
        transacao.setDataOperacao(LocalDateTime.now());

        return transacaoRepository.save(transacao);
    }

    public List<TransacaoResponseDto> consultarCompras(String cpf){
        UsuarioModel usuario = usuarioUtilService.pesquisarSeUsuarioExiste(cpf);
        List<TransacaoModel> compras = transacaoRepository.findByUsuarioAndTipoTransacao(usuario, TipoTransacao.COMPRA);

        if (compras.isEmpty()){
            throw new TransacaoZeradaException("Não há transações de " + TipoTransacao.COMPRA);
        }

        return compras.stream()
                .map(TransacaoResponseDto::fromModel)
                .toList();
    }

    public List<TransacaoResponseDto> consultarVendas(String cpf){
        UsuarioModel usuario = usuarioUtilService.pesquisarSeUsuarioExiste(cpf);
        List<TransacaoModel> vendas = transacaoRepository.findByUsuarioAndTipoTransacao(usuario, TipoTransacao.VENDA);

        if (vendas.isEmpty()){
            throw new TransacaoZeradaException("Não há transações de " + TipoTransacao.VENDA );
        }

        return vendas.stream()
                .map(TransacaoResponseDto::fromModel)
                .toList();
    }
}
