package com.concord.trivio.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.dto.RequirementRequest;
import com.concord.trivio.entity.Requirement;
import com.concord.trivio.repository.RequirementRepository;

import jakarta.transaction.Transactional;

@Service
public class RequirementServiceImpl implements RequirementService {

    private final RequirementRepository requirementRepository;

    public RequirementServiceImpl(RequirementRepository requirementRepository) {
        this.requirementRepository = requirementRepository;
    }

    @Override
    @Transactional
    public Requirement cadastrar(RequirementRequest request) {
        if (request == null ||request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requisito com informações inválidas");
        }

        Requirement requirement = new Requirement();
        requirement.setName(request.getName());
        requirement.setDescription(request.getDescription());
        requirement.setActive(definirActive(request.getActive()));

        return requirementRepository.save(requirement);
    }

    @Override
    @Transactional
    public Requirement atualizar(Long id, RequirementRequest request) {
        Requirement existente = buscarPorId(id);

        if (request == null ||
                request.getName() == null || request.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Requisito com informações inválidas");
        }

        existente.setName(request.getName());
        existente.setDescription(request.getDescription());
        existente.setActive(definirActive(request.getActive()));

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

    private Boolean definirActive(Boolean active) {
        return !Boolean.FALSE.equals(active);
    }
}
