package com.concord.trivio.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.concord.trivio.entity.Maintenance;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {

    @Query("SELECT DISTINCT m FROM Maintenance m LEFT JOIN FETCH m.employees me LEFT JOIN FETCH me.employee WHERE m.id = :id")
    Optional<Maintenance> findByIdWithEmployees(@Param("id") Long id);

    @Query("SELECT DISTINCT m FROM Maintenance m LEFT JOIN FETCH m.employees me LEFT JOIN FETCH me.employee WHERE m.active = true")
    List<Maintenance> findAllWithEmployees();

    @Query("""
        SELECT DISTINCT m FROM Maintenance m
        LEFT JOIN FETCH m.employees me
        LEFT JOIN FETCH me.employee e
        WHERE me.employee.employeeId = :employeeId
        AND me.active = true
        AND m.active = true
    """)
    List<Maintenance> findByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " + "FROM Maintenance m " + "WHERE m.contract.id = :contractId AND m.date > :date AND m.active = true")
    boolean existsNextMaintenance(@Param("contractId") Long contractId, @Param("date") LocalDate date);
}
