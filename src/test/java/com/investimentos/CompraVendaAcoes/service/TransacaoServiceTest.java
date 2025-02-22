package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.TransacaoDto;
import com.investimentos.CompraVendaAcoes.dto.TransacaoResponseDto;
import com.investimentos.CompraVendaAcoes.enums.TipoTransacao;
import com.investimentos.CompraVendaAcoes.exception.transacao.TransacaoSaldoMenorException;
import com.investimentos.CompraVendaAcoes.exception.transacao.TransacaoZeradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.model.TransacaoModel;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.repository.TransacaoRepository;
import com.investimentos.CompraVendaAcoes.service.util.AcaoUtilService;
import com.investimentos.CompraVendaAcoes.service.util.UsuarioUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @InjectMocks
    TransacaoService transacaoService;

    @Mock
    TransacaoRepository transacaoRepository;

    @Mock
    UsuarioUtilService usuarioUtilService;

    @Mock
    AcaoUtilService acaoUtilService;

    private UsuarioModel usuario;
    private AcaoModel acao;
    private TransacaoModel transacaoModel;
    private TransacaoDto transacaoDtoCompra;
    private TransacaoDto transacaoDtoVenda;


    @BeforeEach
    public void setUp(){

        usuario = new UsuarioModel();
        usuario.setId(1L);
        usuario.setCpf("00690022077");
        usuario.setNome("Erinaldo Teste");
        usuario.setIdade(35);
        usuario.setEmail("erinaldo@teste.com.br");

        acao = new AcaoModel();
        acao.setTicker("BBDC3");
        acao.setNomeEmpresa("Banco Bradesco");
        acao.setTipoAcao("Ordinária");
        acao.setSetor("Financeiro");
        acao.setPrecoAtual(BigDecimal.valueOf(10));

        transacaoModel = new TransacaoModel();
        transacaoModel.setId(UUID.fromString("5c963101-6481-4bfd-a04f-5a3a2a551885"));
        transacaoModel.setUsuario(usuario);
        transacaoModel.setAcao(acao);
        transacaoModel.setTipoTransacao(TipoTransacao.COMPRA);
        transacaoModel.setQuantidade(10);
        transacaoModel.setPrecoUnitario(BigDecimal.valueOf(9.50));
        transacaoModel.setValorTotal(BigDecimal.valueOf(95));
        transacaoModel.setDataOperacao(LocalDateTime.now());

       transacaoDtoCompra = new TransacaoDto(
               "00690022077",
               "BBDC3",
               TipoTransacao.COMPRA,
               10,
               BigDecimal.valueOf(10)
       );

        transacaoDtoVenda = new TransacaoDto(
                "00690022077",
                "BBDC3",
                TipoTransacao.VENDA,
                10,
                BigDecimal.valueOf(10)
        );
    }


    @Test
    @DisplayName("Deve efetuar uma compra com sucesso")
    void deveEfetuarCompraComSucesso() {
        when(usuarioUtilService.pesquisarSeUsuarioExiste(usuario.getCpf())).thenReturn(usuario);
        when(acaoUtilService.pesquisarSeAcaoExiste(acao.getTicker())).thenReturn(acao);
        when(transacaoRepository.save(any(TransacaoModel.class))).thenReturn(transacaoModel);

        TransacaoModel resultado = transacaoService.realizarCompra(transacaoDtoCompra);

        assertNotNull(resultado);

        verify(usuarioUtilService, times(1)).pesquisarSeUsuarioExiste(usuario.getCpf());
        verify(acaoUtilService, times(1)).pesquisarSeAcaoExiste(acao.getTicker());
        verify(transacaoRepository, times(1)).save(any(TransacaoModel.class));
    }

    @Test
    @DisplayName("Deve efetuar venda de ação com sucesso")
    void deveEfetuarVendaAcaoComSucesso() {
        when(usuarioUtilService.pesquisarSeUsuarioExiste(usuario.getCpf())).thenReturn(usuario);
        when(acaoUtilService.pesquisarSeAcaoExiste(acao.getTicker())).thenReturn(acao);
        when(transacaoRepository.findByUsuarioAndAcao(usuario, acao)).thenReturn(Collections.singletonList(transacaoModel));
        when(transacaoRepository.save(any(TransacaoModel.class))).thenReturn(transacaoModel);

        TransacaoModel resultado = transacaoService.realizarVenda(transacaoDtoVenda);

        assertNotNull(resultado);

        verify(usuarioUtilService, times(1)).pesquisarSeUsuarioExiste(usuario.getCpf());
        verify(acaoUtilService, times(1)).pesquisarSeAcaoExiste(acao.getTicker());
        verify(transacaoRepository, times(1)).findByUsuarioAndAcao(usuario, acao);
        verify(transacaoRepository, times(1)).save(any(TransacaoModel.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver ações disponíveis para venda")
    void deveLancarExcecaoQuandoNaoHouverAcoesDisponiveisParaVenda() {
        when(usuarioUtilService.pesquisarSeUsuarioExiste(usuario.getCpf()))
                .thenReturn(usuario);
        when(acaoUtilService.pesquisarSeAcaoExiste(acao.getTicker()))
                .thenReturn(acao);
        transacaoModel.setQuantidade(5);
        when(transacaoRepository.findByUsuarioAndAcao(any(UsuarioModel.class), any(AcaoModel.class)))
                .thenReturn(Collections.singletonList(transacaoModel));

        TransacaoSaldoMenorException resultadoException = assertThrows(TransacaoSaldoMenorException.class, () -> {
            transacaoService.realizarVenda(transacaoDtoVenda);
        });

        assertEquals("O usuário não possui ações suficientes para vender!", resultadoException.getMessage());
    }

    @Test
    @DisplayName("Deve consultar todas as compras de um cpf com sucesso")
    void deveConsultarComprasDeUmCpfComSucesso() {
        when(usuarioUtilService.pesquisarSeUsuarioExiste(usuario.getCpf())).thenReturn(usuario);
        when(transacaoRepository.findByUsuarioAndTipoTransacao(usuario, TipoTransacao.COMPRA)).thenReturn(Collections.singletonList(transacaoModel));

        List<TransacaoResponseDto> resultado = transacaoService.consultarCompras(usuario.getCpf());

        assertNotNull(resultado);

        verify(usuarioUtilService, times(1)).pesquisarSeUsuarioExiste(usuario.getCpf());
        verify(transacaoRepository, times(1)).findByUsuarioAndTipoTransacao(usuario, TipoTransacao.COMPRA);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver compras")
    void deveLancarExcecaoQuandoNaoHouverCompras() {
        when(usuarioUtilService.pesquisarSeUsuarioExiste(usuario.getCpf())).thenReturn(usuario);
        when(transacaoRepository.findByUsuarioAndTipoTransacao(usuario, TipoTransacao.COMPRA)).thenReturn(Collections.emptyList());

        TransacaoZeradaException resultadoException = assertThrows(TransacaoZeradaException.class, () -> {
            transacaoService.consultarCompras(usuario.getCpf());
        });

        assertEquals("Não há transações de COMPRA", resultadoException.getMessage());
    }

    @Test
    @DisplayName("Deve consultar todas as vendas de um cpf com sucesso")
    void deveConsultarVendasDeUmCpfComSucesso() {
        when(usuarioUtilService.pesquisarSeUsuarioExiste(usuario.getCpf())).thenReturn(usuario);
        when(transacaoRepository.findByUsuarioAndTipoTransacao(usuario, TipoTransacao.VENDA)).thenReturn(Collections.singletonList(transacaoModel));

        List<TransacaoResponseDto> resultado = transacaoService.consultarVendas(usuario.getCpf());

        assertNotNull(resultado);

        verify(usuarioUtilService, times(1)).pesquisarSeUsuarioExiste(usuario.getCpf());
        verify(transacaoRepository, times(1)).findByUsuarioAndTipoTransacao(usuario, TipoTransacao.VENDA);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não houver vendas")
    void deveLancarExcecaoQuandoNaoHouverVendas() {
        when(usuarioUtilService.pesquisarSeUsuarioExiste(usuario.getCpf())).thenReturn(usuario);
        when(transacaoRepository.findByUsuarioAndTipoTransacao(usuario, TipoTransacao.VENDA)).thenReturn(Collections.emptyList());

        TransacaoZeradaException resultadoException = assertThrows(TransacaoZeradaException.class, () -> {
            transacaoService.consultarVendas(usuario.getCpf());
        });

        assertEquals("Não há transações de VENDA", resultadoException.getMessage());
    }
}