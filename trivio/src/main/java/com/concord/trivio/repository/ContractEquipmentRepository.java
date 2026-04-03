package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concord.trivio.entity.ContractEquipment;

import java.util.Set;

@Repository
public interface ContractEquipmentRepository extends JpaRepository<ContractEquipment, Long> {
    
    Set<ContractEquipment> findByContractId(Long contractId);
}
