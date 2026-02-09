package com.canjecheques.canje_cheques.repository.impl;

import com.canjecheques.canje_cheques.entity.SucursalEntity;
import com.canjecheques.canje_cheques.repository.SucursalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemorySucursalRepository implements SucursalRepository {

    private final List<SucursalEntity> data = new ArrayList<>();

    @Override
    public void saveAll(List<SucursalEntity> sucursales) {
        data.addAll(sucursales);
    }

    @Override
    public List<SucursalEntity> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Optional<SucursalEntity> findByBancoAndSucursal(int bancoId, int sucursalId) {
        return data.stream()
                .filter(s -> s.getBancoId() == bancoId && s.getSucursalId() == sucursalId)
                .findFirst();
    }

    @Override
    public void clear() {
        data.clear();
    }
}