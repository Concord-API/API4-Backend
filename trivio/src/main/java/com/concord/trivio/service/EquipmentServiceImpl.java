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
    public Equipment alterar(Long id, Equipment equipment) {
        Equipment existente = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment não encontrado"));

        existente.setName(equipment.getName());
        existente.setModel(equipment.getModel());
        existente.setManufacturer(equipment.getManufacturer());
        existente.setActive(equipment.getActive());

        return equipmentRepository.save(existente);
}
  
    public Equipment cadastrar(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

}