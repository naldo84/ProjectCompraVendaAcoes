package com.investimentos.CompraVendaAcoes.service;

import com.investimentos.CompraVendaAcoes.dto.UsuarioDto;
import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioJaCadastradoException;
import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioNaoEncontradoException;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.repository.UsuarioRepository;
import com.investimentos.CompraVendaAcoes.service.util.UsuarioUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.stubbing.answers.DoesNothing;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.BeanUtils;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @InjectMocks
    UsuarioService usuarioService;

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    UsuarioUtilService usuarioUtilService;

    private UsuarioDto usuarioDto;
    private UsuarioModel usuarioModel;

    @BeforeEach
    public void setUp(){
        usuarioDto = new UsuarioDto(
                "00690022077",
                "Erinaldo Teste",
                35,
                "erinaldo@teste.com.br"

        );

        usuarioModel = new UsuarioModel();
        usuarioModel.setId(1L);
        usuarioModel.setCpf(usuarioDto.cpf());
        usuarioModel.setNome(usuarioDto.nome());
        usuarioModel.setIdade(usuarioDto.idade());
        usuarioModel.setEmail(usuarioDto.email());
    }

    @Test
    @DisplayName("Deve cadastrar o usuário com sucesso")
    void deveCadastrarUsuarioComSucesso(){
        when(usuarioRepository.findByCpf("00690022077")).thenReturn(Optional.empty());

        BeanUtils.copyProperties(usuarioDto, usuarioModel);
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(usuarioModel);

        UsuarioModel resultado = usuarioService.cadastrarUsuario(usuarioDto);

        assertNotNull(resultado);
        assertEquals("00690022077", resultado.getCpf());
        assertEquals("Erinaldo Teste", resultado.getNome());
        assertEquals(35, resultado.getIdade());
        assertEquals("erinaldo@teste.com.br", resultado.getEmail());

        verify(usuarioRepository, times(1)).findByCpf("00690022077");
        verify(usuarioRepository, times(1)).save(any(UsuarioModel.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar um usuário já cadastrado")
    void deveLancarExcessoaAoCadastrarUsuarioJaCadastrado(){
        when(usuarioRepository.findByCpf("00690022077")).thenReturn(Optional.of(usuarioModel));

        UsuarioJaCadastradoException resultado = assertThrows(UsuarioJaCadastradoException.class, () -> {
            usuarioService.cadastrarUsuario(usuarioDto);
        });

        assertEquals("Usuário já cadastrado!!", resultado.getMessage());
        verify(usuarioRepository, times(1)).findByCpf("00690022077");
        verify(usuarioRepository, never()).save(usuarioModel);
    }

    @Test
    @DisplayName("Deve consultar todos os usuários com sucesso")
    void deveConsultarTodosOsUsuariosComSucesso(){
        when(usuarioRepository.findAll()).thenReturn(Collections.singletonList(usuarioModel));

        List<UsuarioModel> resultado = usuarioService.consultarUsuarios();

        assertEquals("00690022077", resultado.get(0).getCpf());
        assertEquals("Erinaldo Teste", resultado.get(0).getNome());
        assertEquals(35, resultado.get(0).getIdade());
        assertEquals("erinaldo@teste.com.br", resultado.get(0).getEmail());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve alterar o usuário com sucesso")
    void deveAlterarUsuarioComSucesso(){
        when(usuarioUtilService.pesquisarSeUsuarioExiste("00690022077")).thenReturn(usuarioModel);
        BeanUtils.copyProperties(usuarioDto, usuarioModel);
        when(usuarioRepository.save(usuarioModel)).thenReturn(usuarioModel);

        UsuarioModel resultado = usuarioService.alterarUsuario("00690022077", usuarioDto);

        assertNotNull(resultado);
        assertEquals("00690022077", resultado.getCpf());
        assertEquals("Erinaldo Teste", resultado.getNome());
        assertEquals(35, resultado.getIdade());
        assertEquals("erinaldo@teste.com.br", resultado.getEmail());
        verify(usuarioUtilService, times(1)).pesquisarSeUsuarioExiste("00690022077");
        verify(usuarioRepository, times(1)).save(usuarioModel);

    }

    @Test
    @DisplayName("Deve excluir o usuário com sucesso, pesquisando pelo CPF")
    void deveExcluirUsuarioComSucesso(){
        when(usuarioUtilService.pesquisarSeUsuarioExiste("00690022077")).thenReturn(usuarioModel);
        doNothing().when(usuarioRepository).delete(usuarioModel);

        usuarioService.excluirUsuarioByCpf("00690022077");

        verify(usuarioUtilService, times(1)).pesquisarSeUsuarioExiste("00690022077");
        verify(usuarioRepository, times(1)).delete(usuarioModel);
    }

    @Test
    @DisplayName("Deve excluir todos os usuários com sucesso")
    void deveExcluirTodosUsuariosComSucesso(){
        when(usuarioRepository.count()).thenReturn(2L);
        doNothing().when(usuarioRepository).deleteAll();

        usuarioService.excluirTodosOsUsuarios();

        verify(usuarioRepository, times(1)).count();
        verify(usuarioRepository, times(1)).deleteAll();
    }

    @Test
    @DisplayName("Deve lançar excessão ao tentar excluir usuários")
    void deveLancarExcecaoExcluirUsuarios(){
        when(usuarioRepository.count()).thenReturn(0L);
        UsuarioNaoEncontradoException resultado = assertThrows(UsuarioNaoEncontradoException.class, () ->{
            usuarioService.excluirTodosOsUsuarios();
        });

        assertEquals("Não há usuários para excluir!", resultado.getMessage());
        verify(usuarioRepository, times(1)).count();
        verify(usuarioRepository, never()).deleteAll();
    }
}
