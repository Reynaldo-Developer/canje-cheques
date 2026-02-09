package com.canjecheques.canje_cheques.repository;

import com.canjecheques.canje_cheques.entity.ChequeEntity;

import java.util.List;
import java.util.Optional;

public interface ChequeRepository {
    List<ChequeEntity> findAll();
    Optional<ChequeEntity> findById(int chequeId);
    Optional<ChequeEntity> findByNumero(int bancoId, int sucursalId, String chequeNumero);

    void save(ChequeEntity cheque);       // registrar
    void update(ChequeEntity cheque);     // actualizar
    void softDelete(int chequeId);        // eliminar lógico
    int nextId();
    void saveAll(List<ChequeEntity> cheques);
    void clear();
}