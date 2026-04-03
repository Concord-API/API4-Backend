package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concord.trivio.entity.MaintenanceEmployee;

import java.util.Set;

@Repository
public interface MaintenanceEmployeeRepository extends JpaRepository<MaintenanceEmployee, Long> {
    
    Set<MaintenanceEmployee> findByMaintenanceId(Long maintenanceId);
}
