package com.concord.trivio.service;

import com.concord.trivio.entity.Equipment;
import java.util.List;

public interface EquipmentService {
  
    public List<Equipment> listar();
  
    public Equipment buscarPorId(Long id);

    public Equipment atualizar(Long id, Equipment equipment);

    public Equipment cadastrar(Equipment equipment);

}