package com.concord.trivio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concord.trivio.dto.ContractRequest;
import com.concord.trivio.entity.Contract;
import com.concord.trivio.service.ContractService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Contratos", description = "API de Contratos")
@Validated
@RestController
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Operation(summary = "Lista todos os contratos")
    @GetMapping
    public ResponseEntity<List<Contract>> listar() {
        List<Contract> lista = contractService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @Operation(summary = "Busca um contrato por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Contract> buscarPorId(@PathVariable Long id) {
        Contract contract = contractService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(contract);
    }

    @Operation(summary = "Atualiza um contrato existente")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody ContractRequest contractRequest) {
        contractService.atualizar(id, contractRequest);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Cadastra um novo contrato")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody ContractRequest contractRequest) {
        contractService.cadastrar(contractRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
