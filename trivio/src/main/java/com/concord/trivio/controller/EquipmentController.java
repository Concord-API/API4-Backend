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

import com.concord.trivio.entity.Equipment;
import com.concord.trivio.service.EquipmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Equipamentos", description = "API de Equipamentos")
@Validated
@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;
    
    @Operation(summary = "Lista todos os equipamentos")
    @GetMapping
    public ResponseEntity<List<Equipment>> listar() {
        List<Equipment> lista = equipmentService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @Operation(summary = "Atualiza um equipamento existente")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody Equipment equipment) {
        equipmentService.atualizar(id, equipment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Cadastra um novo equipamento")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody Equipment equipment) {
        equipmentService.cadastrar(equipment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Busca um equipamento por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Equipment> buscarPorId(@PathVariable Long id) {
        Equipment equipment = equipmentService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(equipment);
    }
}
