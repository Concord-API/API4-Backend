package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.concord.trivio.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    
}
