package com.concord.trivio.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.concord.trivio.entity.Contract;
import com.concord.trivio.entity.ContractEquipment;
import com.concord.trivio.entity.Equipment;
import com.concord.trivio.repository.ContractEquipmentRepository;

@Service
public class ContractEquipmentServiceImpl implements ContractEquipmentService {

    private final ContractEquipmentRepository contractEquipmentRepository;
    private final EquipmentService equipmentService;

    public ContractEquipmentServiceImpl(ContractEquipmentRepository contractEquipmentRepository, EquipmentService equipmentService) {
        this.contractEquipmentRepository = contractEquipmentRepository;
        this.equipmentService = equipmentService;
    }

    @Override
    public Set<ContractEquipment> buscarPorContratoId(Long contractId) {
        return contractEquipmentRepository.findByContractId(contractId);
    }

    @Override
    @Transactional
    public void cadastrar(ContractEquipment contractEquipment) {
        contractEquipmentRepository.save(contractEquipment);
    }

    @Override
    @Transactional
    public void desativar(ContractEquipment contractEquipment) {
        contractEquipment.setActive(false);
        contractEquipmentRepository.save(contractEquipment);
    }

    @Override
    @Transactional
    public void sincronizarEquipamentos(Contract contrato, Set<Long> novasIds) {
        Set<ContractEquipment> linksAtuais = buscarPorContratoId(contrato.getId());
        Set<Long> idsRecebidas = novasIds == null ? Set.of() : novasIds;

        for (ContractEquipment link : linksAtuais) {
            Long linkEqId = link.getEquipment().getId_equipment();
            if (!idsRecebidas.contains(linkEqId)) {
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
                .anyMatch(link -> link.getEquipment().getId_equipment().equals(novoId));
            
            if (!jaExiste) {
                Equipment eq = equipmentService.buscarPorId(novoId);
                ContractEquipment ce = new ContractEquipment();
                ce.setContract(contrato);
                ce.setEquipment(eq);
                ce.setActive(true);
                cadastrar(ce);
            }
        }
    }
}
