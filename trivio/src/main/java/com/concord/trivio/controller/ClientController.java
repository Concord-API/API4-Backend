package com.concord.trivio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.concord.trivio.entity.Client;
import com.concord.trivio.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<Client> cadastrar(@RequestBody Client client) {
        Client salvo = clientService.cadastrar(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }
}