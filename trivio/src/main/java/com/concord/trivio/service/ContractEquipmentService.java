package com.concord.trivio.service;

import com.concord.trivio.entity.Contract;
import com.concord.trivio.entity.ContractEquipment;

import java.util.Set;

public interface ContractEquipmentService {
    
    Set<ContractEquipment> buscarPorContratoId(Long contractId);
    
    void cadastrar(ContractEquipment contractEquipment);

    void desativar(ContractEquipment contractEquipment);

    void sincronizarEquipamentos(Contract contrato, Set<Long> equipmentIds);
}
