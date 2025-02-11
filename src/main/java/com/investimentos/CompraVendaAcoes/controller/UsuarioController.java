package com.investimentos.CompraVendaAcoes.controller;

import com.investimentos.CompraVendaAcoes.dto.UsuarioDto;
import com.investimentos.CompraVendaAcoes.model.UsuarioModel;
import com.investimentos.CompraVendaAcoes.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UsuarioController {

    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Object> cadastrarUsuario(@RequestBody @Valid UsuarioDto usuarioDto) {
        UsuarioModel usuarioCadastrado = usuarioService.cadastrarUsuario(usuarioDto);
        URI location = URI.create("/users/" + usuarioCadastrado.getCpf());

        return ResponseEntity.created(location).body(usuarioCadastrado);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> consultarUsuarios() {
        List<UsuarioModel> listaDeUsers = usuarioService.consultarUsuarios();

        return ResponseEntity.ok().body(listaDeUsers);

    }

    @PutMapping("{cpf}")
    public ResponseEntity<Object> alterarUsuario(@PathVariable(value = "cpf") String cpf,
                                                 @RequestBody @Valid UsuarioDto usuarioDto) {
        UsuarioModel usuarioAlterado = usuarioService.alterarUsuario(cpf, usuarioDto);

        return ResponseEntity.accepted().body(usuarioAlterado);
    }

    @DeleteMapping("{cpf}")
    public ResponseEntity<Object> excluirUsuarioByCpf(@PathVariable(value = "cpf") String cpf) {

        usuarioService.excluirUsuarioByCpf(cpf);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuário " + cpf + " excluído!");
    }

    @DeleteMapping("/todos")
    public ResponseEntity<Object> excluirTodosOsUsuarios(){
        usuarioService.excluirTodosOsUsuarios();

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Usuários excluídos!");
    }
}
