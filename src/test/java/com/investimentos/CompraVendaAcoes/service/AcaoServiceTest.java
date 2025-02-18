package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.AcaoDto;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoJaCadastradaException;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoNaoEncontradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.repository.AcaoRepository;
import com.investimentos.CompraVendaAcoes.service.util.AcaoUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AcaoServiceTest {

    @InjectMocks
    private AcaoService acaoService;

    @Mock
    private AcaoRepository acaoRepository;

    @Mock
    private AcaoUtilService acaoUtilService;

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
    @DisplayName("Deve cadastrar uma ação com sucesso!")
    void cadastrarAcaoComSucesso(){
        when(acaoRepository.findByTicker(acaoDto.ticker())).thenReturn(Optional.empty());
        when(acaoUtilService.converterDtoParaModel(any(AcaoDto.class), any(AcaoModel.class))).thenReturn(acaoModel);
        when(acaoRepository.save(any(AcaoModel.class))).thenReturn(acaoModel);

        AcaoModel resultado = acaoService.cadastrarAcao(acaoDto);

        assertNotNull(resultado);
        assertEquals("BBDC3", resultado.getTicker());
        assertEquals("Banco Bradesco", resultado.getNomeEmpresa());
        assertEquals("Ordinária", resultado.getTipoAcao());
        assertEquals("Financeiro", resultado.getSetor());
        assertEquals(new BigDecimal("9.5"), resultado.getPrecoAtual());

        verify(acaoRepository, times(1)).findByTicker(acaoDto.ticker());
        verify(acaoRepository, times(1)).save(acaoModel);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a ação já estiver cadastrada")
    void deveLancarExcecaoQuandoAcaoEstiverCadastrada(){
        when(acaoRepository.findByTicker("BBDC3")).thenReturn(Optional.of(acaoModel));

        RuntimeException resultExption = assertThrows(AcaoJaCadastradaException.class, () -> acaoService.cadastrarAcao(acaoDto));

        assertEquals("Ação já cadastrada no sistema", resultExption.getMessage());
        verify(acaoRepository, times(1)).findByTicker(acaoDto.ticker());
        verify(acaoRepository, never()).save(any(acaoModel.getClass()));
    }

    @Test
    @DisplayName("Deve consutar a lista de ações com sucesso")
    void deveConsultarListaDeAcoesComSucesso(){
        when(acaoRepository.findAll()).thenReturn(Collections.singletonList(acaoModel));

        List<AcaoModel> resultado = acaoService.consultarAcoes();

        assertNotNull(resultado);
        verify(acaoRepository, times(1)).findAll();
        assertEquals(1, resultado.size());
        assertEquals("BBDC3", resultado.get(0).getTicker());
        assertEquals("Banco Bradesco", resultado.get(0).getNomeEmpresa());
        assertEquals("Ordinária", resultado.get(0).getTipoAcao());
        assertEquals("Financeiro", resultado.get(0).getSetor());
        assertEquals(new BigDecimal("9.5"), resultado.get(0).getPrecoAtual());
    }

    @Test
    @DisplayName("Deve consultar uma ação pelo ticker com sucesso")
    void deveConsultarAcaoPeloTickerComSucesso(){
        when(acaoUtilService.pesquisarSeAcaoExiste("BBDC3")).thenReturn(acaoModel);

        AcaoModel resultado = acaoService.consultarAcaoByTicker(acaoModel.getTicker());

        assertNotNull(resultado);
        assertEquals("BBDC3", resultado.getTicker());
        assertEquals("Banco Bradesco", resultado.getNomeEmpresa());
        assertEquals("Ordinária", resultado.getTipoAcao());
        assertEquals("Financeiro", resultado.getSetor());
        assertEquals(new BigDecimal("9.5"), resultado.getPrecoAtual());

        verify(acaoUtilService, times(1)).pesquisarSeAcaoExiste(acaoDto.ticker());
    }

    @Test
    @DisplayName("Deve alterar uma ação com sucesso")
    void deveAlterarAcaoComSucesso(){
        when(acaoUtilService.pesquisarSeAcaoExiste("BBDC3")).thenReturn(acaoModel);
        when(acaoUtilService.converterDtoParaModel(any(AcaoDto.class), any(AcaoModel.class))).thenReturn(acaoModel);
        when(acaoRepository.save(any(AcaoModel.class))).thenReturn(acaoModel);

        AcaoModel resultado = acaoService.alterarAcaoByTicker("BBDC3", acaoDto);
        
        assertNotNull(resultado);
        verify(acaoUtilService, times(1)).pesquisarSeAcaoExiste("BBDC3");
        verify(acaoUtilService, times(1)).converterDtoParaModel(any(AcaoDto.class), any(AcaoModel.class));
        verify(acaoRepository, times(1)).save(acaoModel);
        assertEquals("BBDC3", resultado.getTicker());
        assertEquals("Banco Bradesco", resultado.getNomeEmpresa());
        assertEquals("Ordinária", resultado.getTipoAcao());
        assertEquals("Financeiro", resultado.getSetor());
        assertEquals(new BigDecimal("9.5"), resultado.getPrecoAtual());
    }

    @Test
    @DisplayName("Deve excluir uma ação, consultando pelo ticker")
    void deveExcluirAcaoComSucesso(){
        when(acaoUtilService.pesquisarSeAcaoExiste("BBDC3")).thenReturn(acaoModel);
        doNothing().when(acaoRepository).delete(acaoModel);

        acaoService.excluirAcaoByTicker("BBDC3");

        verify(acaoUtilService, times(1)).pesquisarSeAcaoExiste("BBDC3");
        verify(acaoRepository, times(1)).delete(any(AcaoModel.class));
    }

    @Test
    @DisplayName("Deve excluir todas as ações com sucesso")
    void deveExcluirTodasAsAcoesComSucesso(){
        when(acaoRepository.count()).thenReturn(1L);

        acaoService.excluirTodasAsAcoes();

        verify(acaoRepository, times(1)).count();
        verify(acaoRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Deve lançar exceção se não houver ação na lista")
    void deveLancarExcecaoComListaVazia(){
        when(acaoRepository.count()).thenReturn(0L);

        AcaoNaoEncontradaException resultado = assertThrows(AcaoNaoEncontradaException.class, () -> {
            acaoService.excluirTodasAsAcoes();
        });

        assertEquals("Não há ações para excluir!", resultado.getMessage());
        verify(acaoRepository, times(1)).count();
        verify(acaoRepository, never()).deleteAll();
    }
}
