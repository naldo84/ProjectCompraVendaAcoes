package com.investimentos.CompraVendaAcoes.exception;

import com.investimentos.CompraVendaAcoes.exception.acao.AcaoJaCadastradaException;
import com.investimentos.CompraVendaAcoes.exception.acao.AcaoNaoEncontradaException;
import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioJaCadastradoException;
import com.investimentos.CompraVendaAcoes.exception.usuario.UsuarioNaoEncontrado;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {

    //TRATAMENTO DOS ERRO DE VALIDAÇÕES
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        StringBuilder errorMessages = new StringBuilder("Erro(s) de validação:\n");
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                errorMessages.append(fieldError.getDefaultMessage()).append("\n")
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages.toString());
    }

    //EXCEPTIONS DAS AÇÕES
    @ExceptionHandler(AcaoJaCadastradaException.class)
    @ResponseBody
    public ResponseEntity<Object> handleAcaoJaCAdastradaException(AcaoJaCadastradaException ex){
        //Error error 1= new java.lang.Error(HttpStatus.CONFLICT, ex.getMessage());
       Error error = new Error(HttpStatus.CONFLICT, ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }

    @ExceptionHandler(AcaoNaoEncontradaException.class)
    @ResponseBody
    public ResponseEntity<Object> handleAcaoNaoEncontradaException(AcaoNaoEncontradaException ex){
        Error error = new Error(HttpStatus.NOT_FOUND, ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleException(Exception e){
        Error error = new Error(HttpStatus.INTERNAL_SERVER_ERROR, "OCORREU UM ERRO INESPERADO");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //EXCEPTIONS DOS USUÁRIOS
    @ExceptionHandler(UsuarioJaCadastradoException.class)
    @ResponseBody
    public ResponseEntity<Object> handleUsuarioJaCAdastradoException(UsuarioJaCadastradoException ex){
        Error error = new Error(HttpStatus.CONFLICT, ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UsuarioNaoEncontrado.class)
    @ResponseBody
    public ResponseEntity<Object> handleUsuarioNaoEncontradaException(UsuarioNaoEncontrado ex){
        Error error = new Error(HttpStatus.NOT_FOUND, ex.getMessage());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }



}
