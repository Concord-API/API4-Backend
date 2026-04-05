package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.dto.RequirementRequest;
import com.concord.trivio.entity.Requirement;

public interface RequirementService {

    Requirement cadastrar(RequirementRequest request);

    Requirement atualizar(Long id, RequirementRequest request);

    List<Requirement> listar();

    Requirement buscarPorId(Long id);
}
