package com.investimentos.CompraVendaAcoes.service.util;

import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioNaoEncontrado;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioUtilService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public UsuarioModel pesquisarSeUsuarioExiste(String cpf){
        Optional<UsuarioModel> usuario = usuarioRepository.findByCpf(cpf);

        if (usuario.isEmpty()){
            throw new UsuarioNaoEncontrado("Usuário não localizado!");
        }

        return usuario.get();
    }
}
