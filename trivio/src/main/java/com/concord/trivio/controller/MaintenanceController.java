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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.concord.trivio.dto.MaintenanceRequest;
import com.concord.trivio.dto.MaintenanceResponseDTO;
import com.concord.trivio.service.MaintenanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Manutenções", description = "API de Manutenções")
@Validated
@RestController
@RequestMapping("/maintenances")
public class MaintenanceController {

    @Autowired
    private MaintenanceService maintenanceService;

    @Operation(summary = "Cadastra uma nova manutenção")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody MaintenanceRequest maintenanceRequest) {
        maintenanceService.cadastrar(maintenanceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualiza uma manutenção existente. Se status for COMPLETED, retorna nextMaintenanceSuggestion com a sugestão da próxima manutenção a ser confirmada pelo usuário.")
    @PatchMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody MaintenanceRequest maintenanceRequest) {
        return ResponseEntity.ok(maintenanceService.atualizar(id, maintenanceRequest));
    }

    @Operation(summary = "Confirma a criação da próxima manutenção a partir de uma manutenção concluída")
    @PostMapping("/{id}/next")
    public ResponseEntity<MaintenanceResponseDTO> gerarProxima(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.gerarProxima(id));
    }

    @Operation(summary = "Lista manutenções. Se informado employeeId, filtra pelo técnico.")
    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDTO>> listar(
            @RequestParam(required = false) Long employeeId) {

        if (employeeId != null) {
            return ResponseEntity.ok(maintenanceService.listarPorEmployee(employeeId));
        }
        return ResponseEntity.ok(maintenanceService.listar());
    }

    @Operation(summary = "Busca uma manutenção pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(maintenanceService.buscarPorId(id));
    }
}