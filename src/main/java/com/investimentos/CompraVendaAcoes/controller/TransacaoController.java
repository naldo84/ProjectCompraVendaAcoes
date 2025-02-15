package com.investimentos.CompraVendaAcoes.controller;

import com.investimentos.CompraVendaAcoes.dto.TransacaoDto;
import com.investimentos.CompraVendaAcoes.dto.TransacaoResponseDto;
import com.investimentos.CompraVendaAcoes.model.TransacaoModel;
import com.investimentos.CompraVendaAcoes.service.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    @Autowired
    TransacaoService transacaoService;

    @PostMapping("/comprar")
    public ResponseEntity<Object> efetuarCompra(@RequestBody @Valid TransacaoDto transacaoDto){
        TransacaoModel compraEfetuada = transacaoService.realizarCompra(transacaoDto);
        TransacaoResponseDto compraResponse = TransacaoResponseDto.fromModel(compraEfetuada);

        return ResponseEntity.ok().body(compraResponse);
    }

    @DeleteMapping("/vender")
    public ResponseEntity<TransacaoResponseDto> efetuarVenda(@RequestBody @Valid TransacaoDto transacaoDto){
        TransacaoModel vendaEfetuada = transacaoService.realizarVenda(transacaoDto);
        TransacaoResponseDto vendaResponse = TransacaoResponseDto.fromModel(vendaEfetuada);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(vendaResponse);
    }

    @GetMapping("/compras/{cpf}")
    public ResponseEntity<List<TransacaoResponseDto>> consultarCompras(@PathVariable String cpf){
        List<TransacaoResponseDto> listaDecompras = transacaoService.consultarCompras(cpf);

        return ResponseEntity.ok().body(listaDecompras);
    }

    @GetMapping("/vendas/{cpf}")
    public ResponseEntity<List<TransacaoResponseDto>> consultarVendas(@PathVariable String cpf){
        List<TransacaoResponseDto> listaDeVendas = transacaoService.consultarVendas(cpf);

        return ResponseEntity.ok().body(listaDeVendas);
    }
}
