package com.investimentos.CompraVendaAcoes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.investimentos.CompraVendaAcoes.dto.TransacaoDto;
import com.investimentos.CompraVendaAcoes.dto.TransacaoResponseDto;
import com.investimentos.CompraVendaAcoes.enums.TipoTransacao;
import com.investimentos.CompraVendaAcoes.exception.Error;
import com.investimentos.CompraVendaAcoes.exception.GlobalExceptionHandler;
import com.investimentos.CompraVendaAcoes.exception.transacao.TransacaoSaldoMenorException;
import com.investimentos.CompraVendaAcoes.exception.transacao.TransacaoZeradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.model.TransacaoModel;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.service.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransacaoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @InjectMocks
    TransacaoController transacaoController;

    @Mock
    TransacaoService transacaoService;

    private TransacaoDto transacaoDtoCompra;
    private TransacaoModel transacaoModel;
    private TransacaoResponseDto  transacaoResponseDto;
    private ObjectMapper objectMapper;
    private UsuarioModel usuarioModel;
    private AcaoModel acaoModel;
    private TransacaoDto transacaoDtoVenda;
    private List<TransacaoResponseDto> transacaoResponseDtoCompra;
    private List<TransacaoResponseDto> transacaoResponseDtoVenda;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .standaloneSetup(transacaoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        //objectMapper = new ObjectMapper();

        //configuração do object mapper para suportar LocalDateTimee
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        transacaoDtoCompra = new TransacaoDto(
                "00690022077",
                "ITSA3",
                TipoTransacao.COMPRA,
                10,
                BigDecimal.valueOf(9.5)
        );

        transacaoDtoVenda = new TransacaoDto(
                "00690022077",
                "ITSA3",
                TipoTransacao.VENDA,
                10,
                BigDecimal.valueOf(9.5)
        );



        usuarioModel = new UsuarioModel();
        usuarioModel.setId(3L);
        usuarioModel.setCpf("82407211090");
        usuarioModel.setNome("Erinaldo Teste dos Santos");
        usuarioModel.setIdade(30);
        usuarioModel.setEmail("erinaldo@teste.com.br");

        acaoModel = new AcaoModel();
        acaoModel.setId(1L);
        acaoModel.setTicker("BBDC3");
        acaoModel.setNomeEmpresa("Banco Bradesco");
        acaoModel.setTipoAcao("Ordinária");
        acaoModel.setSetor("Financeiro");
        acaoModel.setPrecoAtual(new BigDecimal("10.50"));


        transacaoModel = new TransacaoModel();
        transacaoModel.setId(UUID.fromString("5c963101-6481-4bfd-a04f-5a3a2a551885"));
        transacaoModel.setUsuario(usuarioModel);
        transacaoModel.setAcao(acaoModel);
        transacaoModel.setTipoTransacao(TipoTransacao.COMPRA);
        transacaoModel.setQuantidade(10);
        transacaoModel.setPrecoUnitario(BigDecimal.valueOf(9.50));
        transacaoModel.setValorTotal(BigDecimal.valueOf(95));
        transacaoModel.setDataOperacao(LocalDateTime.now());

        transacaoResponseDtoCompra = List.of(new TransacaoResponseDto(
                UUID.randomUUID(),
                1L,
                "BBDC3",
                TipoTransacao.COMPRA,
                10,
                new BigDecimal("9.50"),
                new BigDecimal("95"),
                LocalDateTime.now()
                )
        );

        transacaoResponseDtoVenda = List.of(new TransacaoResponseDto(
                        UUID.randomUUID(),
                        1L,
                        "BBDC3",
                        TipoTransacao.VENDA,
                        10,
                        new BigDecimal("9.50"),
                        new BigDecimal("95"),
                        LocalDateTime.now()
                )
        );

    }


    @Test
    @DisplayName("Deve efetuar compra e retornar código 200")
    void efetuarCompraRetornarCodigo202() throws Exception {
        when(transacaoService.realizarCompra(transacaoDtoCompra)).thenReturn(transacaoModel);
        transacaoResponseDto = TransacaoResponseDto.fromModel(transacaoModel);

        mockMvc.perform(post("/transacoes/comprar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transacaoDtoCompra)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transacaoResponseDto)));

        verify(transacaoService, times(1)).realizarCompra(transacaoDtoCompra);
    }

    @Test
    @DisplayName("Deve efetuar venda e retornar o código 202")
    void deveEfetuarVendaRetornarCodigo202() throws Exception {
        transacaoModel.setTipoTransacao(TipoTransacao.VENDA);
        when(transacaoService.realizarVenda(transacaoDtoVenda)).thenReturn(transacaoModel);
        transacaoResponseDto = TransacaoResponseDto.fromModel(transacaoModel);

        mockMvc.perform(delete("/transacoes/vender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoDtoVenda)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(objectMapper.writeValueAsString(transacaoResponseDto)));

        verify(transacaoService, times(1)).realizarVenda(transacaoDtoVenda);
    }

    @Test
    @DisplayName("Deve lançar exceção quando não possuir ações para venda")
    void deveLancarExcecaoQuandoNaoHouverAcoesParaVenda() throws Exception {
        doThrow(new TransacaoSaldoMenorException("O usuário não possui ações suficientes para vender!"))
                .when(transacaoService).realizarVenda(transacaoDtoVenda);

        Error errorEsperado = new Error(HttpStatus.BAD_REQUEST, "O usuário não possui ações suficientes para vender!");

        mockMvc.perform(delete("/transacoes/vender")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transacaoDtoVenda)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(errorEsperado)));

        verify(transacaoService, times(1)).realizarVenda(transacaoDtoVenda);
    }

    @Test
    @DisplayName("Deve consultar todas as compras de um CPF")
    void deveConsultarComprasDeUmCPF() throws Exception {
        when(transacaoService.consultarCompras("00690022077"))
                .thenReturn(transacaoResponseDtoCompra);

        mockMvc.perform(get("/transacoes/compras/{cpf}", "00690022077")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transacaoResponseDtoCompra)));

        verify(transacaoService, times(1)).consultarCompras("00690022077");
    }

    @Test
    @DisplayName("Deve consultar todas as vendas de um CPF")
    void deveConsultarVendaDeUmCPF() throws Exception {
        when(transacaoService.consultarVendas("00690022077"))
                .thenReturn(transacaoResponseDtoVenda);

        mockMvc.perform(get("/transacoes/vendas/{cpf}", "00690022077")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transacaoResponseDtoVenda)));

        verify(transacaoService, times(1)).consultarVendas("00690022077");
    }
}