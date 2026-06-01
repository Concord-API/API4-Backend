package com.concord.trivio.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.dto.MaintenanceNotificationDTO;
import com.concord.trivio.dto.MaintenanceRequest;
import com.concord.trivio.dto.MaintenanceResponseDTO;
import com.concord.trivio.dto.NextMaintenanceSuggestionDTO;
import com.concord.trivio.entity.Contract;
import com.concord.trivio.entity.Employee;
import com.concord.trivio.entity.Maintenance;
import com.concord.trivio.entity.MaintenanceEmployee;
import com.concord.trivio.entity.MaintenanceStatus;
import com.concord.trivio.entity.MaintenanceType;
import com.concord.trivio.repository.ContractRepository;
import com.concord.trivio.repository.MaintenanceRepository;
import com.concord.trivio.observer.MaintenancePublisher;

import jakarta.transaction.Transactional;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private static final ZoneId NOTIFICATION_ZONE = ZoneId.of("America/Sao_Paulo");

    private final MaintenanceRepository maintenanceRepository;
    private final ContractRepository contractRepository;
    private final MaintenanceEmployeeService maintenanceEmployeeService;
    private final MaintenancePublisher maintenancePublisher;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository,
                                  ContractRepository contractRepository,
                                  MaintenanceEmployeeService maintenanceEmployeeService,
                                  MaintenancePublisher maintenancePublisher) {
        this.maintenanceRepository = maintenanceRepository;
        this.contractRepository = contractRepository;
        this.maintenanceEmployeeService = maintenanceEmployeeService;
        this.maintenancePublisher = maintenancePublisher;
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

        Contract contract = buscarContractPorId(maintenanceRequest.getContractId());

        Maintenance maintenance = new Maintenance();
        maintenance.setContract(contract);
        maintenance.setDate(maintenanceRequest.getDate());
        maintenance.setPreventive(maintenanceRequest.getPreventive());
        maintenance.setType(maintenanceRequest.getType());
        maintenance.setStatus(maintenanceRequest.getStatus());
        maintenance.setActive(definirActive(maintenanceRequest.getActive()));
        maintenance.setLatitude(maintenanceRequest.getLatitude() != null
                ? maintenanceRequest.getLatitude()
                : contract.getLatitude());
        maintenance.setLongitude(maintenanceRequest.getLongitude() != null
                ? maintenanceRequest.getLongitude()
                : contract.getLongitude());
        maintenance.setStartTime(maintenanceRequest.getStartTime());
        maintenance.setEndTime(maintenanceRequest.getEndTime());

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

        if (maintenanceRequest.getLatitude() != null) {
            existente.setLatitude(maintenanceRequest.getLatitude());
        }

        if (maintenanceRequest.getLongitude() != null) {
            existente.setLongitude(maintenanceRequest.getLongitude());
        }

        if (maintenanceRequest.getStartTime() != null) {
            existente.setStartTime(maintenanceRequest.getStartTime());
        }

        if (maintenanceRequest.getEndTime() != null) {
            existente.setEndTime(maintenanceRequest.getEndTime());
        }

        existente = maintenanceRepository.save(existente);

        maintenanceEmployeeService.sincronizarEmployees(existente, maintenanceRequest.getEmployeeIds());

        MaintenanceResponseDTO dto = buscarPorId(existente.getId());

        if (existente.getStatus() == MaintenanceStatus.COMPLETED) {
            dto.setNextMaintenanceSuggestion(buildSuggestion(existente));
            // notify observers to create the next maintenance automatically
            maintenancePublisher.notifyObservers(existente);
        }

        return dto;
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

    @Override
    public List<MaintenanceResponseDTO> listarPorEmployee(Long employeeId) {
        return maintenanceRepository.findByEmployeeId(employeeId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceNotificationDTO> listarNotificacoesPorEmployee(Long employeeId) {
        if (employeeId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Técnico não informado");
        }

        LocalDate hoje = LocalDate.now(NOTIFICATION_ZONE);
        List<LocalDate> datasAlvo = List.of(
                hoje.plusDays(1),
                hoje.plusDays(3),
                hoje.plusDays(7)
        );

        return maintenanceRepository.findScheduledNotificationsByEmployeeId(
                        employeeId,
                        MaintenanceStatus.SCHEDULED,
                        datasAlvo
                ).stream()
                .map(maintenance -> toNotificationDto(maintenance, hoje))
                .sorted(Comparator
                        .comparing(MaintenanceNotificationDTO::getDaysUntilMaintenance)
                        .thenComparing(MaintenanceNotificationDTO::getMaintenanceDate))
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
        dto.setLatitude(maintenance.getLatitude());
        dto.setLongitude(maintenance.getLongitude());
        dto.setStartTime(maintenance.getStartTime());
        dto.setEndTime(maintenance.getEndTime());

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

    private MaintenanceNotificationDTO toNotificationDto(Maintenance maintenance, LocalDate hoje) {
        MaintenanceNotificationDTO dto = new MaintenanceNotificationDTO();
        dto.setMaintenanceId(maintenance.getId());
        dto.setContractId(maintenance.getContract().getId());
        dto.setClientName(maintenance.getContract().getClient().getName());
        dto.setMaintenanceDate(maintenance.getDate());
        dto.setDaysUntilMaintenance((int) ChronoUnit.DAYS.between(hoje, maintenance.getDate()));
        dto.setType(maintenance.getType());
        dto.setStatus(maintenance.getStatus());
        return dto;
    }

    @Override
    @Transactional
    public MaintenanceResponseDTO gerarProxima(Long id) {
        Maintenance concluida = buscarEntidadePorId(id);

        if (concluida.getStatus() != MaintenanceStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Só é possível gerar próxima manutenção a partir de uma manutenção concluída");
        }

        if (Boolean.TRUE.equals(concluida.getNextGenerated())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "A próxima manutenção já foi gerada a partir desta");
        }

        long recorrencia = concluida.getContract().getRecurrenceMaintenance();

        Maintenance proxima = new Maintenance();
        proxima.setContract(concluida.getContract());
        proxima.setDate(concluida.getDate().plusDays(recorrencia));
        proxima.setType(MaintenanceType.PREVENTIVA);
        proxima.setStatus(MaintenanceStatus.SCHEDULED);
        proxima.setPreventive(true);
        proxima.setActive(true);
        proxima.setLatitude(concluida.getLatitude());
        proxima.setLongitude(concluida.getLongitude());

        proxima = maintenanceRepository.save(proxima);

        concluida.setNextGenerated(true);
        maintenanceRepository.save(concluida);

        return buscarPorId(proxima.getId());
    }

    private NextMaintenanceSuggestionDTO buildSuggestion(Maintenance maintenance) {
        long recorrencia = maintenance.getContract().getRecurrenceMaintenance();
        return new NextMaintenanceSuggestionDTO(
                maintenance.getContract().getId(),
                maintenance.getDate().plusDays(recorrencia),
                MaintenanceType.PREVENTIVA,
                MaintenanceStatus.SCHEDULED,
                true,
                maintenance.getLatitude(),
                maintenance.getLongitude()
        );
    }

    private Boolean definirActive(Boolean active) {
        return !Boolean.FALSE.equals(active);
    }
}