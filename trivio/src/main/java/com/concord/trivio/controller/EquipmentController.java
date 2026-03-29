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

    @GetMapping("/{id}")
    public ResponseEntity<Equipment> buscarPorId(@PathVariable Long id) {
        Equipment equipment = equipmentService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(equipment);
    }
}