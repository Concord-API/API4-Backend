package com.concord.trivio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.concord.trivio.entity.Equipment;
import com.concord.trivio.service.EquipmentService;

import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<Equipment>> listar() {
        List<Equipment> lista = equipmentService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Equipment> alterar(@PathVariable Long id, @RequestBody Equipment equipment) {
        Equipment atualizado = equipmentService.alterar(id, equipment);
        return ResponseEntity.status(HttpStatus.OK).body(atualizado);
    }

    @PostMapping
    public ResponseEntity<Equipment> cadastrar(@RequestBody Equipment equipment) {
        Equipment salvo = equipmentService.cadastrar(equipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> buscarPorId(@PathVariable Long id) {
        Equipment equipment = equipmentService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(equipment);
    }
}