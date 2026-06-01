package com.concord.trivio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.concord.trivio.dto.MaintenanceNotificationDTO;
import com.concord.trivio.dto.MaintenanceRequest;
import com.concord.trivio.dto.MaintenanceResponseDTO;
import com.concord.trivio.entity.Employee;
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
    public ResponseEntity<Void> cadastrar(
            @AuthenticationPrincipal Employee authenticatedEmployee,
            @Valid @RequestBody MaintenanceRequest maintenanceRequest) {
        requireManager(authenticatedEmployee);
        maintenanceService.cadastrar(maintenanceRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Atualiza uma manutenção existente. Se status for COMPLETED, retorna nextMaintenanceSuggestion com a sugestão da próxima manutenção a ser confirmada pelo usuário.")
    @PatchMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> atualizar(
            @AuthenticationPrincipal Employee authenticatedEmployee,
            @PathVariable Long id,
            @Valid @RequestBody MaintenanceRequest maintenanceRequest) {
        requireAssignedTechnicianOrManager(authenticatedEmployee, id);
        return ResponseEntity.ok(maintenanceService.atualizar(id, maintenanceRequest));
    }

    @Operation(summary = "Confirma a criação da próxima manutenção a partir de uma manutenção concluída")
    @PostMapping("/{id}/next")
    public ResponseEntity<MaintenanceResponseDTO> gerarProxima(
            @AuthenticationPrincipal Employee authenticatedEmployee,
            @PathVariable Long id) {
        requireManager(authenticatedEmployee);
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.gerarProxima(id));
    }

    @Operation(summary = "Lista manutenções. Se informado employeeId, filtra pelo técnico.")
    @GetMapping
    public ResponseEntity<List<MaintenanceResponseDTO>> listar(
            @AuthenticationPrincipal Employee authenticatedEmployee,
            @RequestParam(required = false) Long employeeId) {

        if (!isManager(authenticatedEmployee)) {
            return ResponseEntity.ok(maintenanceService.listarPorEmployee(authenticatedEmployee.getEmployeeId()));
        }

        if (employeeId != null) {
            return ResponseEntity.ok(maintenanceService.listarPorEmployee(employeeId));
        }
        return ResponseEntity.ok(maintenanceService.listar());
    }

    @Operation(summary = "Lista notificações de manutenções do técnico com 7, 3 e 1 dia de antecedência")
    @GetMapping("/notifications")
    public ResponseEntity<List<MaintenanceNotificationDTO>> listarNotificacoes(
            @AuthenticationPrincipal Employee authenticatedEmployee,
            @RequestParam(required = false) Long employeeId) {
        Long resolvedEmployeeId = isManager(authenticatedEmployee)
                ? employeeId
                : authenticatedEmployee.getEmployeeId();
        return ResponseEntity.ok(maintenanceService.listarNotificacoesPorEmployee(resolvedEmployeeId));
    }

    @Operation(summary = "Busca uma manutenção pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponseDTO> buscarPorId(
            @AuthenticationPrincipal Employee authenticatedEmployee,
            @PathVariable Long id) {
        MaintenanceResponseDTO response = maintenanceService.buscarPorId(id);
        if (!isManager(authenticatedEmployee) && !isAssignedTo(response, authenticatedEmployee.getEmployeeId())) {
            throw new AccessDeniedException("Técnico sem acesso à manutenção");
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private void requireManager(Employee employee) {
        if (!isManager(employee)) {
            throw new AccessDeniedException("Acesso permitido apenas para gestores");
        }
    }

    private void requireAssignedTechnicianOrManager(Employee employee, Long maintenanceId) {
        if (employee == null) {
            throw new AccessDeniedException("Usuário não autenticado");
        }

        if (isManager(employee)) {
            return;
        }

        MaintenanceResponseDTO response = maintenanceService.buscarPorId(maintenanceId);
        if (!isAssignedTo(response, employee.getEmployeeId())) {
            throw new AccessDeniedException("Técnico sem acesso à manutenção");
        }
    }

    private boolean isManager(Employee employee) {
        return employee != null && employee.isAdmin();
    }

    private boolean isAssignedTo(MaintenanceResponseDTO maintenance, Long employeeId) {
        return maintenance.getEmployees() != null
                && maintenance.getEmployees().stream()
                        .anyMatch(employee -> employee.getEmployeeId().equals(employeeId));
    }
}
