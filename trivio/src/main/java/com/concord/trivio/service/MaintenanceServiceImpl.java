package com.concord.trivio.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.dto.MaintenanceRequest;
import com.concord.trivio.dto.MaintenanceResponseDTO;
import com.concord.trivio.entity.Contract;
import com.concord.trivio.entity.Employee;
import com.concord.trivio.entity.Maintenance;
import com.concord.trivio.entity.MaintenanceEmployee;
import com.concord.trivio.repository.ContractRepository;
import com.concord.trivio.repository.MaintenanceRepository;

import jakarta.transaction.Transactional;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final ContractRepository contractRepository;
    private final MaintenanceEmployeeService maintenanceEmployeeService;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository, 
                                  ContractRepository contractRepository, 
                                  MaintenanceEmployeeService maintenanceEmployeeService) {
        this.maintenanceRepository = maintenanceRepository;
        this.contractRepository = contractRepository;
        this.maintenanceEmployeeService = maintenanceEmployeeService;
    }

    @Override
    @Transactional
    public MaintenanceResponseDTO cadastrar(MaintenanceRequest maintenanceRequest) {
        if (maintenanceRequest == null ||
            maintenanceRequest.getContractId() == null ||
            maintenanceRequest.getDate() == null ||
            maintenanceRequest.getPreventive() == null ||
            maintenanceRequest.getType() == null ||
            maintenanceRequest.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manutenção com informações inválidas");
        }

        Maintenance maintenance = new Maintenance();
        maintenance.setContract(buscarContractPorId(maintenanceRequest.getContractId()));
        maintenance.setDate(maintenanceRequest.getDate());
        maintenance.setPreventive(maintenanceRequest.getPreventive());
        maintenance.setType(maintenanceRequest.getType());
        maintenance.setStatus(maintenanceRequest.getStatus());
        maintenance.setActive(definirActive(maintenanceRequest.getActive()));

        maintenance = maintenanceRepository.save(maintenance);

        maintenanceEmployeeService.sincronizarEmployees(maintenance, maintenanceRequest.getEmployeeIds());

        return buscarPorId(maintenance.getId()); 
    }

    @Override
    @Transactional
    public MaintenanceResponseDTO atualizar(Long id, MaintenanceRequest maintenanceRequest) {
        Maintenance existente = buscarEntidadePorId(id);

        if (maintenanceRequest == null ||
            maintenanceRequest.getDate() == null ||
            maintenanceRequest.getPreventive() == null ||
            maintenanceRequest.getType() == null ||
            maintenanceRequest.getStatus() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manutenção com informações inválidas");
        }

        if (maintenanceRequest.getContractId() != null) {
            existente.setContract(buscarContractPorId(maintenanceRequest.getContractId()));
        }
        existente.setDate(maintenanceRequest.getDate());
        existente.setPreventive(maintenanceRequest.getPreventive());
        existente.setType(maintenanceRequest.getType());
        existente.setStatus(maintenanceRequest.getStatus());
        existente.setActive(definirActive(maintenanceRequest.getActive()));

        existente = maintenanceRepository.save(existente);

        maintenanceEmployeeService.sincronizarEmployees(existente, maintenanceRequest.getEmployeeIds());

        return buscarPorId(existente.getId());
    }

    @Override
    public MaintenanceResponseDTO buscarPorId(Long id) {
        Maintenance maintenance = buscarEntidadeComEmployees(id);
        return toDto(maintenance);
    }

    @Override
    public List<MaintenanceResponseDTO> listar() {
        return maintenanceRepository.findAllWithEmployees().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private Maintenance buscarEntidadePorId(Long id) {
        return maintenanceRepository.findById(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Manutenção não encontrada")
        );
    }

    private Maintenance buscarEntidadeComEmployees(Long id) {
        return maintenanceRepository.findByIdWithEmployees(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Manutenção não encontrada")
        );
    }

    private Contract buscarContractPorId(Long id) {
        return contractRepository.findById(id).orElseThrow(() -> 
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato não encontrado")
        );
    }

    private MaintenanceResponseDTO toDto(Maintenance maintenance) {
        MaintenanceResponseDTO dto = new MaintenanceResponseDTO();
        dto.setId(maintenance.getId());
        dto.setContract(maintenance.getContract());
        dto.setDate(maintenance.getDate());
        dto.setPreventive(maintenance.getPreventive());
        dto.setType(maintenance.getType());
        dto.setStatus(maintenance.getStatus());
        dto.setActive(maintenance.getActive());

        Set<MaintenanceEmployee> links = maintenance.getEmployees();
        
        if (links != null) {
            List<Employee> employeesAtivos = links.stream()
                .filter(MaintenanceEmployee::getActive)
                .map(MaintenanceEmployee::getEmployee)
                .collect(Collectors.toList());
                
            dto.setEmployees(employeesAtivos);
        } else {
            dto.setEmployees(List.of());
        }

        return dto;
    }

    private Boolean definirActive(Boolean active) {
        return !Boolean.FALSE.equals(active);
    }
}
