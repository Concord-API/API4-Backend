package com.concord.trivio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concord.trivio.dto.MaintenanceRequest;
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
}
