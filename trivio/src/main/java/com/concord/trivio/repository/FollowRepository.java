package com.concord.trivio.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.concord.trivio.entity.Follow;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByMaintenance_IdAndActiveTrueOrderByCreatedAtAsc(Long maintenanceId);
}
