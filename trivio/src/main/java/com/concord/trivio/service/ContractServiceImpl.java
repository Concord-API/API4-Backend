package com.concord.trivio.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.dto.ContractRequest;
import com.concord.trivio.entity.Client;
import com.concord.trivio.entity.Contract;
import com.concord.trivio.repository.ContractRepository;

import jakarta.transaction.Transactional;

@Service
public class ContractServiceImpl implements ContractService {

    private ContractRepository contractRepository;

    private ClientService clientService;

    public ContractServiceImpl(ContractRepository contractRepository, ClientService clientService) {
        this.contractRepository = contractRepository;
        this.clientService = clientService;
    }

    @Override
    @Transactional
    public Contract cadastrar(ContractRequest contractRequest) {
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

        return contractRepository.save(contract);
    }

    @Override
    @Transactional
    public Contract atualizar(Long id, ContractRequest contractRequest) {
        Contract existente = buscarPorId(id);
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

        return contractRepository.save(existente);
    }

    @Override
    public List<Contract> listar() {
        return contractRepository.findAll();
    }

    @Override
    public Contract buscarPorId(Long id) {
        return contractRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado");
        });
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
}
