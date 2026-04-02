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

import com.concord.trivio.entity.Client;
import com.concord.trivio.service.ClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@Tag(name = "Clientes", description = "API de Clientes")
@Validated
@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Lista todos os clientes")
    @GetMapping
    public ResponseEntity<List<Client>> listar() {
        List<Client> lista = clientService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @Operation(summary = "Busca um cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Client> buscarPorId(@PathVariable Long id) {
        Client client = clientService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @Operation(summary = "Atualiza um cliente existente")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody Client client) {
        clientService.atualizar(id, client);
        return ResponseEntity.status(204).build();
    }

    @Operation(summary = "Cadastra um novo cliente")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody Client client) {
        clientService.cadastrar(client);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}