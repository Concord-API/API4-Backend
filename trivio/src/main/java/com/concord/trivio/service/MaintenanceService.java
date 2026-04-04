package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.dto.MaintenanceRequest;
import com.concord.trivio.dto.MaintenanceResponseDTO;

public interface MaintenanceService {

    public MaintenanceResponseDTO cadastrar(MaintenanceRequest maintenanceRequest);

    public MaintenanceResponseDTO atualizar(Long id, MaintenanceRequest maintenanceRequest);

    public List<MaintenanceResponseDTO> listar();

    public MaintenanceResponseDTO buscarPorId(Long id);
}
