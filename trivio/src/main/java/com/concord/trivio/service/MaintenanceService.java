package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.dto.MaintenanceRequest;
import com.concord.trivio.dto.MaintenanceResponseDTO;

public interface MaintenanceService {

    MaintenanceResponseDTO cadastrar(MaintenanceRequest maintenanceRequest);

    MaintenanceResponseDTO atualizar(Long id, MaintenanceRequest maintenanceRequest);

    List<MaintenanceResponseDTO> listar();

    MaintenanceResponseDTO buscarPorId(Long id);

    List<MaintenanceResponseDTO> listarPorEmployee(Long employeeId); // novo
}