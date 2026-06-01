package com.concord.trivio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.concord.trivio.dto.ChecklistRequest;
import com.concord.trivio.dto.ChecklistResponseDTO;
import com.concord.trivio.service.ChecklistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Checklists", description = "API de Checklists")
@Validated
@RestController
@RequestMapping("/checklists")
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    @Operation(summary = "Cadastra um novo checklist")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody ChecklistRequest request) {
        checklistService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Lista checklists de uma manutencao")
    @GetMapping
    public ResponseEntity<List<ChecklistResponseDTO>> listarPorMaintenance(@RequestParam Long maintenanceId) {
        return ResponseEntity.status(HttpStatus.OK).body(checklistService.listarPorMaintenance(maintenanceId));
    }

    @Operation(summary = "Atualiza um checklist existente")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody ChecklistRequest request) {
        checklistService.atualizar(id, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Remove um checklist")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        checklistService.deletar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
