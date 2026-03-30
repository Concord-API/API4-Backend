package com.concord.trivio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concord.trivio.entity.Client;
import com.concord.trivio.repository.ClientRepository;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> listar() {
        return clientRepository.findAll();
    }

    @Override
    public Client buscarPorId(Long id) {
        return clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client não encontrado"));
    }

    public Client alterar(Long id, Client client) {
        Client existente = clientRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Client não encontrado"));
        existente.setName(client.getName());
        existente.setCpf(client.getCpf());
        existente.setCnpj(client.getCnpj());
        existente.setEmail(client.getEmail());
        existente.setPhone(client.getPhone());
        existente.setActive(client.getActive());
        return clientRepository.save(existente);
    }
  
    public Client cadastrar(Client client) {
        return clientRepository.save(client);
    }
}