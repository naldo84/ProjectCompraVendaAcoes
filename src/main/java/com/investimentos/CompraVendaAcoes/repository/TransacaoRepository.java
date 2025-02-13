package com.investimentos.CompraVendaAcoes.repository;

import com.investimentos.CompraVendaAcoes.model.TranscaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository<TranscaoModel, UUID> {
}
