package com.canjecheques.canje_cheques.repository.impl;

import com.canjecheques.canje_cheques.entity.ChequeEntity;
import com.canjecheques.canje_cheques.repository.ChequeRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class InMemoryChequeRepository implements ChequeRepository {

    private final List<ChequeEntity> data = new ArrayList<>();

    @Override
    public List<ChequeEntity> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Optional<ChequeEntity> findById(int chequeId) {
        return data.stream().filter(c -> c.getChequeId() == chequeId).findFirst();
    }

    @Override
    public Optional<ChequeEntity> findByNumero(int bancoId, int sucursalId, String chequeNumero) {
        return data.stream()
                .filter(c -> c.getBancoId() == bancoId
                        && c.getSucursalId() == sucursalId
                        && c.getChequeNumero().equalsIgnoreCase(chequeNumero))
                .findFirst();
    }

    @Override
    public void save(ChequeEntity cheque) {
        data.add(cheque);
    }

    @Override
    public void update(ChequeEntity cheque) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getChequeId() == cheque.getChequeId()) {
                data.set(i, cheque);
                return;
            }
        }
        // si no existe, lo agrega (por seguridad)
        data.add(cheque);
    }

    @Override
    public void softDelete(int chequeId) {
        findById(chequeId).ifPresent(c -> c.setEstado(0));
    }

    @Override
    public int nextId() {
        return data.stream()
                .map(ChequeEntity::getChequeId)
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;
    }

    @Override
    public void saveAll(List<ChequeEntity> cheques) {
        data.addAll(cheques);
    }

    @Override
    public void clear() {
        data.clear();
    }
}