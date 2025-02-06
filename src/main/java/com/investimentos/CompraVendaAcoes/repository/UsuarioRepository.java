package com.investimentos.CompraVendaAcoes.repository;

import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    UsuarioModel findByCpf(String cpf);

}