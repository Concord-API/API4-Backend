package com.concord.trivio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.concord.trivio.entity.Employee;
import com.concord.trivio.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Colaboradores", description = "API de Colaboradores")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Operation(summary = "Cadastra um novo colaborador")
    @PostMapping
    public ResponseEntity<Employee> cadastrar(@RequestBody Employee employee) {
        Employee salvo = employeeService.cadastrar(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @Operation(summary = "Atualiza um colaborador existente")
    @PutMapping("/{id}")
    public ResponseEntity<Employee> atualizar(@PathVariable Long id, @RequestBody Employee employee) {
        Employee atualizado = employeeService.atualizar(id, employee);
        if (atualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Busca todos os colaboradores")
    @GetMapping
    public ResponseEntity<List<Employee>> listar() {
        List<Employee> employees = employeeService.listar();
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Busca um colaborador por ID")
    @GetMapping("/{id}")
    public ResponseEntity<Employee> buscarPorId(@PathVariable Long id) {
        Employee employee = employeeService.buscarPorId(id);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

}
