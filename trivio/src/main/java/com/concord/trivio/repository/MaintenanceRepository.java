package com.concord.trivio.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.concord.trivio.entity.Maintenance;
import com.concord.trivio.entity.MaintenanceStatus;

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

    @Query("""
        SELECT DISTINCT m FROM Maintenance m
        JOIN FETCH m.contract c
        JOIN FETCH c.client
        LEFT JOIN FETCH m.employees me
        LEFT JOIN FETCH me.employee e
        WHERE me.employee.employeeId = :employeeId
        AND me.active = true
        AND m.active = true
        AND m.status = :status
        AND m.date IN :dates
    """)
    List<Maintenance> findScheduledNotificationsByEmployeeId(
            @Param("employeeId") Long employeeId,
            @Param("status") MaintenanceStatus status,
            @Param("dates") List<LocalDate> dates
    );
}