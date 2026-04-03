package com.concord.trivio.service;

import com.concord.trivio.entity.Maintenance;
import com.concord.trivio.entity.MaintenanceEmployee;

import java.util.Set;

public interface MaintenanceEmployeeService {
    
    Set<MaintenanceEmployee> buscarPorMaintenanceId(Long maintenanceId);
    
    void cadastrar(MaintenanceEmployee maintenanceEmployee);

    void desativar(MaintenanceEmployee maintenanceEmployee);

    void sincronizarEmployees(Maintenance maintenance, Set<Long> employeeIds);
}
