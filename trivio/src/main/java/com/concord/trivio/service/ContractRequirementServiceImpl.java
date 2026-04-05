package com.concord.trivio.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.concord.trivio.entity.Contract;
import com.concord.trivio.entity.ContractRequirement;
import com.concord.trivio.entity.Requirement;
import com.concord.trivio.repository.ContractRequirementRepository;

@Service
public class ContractRequirementServiceImpl implements ContractRequirementService {

    private final ContractRequirementRepository contractRequirementRepository;
    private final RequirementService requirementService;

    public ContractRequirementServiceImpl(
            ContractRequirementRepository contractRequirementRepository,
            RequirementService requirementService) {
        this.contractRequirementRepository = contractRequirementRepository;
        this.requirementService = requirementService;
    }

    @Override
    public Set<ContractRequirement> buscarPorContratoId(Long contractId) {
        return contractRequirementRepository.findByContract_Id(contractId);
    }

    @Override
    @Transactional
    public void cadastrar(ContractRequirement contractRequirement) {
        contractRequirementRepository.save(contractRequirement);
    }

    @Override
    @Transactional
    public void desativar(ContractRequirement contractRequirement) {
        contractRequirement.setActive(false);
        contractRequirementRepository.save(contractRequirement);
    }

    @Override
    @Transactional
    public void sincronizarRequisitos(Contract contrato, Set<Long> novasIds) {
        Set<ContractRequirement> linksAtuais = buscarPorContratoId(contrato.getId());
        Set<Long> idsRecebidas = novasIds == null ? Set.of() : novasIds;

        for (ContractRequirement link : linksAtuais) {
            Long linkReqId = link.getRequirement().getId();
            if (!idsRecebidas.contains(linkReqId)) {
                desativar(link);
            } else {
                if (!link.getActive()) {
                    link.setActive(true);
                    cadastrar(link);
                }
            }
        }

        for (Long novoId : idsRecebidas) {
            boolean jaExiste = linksAtuais.stream()
                .anyMatch(link -> link.getRequirement().getId().equals(novoId));

            if (!jaExiste) {
                Requirement req = requirementService.buscarPorId(novoId);
                ContractRequirement cr = new ContractRequirement();
                cr.setContract(contrato);
                cr.setRequirement(req);
                cr.setActive(true);
                cadastrar(cr);
            }
        }
    }
}
