package com.investimentos.CompraVendaAcoes.controller;

import com.investimentos.CompraVendaAcoes.dto.AcaoDto;
import com.investimentos.CompraVendaAcoes.model.AcaoModel;
import com.investimentos.CompraVendaAcoes.service.AcaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/acoes")
public class AcaoController {

    @Autowired
    AcaoService acaoService;

    @PostMapping
    public ResponseEntity<Object> cadastrarAcao(@RequestBody @Valid AcaoDto acaoDto){
        var acaoCadastrada = acaoService.cadastrarAcao(acaoDto);
        URI location = URI.create("/acao/" + acaoCadastrada.getTicker());

        return ResponseEntity.created(location).body(acaoCadastrada);
    }

    @GetMapping
    public ResponseEntity<Object> consultarAcoes(){
        List<AcaoModel> listaDeAcoes = acaoService.consultarAcoes();

        return ResponseEntity.ok().body(listaDeAcoes);
    }

    @GetMapping("/{ticker}")
    public ResponseEntity<Object> consultarAcaoByTicker(@PathVariable(value = "ticker") String ticker){
        AcaoModel acaoEncontrada = acaoService.consultarAcaoByTicker(ticker);

        return ResponseEntity.ok().body(acaoEncontrada);
    }

    @PutMapping("{ticker}")
    public ResponseEntity<Object> alterarAcao(@PathVariable(value = "ticker") String ticker,
                                              @RequestBody @Valid AcaoDto acaoDto){
        AcaoModel acaoAlterada = acaoService.alterarAcaoByTicker(ticker, acaoDto);

        return ResponseEntity.accepted().body(acaoAlterada);
    }

    @DeleteMapping("/{ticker}")
    public ResponseEntity<Object> excluirAcaoByTicker(@PathVariable(value = "ticker") String ticker){
        AcaoModel acaoEncontrada = acaoService.consultarAcaoByTicker(ticker);
        acaoService.excluirAcaoByTicker((acaoEncontrada.getTicker().toUpperCase()));
        return ResponseEntity.ok().body("Acão " + acaoEncontrada.getTicker() + " excluída!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> excluirTodasAcoes(){
        acaoService.excluirTodasAsAcoes();

        return ResponseEntity.accepted().body("As ações foram excluídas!");

    }
}