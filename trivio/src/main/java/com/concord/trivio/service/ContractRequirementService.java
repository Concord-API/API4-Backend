package com.concord.trivio.service;

import com.concord.trivio.entity.Contract;
import com.concord.trivio.entity.ContractRequirement;

import java.util.Set;

public interface ContractRequirementService {

    Set<ContractRequirement> buscarPorContratoId(Long contractId);

    void cadastrar(ContractRequirement contractRequirement);

    void desativar(ContractRequirement contractRequirement);

    void sincronizarRequisitos(Contract contrato, Set<Long> requirementIds);
}
