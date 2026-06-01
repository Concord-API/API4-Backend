package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.dto.ChecklistRequest;
import com.concord.trivio.dto.ChecklistResponseDTO;
import com.concord.trivio.entity.Checklist;

public interface ChecklistService {

    Checklist cadastrar(ChecklistRequest request);

    Checklist atualizar(Long id, ChecklistRequest request);

    void deletar(Long id);

    List<ChecklistResponseDTO> listarPorMaintenance(Long maintenanceId);

}
