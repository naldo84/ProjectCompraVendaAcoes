package com.investimentos.CompraVendaAcoes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) //captura erros de validação(notNull, size, patter, etc)
    public ResponseEntity<Map<String, String>> handlerValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        //percorre todos os erros da requisição
        for (FieldError error : ex.getBindingResult().getFieldErrors()){
            errors.put(error.getField(), error.getDefaultMessage());  //inclui no map o nome do campo + mensagem de erro

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);  //Retorna um erro 400
    }

    @ExceptionHandler(AcaoJaCadastradaException.class)
    public ResponseEntity<String> handleAcaoJaCadastradaException(AcaoJaCadastradaException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(AcaoNaoEncontradaException.class)
    public ResponseEntity<String> handleAcaoNaoEncontradaException(AcaoNaoEncontradaException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class) //captura qualquer outra exceção não tratada
    public ResponseEntity<String> handleGlobalExceptions (Exception ex){
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp ", LocalDateTime.now());
        response.put("status ", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error ", "Erro interno.");
        response.put("message ", "Ocorreu um erro inesperado.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.toString());

        //versão 2:
        //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado. Contate o suporte!");
    }

    //EXCEÇÕES DE USUÁRIOS
    @ExceptionHandler(UsuarioJaCadastradoException.class)
    public ResponseEntity<String> handlerUsuarioJaCadastradoException(UsuarioJaCadastradoException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontrado.class)
    public ResponseEntity<String> handlerUsuarioNaoEncontrado(UsuarioNaoEncontrado ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


}
