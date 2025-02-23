package com.investimentos.CompraVendaAcoes.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.investimentos.CompraVendaAcoes.dto.AcaoDto;
import com.investimentos.CompraVendaAcoes.exception.GlobalExceptionHandler;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoJaCadastradaException;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoNaoEncontradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.service.AcaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AcaoControllerTest {

    @InjectMocks
    AcaoController acaoController;

    @Mock
    private AcaoService acaoService;

    private MockMvc mockMvc;
    private AcaoModel acaoModel;
    private AcaoDto acaoDto;
    private AcaoModel acaoModelAlterada;
    private AcaoDto acaoDtoAlterada;
    private String jsonContent;
    private ObjectMapper objectMapper;
    private String arrayAcoesResponse;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders
                .standaloneSetup(acaoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        acaoModel = new AcaoModel();
        acaoModel.setId(1L);
        acaoModel.setTicker("BBDC3");
        acaoModel.setNomeEmpresa("Banco Bradesco");
        acaoModel.setTipoAcao("Ordinária");
        acaoModel.setSetor("Financeiro");
        acaoModel.setPrecoAtual(new BigDecimal("10.50"));

        acaoDto = new AcaoDto("BBDC3",
                "Banco Bradesco",
                "Ordinária",
                "Financeiro",
                new BigDecimal("10.50"));

        acaoModelAlterada = new AcaoModel();
        acaoModelAlterada.setId(1L);
        acaoModelAlterada.setTicker("BBDC3");
        acaoModelAlterada.setNomeEmpresa("Banco Bradesco - Alterado");
        acaoModelAlterada.setTipoAcao("Preferencial");
        acaoModelAlterada.setSetor("Financeiro");
        acaoModelAlterada.setPrecoAtual(new BigDecimal("11.50"));

        acaoDtoAlterada = new AcaoDto("BBDC3",
                "Banco Bradesco - Alterado",
                "Preferencial",
                "Financeiro",
                new BigDecimal("11.50"));

        objectMapper = new ObjectMapper();

        jsonContent = objectMapper.writeValueAsString(acaoDto);

        arrayAcoesResponse = """
                [
                    {
                    "id": 1,
                    "ticker": "BBDC3",
                    "nomeEmpresa": "Banco Bradesco",
                    "tipoAcao": "Ordinária",
                    "setor": "Financeiro",
                    "precoAtual": 10.50
                    }
                ]
                """;
    }

    @Test
    @DisplayName("Deve cadastrar uma ação com sucesso e retornar código 201")
    void deveCadastrarAcaoERetornarCodigo201() throws Exception {
        when(acaoService.cadastrarAcao(acaoDto)).thenReturn(acaoModel);

        mockMvc.perform(post("/acoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/acao/BBDC3"))
                .andExpect(content().json(objectMapper.writeValueAsString(acaoModel)));

        verify(acaoService, times(1)).cadastrarAcao(acaoDto);
    }


    @Test
    @DisplayName("Deve lançar exceção quando a ação já estiver cadastrada")
    void deveRetornar409QuandoAcaoJaEstiverCadastrada() throws Exception{
        when(acaoService.cadastrarAcao(any(AcaoDto.class)))
                .thenThrow(new AcaoJaCadastradaException("Ação já cadastrada no sistema"));

        mockMvc.perform(post("/acoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(acaoDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Ação já cadastrada no sistema"));

        verify(acaoService, times(1)).cadastrarAcao(any(AcaoDto.class));
    }

    @Test
    @DisplayName("Deve consultar ações e retornar código 200 ")
    void deveConsultarAcoesRetornarCodigo200() throws Exception {
        when(acaoService.consultarAcoes()).thenReturn(Collections.singletonList(acaoModel));

        mockMvc.perform(get("/acoes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(arrayAcoesResponse));

        verify(acaoService, times(1)).consultarAcoes();
    }

    @Test
    @DisplayName("Deve lançar exceção quando não localizar a ação")
    void deveLancarExcecaoQuandoNaoLocalizarAcao() throws Exception {
        when(acaoService.consultarAcaoByTicker("BBDC3")).thenThrow(new AcaoNaoEncontradaException("A ação BBDC3 não foi localizada!"));

        mockMvc.perform(get("/acoes/BBDC3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("A ação BBDC3 não foi localizada!"));

        verify(acaoService, times(1)).consultarAcaoByTicker("BBDC3");
    }

    @Test
    @DisplayName("Deve consultar uma ação pelo ticker e retornar código 200")
    void deveConsultarAcaoPeloTickerRetornarCodigo200() throws Exception {
        when(acaoService.consultarAcaoByTicker("BBDC3")).thenReturn(acaoModel);

        mockMvc.perform(get("/acoes/BBDC3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(acaoModel)));

        verify(acaoService, times(1)).consultarAcaoByTicker("BBDC3");
    }

    @Test
    @DisplayName("Deve alterar uma ação e retornar o código 202")
    void deveAlterarAcaoERetornarCodigo202() throws Exception {
        when(acaoService.alterarAcaoByTicker("BBDC3", acaoDtoAlterada)).thenReturn(acaoModelAlterada);

        mockMvc.perform(put("/acoes/BBDC3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(acaoDtoAlterada)))
                .andExpect(status().isAccepted())
                .andExpect(content().json(objectMapper.writeValueAsString(acaoModelAlterada)));

        verify(acaoService, times(1)).alterarAcaoByTicker("BBDC3", acaoDtoAlterada);
    }

    @Test
    @DisplayName("Deve excluir ação pelo ticker e retornar o código 202")
    void deveExcluirAcaoPeloTickerERetornarCodigo202() throws Exception {
        doNothing().when(acaoService).excluirAcaoByTicker("BBDC3");

        mockMvc.perform(delete("/acoes/BBDC3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Ação BBDC3 excluída!"));

        verify(acaoService, times(1)).excluirAcaoByTicker("BBDC3");
    }

    @Test
    @DisplayName("Deve excluir todas as ações e retornar o código 202")
    void deveExcluirAcaoTodasAcoesERetornarCodigo202() throws Exception {
        doNothing().when(acaoService).excluirTodasAsAcoes();

        mockMvc.perform(delete("/acoes/delete")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string("As ações foram excluídas!"));

        verify(acaoService, times(1)).excluirTodasAsAcoes();
    }
}
