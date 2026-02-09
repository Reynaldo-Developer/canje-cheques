package com.canjecheques.canje_cheques.repository.impl;

import com.canjecheques.canje_cheques.entity.ClienteEntity;
import com.canjecheques.canje_cheques.repository.ClienteRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryClienteRepository implements ClienteRepository {

    private final List<ClienteEntity> data = new ArrayList<>();

    @Override
    public void saveAll(List<ClienteEntity> clientes) {
        data.addAll(clientes);
    }

    @Override
    public List<ClienteEntity> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Optional<ClienteEntity> findById(int clienteId) {
        return data.stream().filter(c -> c.getClienteId() == clienteId).findFirst();
    }

    @Override
    public void clear() {
        data.clear();
    }
}