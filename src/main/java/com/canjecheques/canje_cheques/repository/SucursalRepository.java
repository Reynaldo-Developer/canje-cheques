package com.canjecheques.canje_cheques.repository;

import com.canjecheques.canje_cheques.entity.SucursalEntity;

import java.util.List;
import java.util.Optional;

public interface SucursalRepository {
    void saveAll(List<SucursalEntity> sucursales);
    List<SucursalEntity> findAll();
    Optional<SucursalEntity> findByBancoAndSucursal(int bancoId, int sucursalId);
    void clear();
}