package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.dto.ContractRequest;
import com.concord.trivio.dto.ContractResponseDTO;

public interface ContractService {

    public ContractResponseDTO cadastrar(ContractRequest contractRequest);

    public ContractResponseDTO atualizar(Long id, ContractRequest contractRequest);

    public List<ContractResponseDTO> listar();

    public ContractResponseDTO buscarPorId(Long id);
}
