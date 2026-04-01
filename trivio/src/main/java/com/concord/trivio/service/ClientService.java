package com.concord.trivio.service;

import java.util.List;
import com.concord.trivio.dto.ClientRequest;
import com.concord.trivio.entity.Client;

public interface ClientService {

    public Client cadastrar(ClientRequest clientRequest);

    public Client alterar(Long id, ClientRequest clientRequest);

    public List<Client> listar();
    
    public Client buscarPorId(Long id);
}