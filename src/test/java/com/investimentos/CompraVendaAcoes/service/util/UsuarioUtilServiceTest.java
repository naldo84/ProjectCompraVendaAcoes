package com.investimentos.CompraVendaAcoes.service.util;

import com.investimentos.CompraVendaAcoes.dto.UsuarioDto;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoNaoEncontradaException;
import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioNaoEncontradoException;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioUtilServiceTest {

    @InjectMocks
    UsuarioUtilService usuarioUtilService;

    @Mock
    UsuarioRepository usuarioRepository;

    private UsuarioModel usuarioModel;
    private UsuarioDto usuarioDto;

    @BeforeEach
    public void setUp(){
        usuarioDto= new UsuarioDto(
                "95625942030",
                "Teste Santos Silva",
                35,
                "teste.silva@teste.com.br"
        );

        usuarioModel = new UsuarioModel();
        usuarioModel.setCpf(usuarioDto.cpf());
        usuarioModel.setNome(usuarioDto.nome());
        usuarioModel.setIdade(usuarioDto.idade());
        usuarioModel.setEmail(usuarioDto.email());
    }



    @Test
    @DisplayName("Deve pesquisar se o usuário existe")
    void devePesquisarSeOUsuarioExiste(){
       when(usuarioRepository.findByCpf("95625942030")).thenReturn(Optional.of(usuarioModel));

       UsuarioModel resultado = usuarioUtilService.pesquisarSeUsuarioExiste("95625942030");

       assertNotNull(resultado);
       assertEquals("95625942030", resultado.getCpf());
       assertEquals("Teste Santos Silva", resultado.getNome());
       assertEquals(35, resultado.getIdade());
       assertEquals("teste.silva@teste.com.br", resultado.getEmail());

       verify(usuarioRepository, times(1)).findByCpf("95625942030");
    }

    @Test
    @DisplayName("Deve lançar exceção quando não localizar o usuário")
    void deveLancarExcecaoQuandoNaoLocalizarOUsuario(){
        when(usuarioRepository.findByCpf("123456789")).thenReturn(Optional.empty());

        UsuarioNaoEncontradoException resultadoException = assertThrows(UsuarioNaoEncontradoException.class, () ->
            usuarioUtilService.pesquisarSeUsuarioExiste("123456789"));

            assertEquals("Usuário não localizado!", resultadoException.getMessage());
    }
}
