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

import com.concord.trivio.entity.Employee;
import com.concord.trivio.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Colaboradores", description = "API de Colaboradores")
@Validated
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Cadastra um novo colaborador")
    @PostMapping
    public ResponseEntity<Void> cadastrar(@Valid @RequestBody Employee employee) {
        employeeService.cadastrar(employee);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualiza um colaborador existente")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualizar(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        employeeService.atualizar(id, employee);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Busca todos os colaboradores")
    @GetMapping
    public ResponseEntity<List<Employee>> listar() {
        List<Employee> lista = employeeService.listar();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @Operation(summary = "Busca um colaborador por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> buscarPorId(@PathVariable Long id) {
        Employee employee = employeeService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(employee);
    }

}
