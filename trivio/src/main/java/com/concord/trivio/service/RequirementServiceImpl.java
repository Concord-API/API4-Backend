package com.concord.trivio.service;

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
}
