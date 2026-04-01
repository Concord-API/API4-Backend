package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.dto.ContractRequest;
import com.concord.trivio.entity.Contract;

public interface ContractService {

    public Contract cadastrar(ContractRequest contractRequest);

    public Contract atualizar(Long id, ContractRequest contractRequest);

    public List<Contract> listar();

    public Contract buscarPorId(Long id);
}
