package com.concord.trivio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concord.trivio.entity.Equipment;
import com.concord.trivio.repository.EquipmentRepository;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public Equipment cadastrar(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

}