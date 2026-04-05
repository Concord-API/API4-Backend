package com.concord.trivio.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.dto.RequirementRequest;
import com.concord.trivio.entity.Requirement;
import com.concord.trivio.repository.RequirementRepository;

@Service
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository requirementRepository;

    public RequirementServiceImpl(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    @Override
    @Transactional
    public Requirement cadastrar(RequirementRequest request) {
        if (request == null || request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do requisito é obrigatório");
        }

        Requirement requirement = new Requirement();
        requirement.setName(request.getName());
        requirement.setDescription(request.getDescription());
        requirement.setActive(request.getActive() != null ? request.getActive() : true);

        return requirementRepository.save(requirement);
    }

    @Override
    @Transactional
    public Requirement atualizar(Long id, RequirementRequest request) {
        Requirement existente = requirementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Requisito não encontrado"));

        if (request.getName() != null && !request.getName().isBlank()) {
            existente.setName(request.getName());
        }
        if (request.getDescription() != null) {
            existente.setDescription(request.getDescription());
        }
        if (request.getActive() != null) {
            existente.setActive(request.getActive());
        }

        return requirementRepository.save(existente);
    }

    @Override
    public List<Requirement> listar() {
        return requirementRepository.findAll();
    }

    @Override
    public Requirement buscarPorId(Long id) {
        return requirementRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Requisito não encontrado"));
    }
}
