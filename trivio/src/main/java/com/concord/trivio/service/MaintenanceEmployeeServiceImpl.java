package com.concord.trivio.service;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.concord.trivio.entity.Employee;
import com.concord.trivio.entity.Maintenance;
import com.concord.trivio.entity.MaintenanceEmployee;
import com.concord.trivio.repository.MaintenanceEmployeeRepository;

@Service
public class MaintenanceEmployeeServiceImpl implements MaintenanceEmployeeService {

    private final MaintenanceEmployeeRepository maintenanceEmployeeRepository;
    private final EmployeeService employeeService;

    public MaintenanceEmployeeServiceImpl(MaintenanceEmployeeRepository maintenanceEmployeeRepository, EmployeeService employeeService) {
        this.maintenanceEmployeeRepository = maintenanceEmployeeRepository;
        this.employeeService = employeeService;
    }

    @Override
    public Set<MaintenanceEmployee> buscarPorMaintenanceId(Long maintenanceId) {
        return maintenanceEmployeeRepository.findByMaintenanceId(maintenanceId);
    }

    @Override
    @Transactional
    public void cadastrar(MaintenanceEmployee maintenanceEmployee) {
        maintenanceEmployeeRepository.save(maintenanceEmployee);
    }

    @Override
    @Transactional
    public void desativar(MaintenanceEmployee maintenanceEmployee) {
        maintenanceEmployee.setActive(false);
        maintenanceEmployeeRepository.save(maintenanceEmployee);
    }

    @Override
    @Transactional
    public void sincronizarEmployees(Maintenance maintenance, Set<Long> novasIds) {
        Set<MaintenanceEmployee> linksAtuais = buscarPorMaintenanceId(maintenance.getId());
        Set<Long> idsRecebidas = novasIds == null ? Set.of() : novasIds;

        for (MaintenanceEmployee link : linksAtuais) {
            Long linkEmpId = link.getEmployee().getEmployeeId();
            if (!idsRecebidas.contains(linkEmpId)) {
                desativar(link);
            } else {
                if (!link.getActive()) {
                    link.setActive(true);
                    cadastrar(link);
                }
            }
        }

        for (Long novoId : idsRecebidas) {
            boolean jaExiste = linksAtuais.stream()
                .anyMatch(link -> link.getEmployee().getEmployeeId().equals(novoId));
            
            if (!jaExiste) {
                Employee emp = employeeService.buscarPorId(novoId);
                MaintenanceEmployee me = new MaintenanceEmployee();
                me.setMaintenance(maintenance);
                me.setEmployee(emp);
                me.setActive(true);
                cadastrar(me);
            }
        }
    }
}
