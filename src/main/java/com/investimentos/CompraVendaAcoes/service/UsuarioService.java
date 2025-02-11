package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.UsuarioDto;
import com.investimentos.CompraVendaAcoes.exception.UsuarioJaCadastradoException;
import com.investimentos.CompraVendaAcoes.exception.UsuarioNaoEncontrado;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.repository.UsuarioRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    public UsuarioModel cadastrarUsuario(UsuarioDto usuarioDto){
        usuarioRepository.findByCpf(usuarioDto.cpf())
                .ifPresent(usuario -> {
                            throw new UsuarioJaCadastradoException("Usuário já cadastrado!!");
                });

        var usuarioModel = new UsuarioModel();
        BeanUtils.copyProperties(usuarioDto, usuarioModel);

        return usuarioRepository.save(usuarioModel);
    }

    public List<UsuarioModel> consultarUsuarios(){
        List<UsuarioModel> listaDeUsuarios = usuarioRepository.findAll();

        return listaDeUsuarios.isEmpty() ? Collections.emptyList() : listaDeUsuarios;
    }

    public UsuarioModel alterarUsuario(String cpf, UsuarioDto usuarioDto) {
        Optional<UsuarioModel> usuarioEncontrado = usuarioRepository.findByCpf(cpf);

        if (usuarioEncontrado.isEmpty()){
            throw new UsuarioNaoEncontrado("Usuário não localizado");
        }

        UsuarioModel usuarioModel = usuarioEncontrado.get();
        BeanUtils.copyProperties(usuarioDto, usuarioModel);
        usuarioRepository.save(usuarioModel);

        return usuarioModel;
    }

    public void excluirUsuarioByCpf(String cpf){
        Optional<UsuarioModel> usuarioEncontrado = usuarioRepository.findByCpf(cpf);

        if (usuarioEncontrado.isEmpty()) {
            throw new UsuarioNaoEncontrado("Usuário não localizado");

        }

        usuarioRepository.delete(usuarioEncontrado.get());
    }

    public void excluirTodosOsUsuarios(){
        if (usuarioRepository.count() == 0){
            throw new UsuarioNaoEncontrado("Não há usuários na lista!");
        }

        usuarioRepository.deleteAll();
    }
}