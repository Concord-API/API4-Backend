package com.concord.trivio.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.entity.Equipment;
import com.concord.trivio.repository.EquipmentRepository;

import jakarta.transaction.Transactional;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<Equipment> listar() {
        return equipmentRepository.findAll();
    }

    @Override
    @Transactional
    public Equipment atualizar(Long id, Equipment equipment) {
        Equipment existente = buscarPorId(id);
        if (equipment == null ||
                equipment.getName() == null || equipment.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipamento com informações inválidas");
        }

        existente.setName(equipment.getName());
        existente.setModel(equipment.getModel());
        existente.setManufacturer(equipment.getManufacturer());
        existente.setActive(definirActive(equipment.getActive()));

        return equipmentRepository.save(existente);
    }

    @Override
    @Transactional
    public Equipment cadastrar(Equipment equipment) {
        if (equipment == null ||
                equipment.getName() == null || equipment.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipamento com informações inválidas");
        }
        equipment.setActive(definirActive(equipment.getActive()));
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment buscarPorId(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipamento não encontrado"));
    }

    private Boolean definirActive(Boolean active) {
        return !Boolean.FALSE.equals(active);
    }
}
