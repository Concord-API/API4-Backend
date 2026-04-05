package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concord.trivio.entity.Requirement;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {
}
