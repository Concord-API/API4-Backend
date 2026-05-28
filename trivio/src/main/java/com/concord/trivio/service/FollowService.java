package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.dto.FollowRequest;
import com.concord.trivio.dto.FollowResponseDTO;

public interface FollowService {

    FollowResponseDTO cadastrar(FollowRequest followRequest);

    FollowResponseDTO atualizar(FollowRequest followRequest);

    List<FollowResponseDTO> listarPorMaintenance(Long maintenanceId);
}
