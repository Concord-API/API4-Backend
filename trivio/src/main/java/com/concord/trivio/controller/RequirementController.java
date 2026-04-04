package com.concord.trivio.controller;

import java.util.List;

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

import com.concord.trivio.dto.RequirementRequest;
import com.concord.trivio.entity.Requirement;
import com.concord.trivio.service.RequirementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Requisitos", description = "API de Requisitos")
@Validated
@RestController
@RequestMapping("/requirements")
public class RequirementController {

    private final RequirementService requirementService;

    public RequirementController(RequirementService requirementService) {
        this.requirementService = requirementService;
    }

    @Operation(summary = "Cadastra um novo requisito")
    @PostMapping
    public ResponseEntity<Requirement> cadastrar(@Valid @RequestBody RequirementRequest request) {
        Requirement salvo = requirementService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @Operation(summary = "Atualiza um requisito existente")
    @PatchMapping("/{id}")
    public ResponseEntity<Requirement> atualizar(@PathVariable Long id, @RequestBody RequirementRequest request) {
        Requirement atualizado = requirementService.atualizar(id, request);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Lista todos os requisitos")
    @GetMapping
    public ResponseEntity<List<Requirement>> listar() {
        return ResponseEntity.ok(requirementService.listar());
    }

    @Operation(summary = "Busca um requisito por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Requirement> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(requirementService.buscarPorId(id));
    }
}
