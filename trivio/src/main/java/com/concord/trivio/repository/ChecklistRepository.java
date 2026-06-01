package com.concord.trivio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concord.trivio.entity.Checklist;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    List<Checklist> findByMaintenance_IdOrderByIdAsc(Long maintenanceId);
}
