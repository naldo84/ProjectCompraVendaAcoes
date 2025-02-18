package com.investimentos.CompraVendaAcoes.service.util;

import com.investimentos.CompraVendaAcoes.dto.AcaoDto;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoNaoEncontradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.repository.AcaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AcaoUtilServiceTest {
    @InjectMocks
    AcaoUtilService acaoUtilService;

    @Mock
    AcaoRepository acaoRepository;

    private AcaoModel acaoModel;
    private AcaoDto acaoDto;

    @BeforeEach
    public void setUp(){
        acaoDto = new AcaoDto(
                "BBDC3",
                "Banco Bradesco",
                "Ordinária",
                "Financeiro",
                BigDecimal.valueOf(9.50)
        );

        acaoModel = new AcaoModel();
        acaoModel.setTicker(acaoDto.ticker());
        acaoModel.setNomeEmpresa(acaoDto.nomeEmpresa());
        acaoModel.setTipoAcao(acaoDto.tipoAcao());
        acaoModel.setSetor(acaoDto.setor());
        acaoModel.setPrecoAtual(acaoDto.precoAtual());
    }

    @Test
    @DisplayName("Deve pesquisar ação com sucesso")
    void devePesquisarAcaoComSucesso(){
        when(acaoRepository.findByTicker("BBDC3")).thenReturn(Optional.of(acaoModel));

        AcaoModel resultado = acaoUtilService.pesquisarSeAcaoExiste("BBDC3");

        assertNotNull(resultado);
        assertEquals("BBDC3", resultado.getTicker());
        assertEquals("Banco Bradesco", resultado.getNomeEmpresa());
        assertEquals("Ordinária", resultado.getTipoAcao());
        assertEquals("Financeiro", resultado.getSetor());
        assertEquals(new BigDecimal("9.5"), resultado.getPrecoAtual());

        verify(acaoRepository, times(1)).findByTicker("BBDC3");
    }

    @Test
    @DisplayName("Deve lançar erro quando não localizar a ação")
    void deveLancarErroQuandoNaoEncontrarAcao(){
        when(acaoRepository.findByTicker("BBDC3")).thenReturn(Optional.empty());

        AcaoNaoEncontradaException resultException = assertThrows(AcaoNaoEncontradaException.class, () ->
                acaoUtilService.pesquisarSeAcaoExiste("BBDC3"));

        assertEquals("A ação BBDC3 não foi localizada!", resultException.getMessage());

        verify(acaoRepository, times(1)).findByTicker("BBDC3");

    }

    @Test
    @DisplayName("Deve fazer a conversão DTO para model")
    void deveConverterDtoParaModel(){
        AcaoModel acaoModel = new AcaoModel();
        AcaoModel resultado = acaoUtilService.converterDtoParaModel(acaoDto, acaoModel);

        assertEquals("BBDC3", resultado.getTicker());
        assertEquals("Banco Bradesco", resultado.getNomeEmpresa());
    }
}
