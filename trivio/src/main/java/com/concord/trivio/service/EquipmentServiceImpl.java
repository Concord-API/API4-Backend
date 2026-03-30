package com.concord.trivio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concord.trivio.entity.Equipment;
import com.concord.trivio.repository.EquipmentRepository;

import java.util.List;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public List<Equipment> listar() {
        return equipmentRepository.findAll();
}
  
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

    @Override
    public Equipment buscarPorId(Long id) {
        return equipmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Equipment não encontrado"));
    }
}