package com.investimentos.CompraVendaAcoes.repository;

import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AcaoRepository extends JpaRepository<AcaoModel, Long> {

    Optional<AcaoModel> findByticker(String ticker);

}