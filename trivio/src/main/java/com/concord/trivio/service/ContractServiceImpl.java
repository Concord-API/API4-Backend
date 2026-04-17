package com.concord.trivio.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.dto.ContractRequest;
import com.concord.trivio.dto.ContractResponseDTO;
import com.concord.trivio.entity.Client;
import com.concord.trivio.entity.Contract;
import com.concord.trivio.entity.Equipment;
import com.concord.trivio.entity.Requirement;
import com.concord.trivio.entity.ContractEquipment;
import com.concord.trivio.entity.ContractRequirement;
import com.concord.trivio.repository.ContractRepository;

import jakarta.transaction.Transactional;

@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ClientService clientService;
    private final ContractEquipmentService contractEquipmentService;
    private final ContractRequirementService contractRequirementService;

    public ContractServiceImpl(ContractRepository contractRepository,
                               ClientService clientService,
                               ContractEquipmentService contractEquipmentService,
                               ContractRequirementService contractRequirementService) {
        this.contractRepository = contractRepository;
        this.clientService = clientService;
        this.contractEquipmentService = contractEquipmentService;
        this.contractRequirementService = contractRequirementService;
    }

    @Override
    @Transactional
    public ContractResponseDTO cadastrar(ContractRequest contractRequest) {
        if (contractRequest == null ||
            contractRequest.getClientId() == null ||
            contractRequest.getInitialDate() == null ||
            contractRequest.getFinalDate() == null ||
            contractRequest.getRecurrenceMaintenance() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contrato com informações inválidas");
        }

        Contract contract = new Contract();
        contract.setClient(buscarClientePorId(contractRequest.getClientId()));
        contract.setInitialDate(contractRequest.getInitialDate());
        contract.setFinalDate(contractRequest.getFinalDate());
        contract.setRecurrenceMaintenance(contractRequest.getRecurrenceMaintenance());
        contract.setActive(definirActive(contractRequest.getActive()));

        contract = contractRepository.save(contract);

        contractEquipmentService.sincronizarEquipamentos(contract, contractRequest.getEquipmentIds());
        contractRequirementService.sincronizarRequisitos(contract, contractRequest.getRequirementIds());

        return buscarPorId(contract.getId());
    }

    @Override
    @Transactional
    public ContractResponseDTO atualizar(Long id, ContractRequest contractRequest) {
        Contract existente = buscarEntidadePorId(id);

        if (contractRequest == null ||
            contractRequest.getClientId() == null ||
            contractRequest.getInitialDate() == null ||
            contractRequest.getFinalDate() == null ||
            contractRequest.getRecurrenceMaintenance() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contrato com informações inválidas");
        }

        existente.setClient(buscarClientePorId(contractRequest.getClientId()));
        existente.setInitialDate(contractRequest.getInitialDate());
        existente.setFinalDate(contractRequest.getFinalDate());
        existente.setRecurrenceMaintenance(contractRequest.getRecurrenceMaintenance());
        existente.setActive(definirActive(contractRequest.getActive()));

        existente = contractRepository.save(existente);

        contractEquipmentService.sincronizarEquipamentos(existente, contractRequest.getEquipmentIds());
        contractRequirementService.sincronizarRequisitos(existente, contractRequest.getRequirementIds());

        return buscarPorId(existente.getId());
    }

    @Override
    public List<ContractResponseDTO> listar() {
        return contractRepository.findAllWithEquipments().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public ContractResponseDTO buscarPorId(Long id) {
        Contract contract = buscarEntidadeComEquipamentos(id);
        return toDto(contract);
    }

    private Contract buscarEntidadePorId(Long id) {
        return contractRepository.findById(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado")
        );
    }

    private Contract buscarEntidadeComEquipamentos(Long id) {
        return contractRepository.findByIdWithEquipments(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado")
        );
    }

    private Client buscarClientePorId(Long id) {
        try {
            return clientService.buscarPorId(id);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado");
        }
    }

    private Boolean definirActive(Boolean active) {
        return !Boolean.FALSE.equals(active);
    }

    private ContractResponseDTO toDto(Contract contract) {
        ContractResponseDTO dto = new ContractResponseDTO();
        dto.setId(contract.getId());
        dto.setClient(contract.getClient());
        dto.setInitialDate(contract.getInitialDate());
        dto.setFinalDate(contract.getFinalDate());
        dto.setRecurrenceMaintenance(contract.getRecurrenceMaintenance());
        dto.setActive(contract.getActive());

        Set<ContractEquipment> equipLinks = contract.getEquipments();
        if (equipLinks != null) {
            List<Equipment> equipamentosAtivos = equipLinks.stream()
                .filter(ContractEquipment::getActive)
                .map(ContractEquipment::getEquipment)
                .collect(Collectors.toList());
            dto.setEquipments(equipamentosAtivos);
        } else {
            dto.setEquipments(List.of());
        }

        Set<ContractRequirement> reqLinks = contractRequirementService.buscarPorContratoId(contract.getId());
        if (reqLinks != null) {
            List<Requirement> requisitosAtivos = reqLinks.stream()
                .filter(ContractRequirement::getActive)
                .map(ContractRequirement::getRequirement)
                .collect(Collectors.toList());
            dto.setRequirements(requisitosAtivos);
        } else {
            dto.setRequirements(List.of());
        }

        return dto;
    }
}
