package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.UsuarioDto;
import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioJaCadastradoException;
import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioNaoEncontradoException;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.repository.UsuarioRepository;
import com.investimentos.CompraVendaAcoes.service.util.UsuarioUtilService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioUtilService usuarioUtilService;

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
        return usuarioRepository.findAll();
    }

    public UsuarioModel alterarUsuario(String cpf, UsuarioDto usuarioDto) {
        UsuarioModel usuarioEncontrado = usuarioUtilService.pesquisarSeUsuarioExiste(cpf);

        BeanUtils.copyProperties(usuarioDto, usuarioEncontrado);
        usuarioRepository.save(usuarioEncontrado);

        return usuarioEncontrado;
    }

    public void excluirUsuarioByCpf(String cpf){
        UsuarioModel usuarioEncontrado = usuarioUtilService.pesquisarSeUsuarioExiste(cpf);

        usuarioRepository.delete(usuarioEncontrado);
    }

    public void excluirTodosOsUsuarios(){
        if (usuarioRepository.count() == 0){
            throw new UsuarioNaoEncontradoException("Não há usuários para excluir!");
        }

        usuarioRepository.deleteAll();
    }
}