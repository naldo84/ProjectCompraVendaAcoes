package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.AcaoDto;
import com.investimentos.CompraVendaAcoes.exception.AcaoJaCadastradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.repository.AcaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;


import java.util.Collections;
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

        return acaoRepository.save(acaoModel);
    }

    public List<AcaoModel> consultarAcoes(){
        List<AcaoModel> listaDeAcoes = acaoRepository.findAll();

        return listaDeAcoes.isEmpty() ? Collections.emptyList() : listaDeAcoes;
    }

    public AcaoModel consultarAcaoByTicker(String ticker){
        Optional<AcaoModel> acaoEncontrada = acaoUtilService.pesquisarSeAcaoExiste(ticker);

        return acaoEncontrada.get();
    }

    public AcaoModel alterarAcaoByTicker(String ticker, AcaoDto acaoDto) {
        Optional<AcaoModel> acaoEncontrada = acaoUtilService.pesquisarSeAcaoExiste(ticker);
        //        AcaoModel acaoModel = acaoEncontrada.get();
//
//        AcaoModel acaoAlterada = acaoUtilService.converterDtoParaModel(acaoDto);
//
//        BeanUtils.copyProperties(acaoAlterada, acaoModel);
//
//        return acaoRepository.save(acaoAlterada);


        var acaoModel = acaoEncontrada.get();
        acaoUtilService.converterDtoParaModel(acaoDto, acaoModel);
        acaoRepository.save(acaoModel);

        return acaoModel;
    }

    public void excluirAcaoByTicker(String ticker) {
        Optional<AcaoModel> consultarAcao = acaoRepository.findByticker(ticker);

        if (consultarAcao.isEmpty()) {
            throw new DataIntegrityViolationException("Ação não localizada!");
        }

        acaoRepository.delete(consultarAcao.get());
    }

    public void excluirTodasAsAcoes(){
        List<AcaoModel> listaDeAcoes = acaoRepository.findAll();

        if (listaDeAcoes.isEmpty()){
            throw new DataIntegrityViolationException("Não há ações para excluir!");
        }

        acaoRepository.deleteAll();
    }
}
