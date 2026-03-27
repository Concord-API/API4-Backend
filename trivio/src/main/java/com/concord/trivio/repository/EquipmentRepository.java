package com.concord.trivio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concord.trivio.entity.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

}