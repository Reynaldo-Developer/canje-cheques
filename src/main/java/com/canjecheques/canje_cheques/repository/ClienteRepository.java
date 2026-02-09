package com.canjecheques.canje_cheques.repository;

import com.canjecheques.canje_cheques.entity.ClienteEntity;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    void saveAll(List<ClienteEntity> clientes);
    List<ClienteEntity> findAll();
    Optional<ClienteEntity> findById(int clienteId);
    void clear();
}