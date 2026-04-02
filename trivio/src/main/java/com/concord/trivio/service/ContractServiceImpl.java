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
import com.concord.trivio.entity.ContractEquipment;
import com.concord.trivio.repository.ContractRepository;

import jakarta.transaction.Transactional;

@Service
public class ContractServiceImpl implements ContractService {

    private ContractRepository contractRepository;
    private ClientService clientService;
    private ContractEquipmentService contractEquipmentService;

    public ContractServiceImpl(ContractRepository contractRepository, 
                               ClientService clientService, 
                               ContractEquipmentService contractEquipmentService) {
        this.contractRepository = contractRepository;
        this.clientService = clientService;
        this.contractEquipmentService = contractEquipmentService;
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
        contract.setClient(buscarClientPorId(contractRequest.getClientId()));
        contract.setInitialDate(contractRequest.getInitialDate());
        contract.setFinalDate(contractRequest.getFinalDate());
        contract.setRecurrenceMaintenance(contractRequest.getRecurrenceMaintenance());
        contract.setActive(definirActive(contractRequest.getActive()));

        contract = contractRepository.save(contract);

        contractEquipmentService.sincronizarEquipamentos(contract, contractRequest.getEquipmentIds());

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

        existente.setClient(buscarClientPorId(contractRequest.getClientId()));
        existente.setInitialDate(contractRequest.getInitialDate());
        existente.setFinalDate(contractRequest.getFinalDate());
        existente.setRecurrenceMaintenance(contractRequest.getRecurrenceMaintenance());
        existente.setActive(definirActive(contractRequest.getActive()));

        existente = contractRepository.save(existente);

        contractEquipmentService.sincronizarEquipamentos(existente, contractRequest.getEquipmentIds());

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

    private Client buscarClientPorId(Long id) {
        try {
            return clientService.buscarPorId(id);
        } catch (RuntimeException ex) {
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

        Set<ContractEquipment> links = contract.getEquipments();
        
        if (links != null) {
            List<Equipment> equipamentosAtivos = links.stream()
                .filter(ContractEquipment::getActive)
                .map(ContractEquipment::getEquipment)
                .collect(Collectors.toList());
                
            dto.setEquipments(equipamentosAtivos);
        } else {
            dto.setEquipments(List.of());
        }

        return dto;
    }
}
