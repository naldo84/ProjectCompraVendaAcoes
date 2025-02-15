package com.investimentos.CompraVendaAcoes.repository;

import com.investimentos.CompraVendaAcoes.enums.TipoTransacao;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.model.TransacaoModel;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository<TransacaoModel, UUID> {
    List<TransacaoModel> findByUsuarioAndAcao(UsuarioModel usuario, AcaoModel acao);

    List<TransacaoModel> findByUsuarioAndTipoTransacao(UsuarioModel usuario, TipoTransacao tipoTransacao);
}
