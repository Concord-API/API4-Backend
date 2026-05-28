package com.concord.trivio.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.dto.ChecklistRequest;
import com.concord.trivio.entity.Checklist;
import com.concord.trivio.entity.Maintenance;
import com.concord.trivio.repository.ChecklistRepository;
import com.concord.trivio.repository.MaintenanceRepository;

import jakarta.transaction.Transactional;

@Service
public class ChecklistServiceImpl implements ChecklistService {

    private final ChecklistRepository checklistRepository;
    private final MaintenanceRepository maintenanceRepository;

    public ChecklistServiceImpl(ChecklistRepository checklistRepository,
                                MaintenanceRepository maintenanceRepository) {
        this.checklistRepository = checklistRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    @Transactional
    public Checklist cadastrar(ChecklistRequest request) {
        if (request == null || request.getMaintenanceId() == null || request.getDescription() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checklist com informações inválidas");
        }

        Maintenance maintenance = maintenanceRepository.findById(request.getMaintenanceId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Manutenção não encontrada")
        );

        Checklist checklist = new Checklist();
        checklist.setMaintenance(maintenance);
        checklist.setDescription(request.getDescription());
        checklist.setCompleted(request.getCompleted() != null && request.getCompleted());

        return checklistRepository.save(checklist);
    }

    @Override
    @Transactional
    public Checklist atualizar(Long id, ChecklistRequest request) {
        Checklist existente = checklistRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist não encontrado")
        );

        if (request.getMaintenanceId() != null) {
            Maintenance maintenance = maintenanceRepository.findById(request.getMaintenanceId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Manutenção não encontrada")
            );
            existente.setMaintenance(maintenance);
        }

        if (request.getDescription() != null) {
            existente.setDescription(request.getDescription());
        }

        if (request.getCompleted() != null) {
            existente.setCompleted(request.getCompleted());
        }

        return checklistRepository.save(existente);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Checklist existente = checklistRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist não encontrado")
        );

        checklistRepository.delete(existente);
    }
}
