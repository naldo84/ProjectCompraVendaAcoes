package com.investimentos.CompraVendaAcoes.repository;

import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {

    Optional<UsuarioModel> findByCpf(String cpf);

}