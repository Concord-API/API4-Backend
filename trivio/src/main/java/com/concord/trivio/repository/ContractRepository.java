package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.concord.trivio.entity.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    
    @Query("SELECT DISTINCT c FROM Contract c LEFT JOIN FETCH c.equipments ce LEFT JOIN FETCH ce.equipment WHERE c.id = :id")
    Optional<Contract> findByIdWithEquipments(@Param("id") Long id);

    @Query("SELECT DISTINCT c FROM Contract c LEFT JOIN FETCH c.equipments ce LEFT JOIN FETCH ce.equipment")
    List<Contract> findAllWithEquipments();
}
