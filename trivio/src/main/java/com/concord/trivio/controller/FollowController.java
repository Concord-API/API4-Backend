package com.concord.trivio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.concord.trivio.dto.FollowRequest;
import com.concord.trivio.dto.FollowResponseDTO;
import com.concord.trivio.service.FollowService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Acompanhamentos", description = "API de Acompanhamentos de Manutencao")
@Validated
@RestController
@RequestMapping("/follows")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Operation(summary = "Cadastra um novo acompanhamento para uma manutencao")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody FollowRequest followRequest) {
        followService.cadastrar(followRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualiza um acompanhamento existente")
    @PatchMapping
    public ResponseEntity<Void> atualizar(@Valid @RequestBody FollowRequest followRequest) {
        followService.atualizar(followRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Lista acompanhamentos ativos de uma manutencao")
    @GetMapping
    public ResponseEntity<List<FollowResponseDTO>> listarPorMaintenance(
            @RequestParam Long maintenanceId) {
        return ResponseEntity.status(HttpStatus.OK).body(followService.listarPorMaintenance(maintenanceId));
    }
}
