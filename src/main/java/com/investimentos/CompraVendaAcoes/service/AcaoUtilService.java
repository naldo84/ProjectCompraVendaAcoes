package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.AcaoDto;
import com.investimentos.CompraVendaAcoes.exception.AcaoNaoEncontradaException;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.repository.AcaoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AcaoUtilService {

    @Autowired
    AcaoRepository acaoRepository;

    public Optional<AcaoModel> pesquisarSeAcaoExiste(String ticker) {
        Optional<AcaoModel> acaoEncontrada = acaoRepository.findByticker(ticker);
        if (acaoEncontrada.isEmpty()){
            throw new AcaoNaoEncontradaException("A ação " + ticker.toUpperCase() + " não foi localizada!");
        }

        return acaoEncontrada;
    }

    public AcaoModel converterDtoParaModel (AcaoDto acaoDto, AcaoModel acaoModel){
        BeanUtils.copyProperties(acaoDto, acaoModel);

        return acaoModel;
    }
}
