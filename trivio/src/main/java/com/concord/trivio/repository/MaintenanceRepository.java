package com.concord.trivio.repository;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

import com.concord.trivio.entity.Maintenance;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    
    @Query("SELECT DISTINCT m FROM Maintenance m LEFT JOIN FETCH m.employees me LEFT JOIN FETCH me.employee WHERE m.id = :id")
    Optional<Maintenance> findByIdWithEmployees(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Maintenance m LEFT JOIN FETCH m.employees me LEFT JOIN FETCH me.employee")
    List<Maintenance> findAllWithEmployees();  

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
       "FROM Maintenance m " +
       "WHERE m.contract.id = :contractId AND m.date > :date AND m.active = true")
    boolean existsNextMaintenance(@Param("contractId") Long contractId,
                               @Param("date") LocalDate date);
}


