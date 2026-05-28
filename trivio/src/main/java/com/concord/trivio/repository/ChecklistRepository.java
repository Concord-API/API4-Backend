package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concord.trivio.entity.Checklist;

public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

}
