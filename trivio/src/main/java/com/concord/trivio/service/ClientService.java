package com.concord.trivio.service;

import java.util.List;

import com.concord.trivio.entity.Client;

public interface ClientService {

    public Client cadastrar(Client client);

    public Client atualizar(Long id, Client client);

    public List<Client> listar();

    public Client buscarPorId(Long id);
}