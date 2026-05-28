package com.concord.trivio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.concord.trivio.dto.FollowRequest;
import com.concord.trivio.dto.FollowResponseDTO;
import com.concord.trivio.entity.Employee;
import com.concord.trivio.entity.Follow;
import com.concord.trivio.entity.Maintenance;
import com.concord.trivio.repository.FollowRepository;
import com.concord.trivio.repository.MaintenanceRepository;

import jakarta.transaction.Transactional;

@Service
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;
    private final MaintenanceRepository maintenanceRepository;

    public FollowServiceImpl(FollowRepository followRepository, MaintenanceRepository maintenanceRepository) {
        this.followRepository = followRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    @Transactional
    public FollowResponseDTO cadastrar(FollowRequest followRequest) {
        validarCadastro(followRequest);

        Follow follow = new Follow();
        follow.setMaintenance(buscarMaintenancePorId(followRequest.getMaintenanceId()));
        follow.setEmployee(buscarEmployeeLogado());
        follow.setMessage(followRequest.getMessage());
        follow.setActive(definirActive(followRequest.getActive()));

        follow = followRepository.save(follow);

        return toDto(follow);
    }

    @Override
    @Transactional
    public FollowResponseDTO atualizar(FollowRequest followRequest) {
        validarAtualizacao(followRequest);

        Follow existente = buscarEntidadePorId(followRequest.getId());
        existente.setMessage(followRequest.getMessage());
        existente.setActive(definirActive(followRequest.getActive()));

        existente = followRepository.save(existente);

        return toDto(existente);
    }

    @Override
    public List<FollowResponseDTO> listarPorMaintenance(Long maintenanceId) {
        if (maintenanceId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Manutencao nao informada");
        }

        return followRepository.findByMaintenance_IdAndActiveTrueOrderByCreatedAtAsc(maintenanceId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private void validarCadastro(FollowRequest followRequest) {
        if (followRequest == null ||
            followRequest.getMaintenanceId() == null ||
            followRequest.getMessage() == null ||
            followRequest.getMessage().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Acompanhamento com informacoes invalidas");
        }
    }

    private void validarAtualizacao(FollowRequest followRequest) {
        if (followRequest == null ||
            followRequest.getId() == null ||
            followRequest.getMessage() == null ||
            followRequest.getMessage().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Acompanhamento com informacoes invalidas");
        }
    }

    private Follow buscarEntidadePorId(Long id) {
        return followRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Acompanhamento nao encontrado")
        );
    }

    private Maintenance buscarMaintenancePorId(Long id) {
        return maintenanceRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Manutencao nao encontrada")
        );
    }

    private Employee buscarEmployeeLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Employee employee)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario nao autenticado");
        }

        return employee;
    }

    private Boolean definirActive(Boolean active) {
        return !Boolean.FALSE.equals(active);
    }

    private FollowResponseDTO toDto(Follow follow) {
        FollowResponseDTO dto = new FollowResponseDTO();
        dto.setId(follow.getId());
        dto.setMaintenanceId(follow.getMaintenance().getId());
        dto.setEmployeeId(follow.getEmployee().getEmployeeId());
        dto.setEmployeeName(follow.getEmployee().getName());
        dto.setMessage(follow.getMessage());
        dto.setActive(follow.getActive());
        dto.setCreatedAt(follow.getCreatedAt());
        dto.setUpdatedAt(follow.getUpdatedAt());

        return dto;
    }
}
