package com.concord.trivio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concord.trivio.dto.ChecklistRequest;
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
}
