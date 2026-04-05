package com.concord.trivio.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concord.trivio.entity.ContractRequirement;

@Repository
public interface ContractRequirementRepository extends JpaRepository<ContractRequirement, Long> {

    Set<ContractRequirement> findByContract_Id(Long contractId);
}
