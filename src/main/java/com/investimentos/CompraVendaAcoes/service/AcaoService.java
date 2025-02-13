package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.AcaoDto;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoJaCadastradaException;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoNaoEncontradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.repository.AcaoRepository;
import com.investimentos.CompraVendaAcoes.service.util.AcaoUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class AcaoService {

    @Autowired
    AcaoRepository acaoRepository;

    @Autowired
    AcaoUtilService acaoUtilService;

    public AcaoModel cadastrarAcao(AcaoDto acaoDto){
        acaoRepository.findByticker(acaoDto.ticker())
                .ifPresent(acao -> {
                    throw new AcaoJaCadastradaException("Ação já cadastrada no sistema");
                });

        var acaoModel2 = new AcaoModel();

        AcaoModel acaoModel = acaoUtilService.converterDtoParaModel(acaoDto, acaoModel2);

        acaoModel.setTicker(acaoModel.getTicker().toUpperCase());

        return acaoRepository.save(acaoModel);
    }

    public List<AcaoModel> consultarAcoes(){
        return acaoRepository.findAll();
    }

    public AcaoModel consultarAcaoByTicker(String ticker){
        Optional<AcaoModel> acaoEncontrada = acaoUtilService.pesquisarSeAcaoExiste(ticker.toUpperCase());

        return acaoEncontrada.get();
    }

    public AcaoModel alterarAcaoByTicker(String ticker, AcaoDto acaoDto) {
        Optional<AcaoModel> acaoEncontrada = acaoUtilService.pesquisarSeAcaoExiste(ticker);

        var acaoModel = acaoEncontrada.get();
        acaoUtilService.converterDtoParaModel(acaoDto, acaoModel);
        acaoRepository.save(acaoModel);

        return acaoModel;
    }

    public void excluirAcaoByTicker(String ticker) {
        Optional<AcaoModel> acaoEncontrada = acaoUtilService.pesquisarSeAcaoExiste(ticker.toUpperCase());

        acaoRepository.delete(acaoEncontrada.get());
    }

    public void excluirTodasAsAcoes(){
        if (acaoRepository.count() == 0){
            throw new AcaoNaoEncontradaException("Não há ações para excluir!");
        }

        acaoRepository.deleteAll();
    }
}
