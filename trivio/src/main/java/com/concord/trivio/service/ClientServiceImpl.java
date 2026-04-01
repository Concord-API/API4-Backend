package com.concord.trivio.service;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.concord.trivio.entity.Client;
import com.concord.trivio.repository.ClientRepository;
import jakarta.transaction.Transactional;

@Service
public class ClientServiceImpl implements ClientService {

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public Client cadastrar(Client client) {
        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public Client alterar(Long id, Client client) {
        Client existente = buscarPorId(id);
        existente.setName(client.getName());
        existente.setCpf(client.getCpf());
        existente.setCnpj(client.getCnpj());
        existente.setEmail(client.getEmail());
        existente.setPhone(client.getPhone());
        existente.setActive(client.getActive());
        return clientRepository.save(existente);
    }

    @Override
    public List<Client> listar() {
        return clientRepository.findAll();
    }

    @Override
    public Client buscarPorId(Long id) {
        return clientRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }
}