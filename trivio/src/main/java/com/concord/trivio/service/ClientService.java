package com.concord.trivio.service;

import com.concord.trivio.entity.Client;
import java.util.List;

public interface ClientService {
    public List<Client> listar();
    public Client buscarPorId(Long id);
}