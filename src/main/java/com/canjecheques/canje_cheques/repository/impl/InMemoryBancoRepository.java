package com.canjecheques.canje_cheques.repository.impl;

import com.canjecheques.canje_cheques.entity.BancoEntity;
import com.canjecheques.canje_cheques.repository.BancoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryBancoRepository implements BancoRepository {

    private final List<BancoEntity> data = new ArrayList<>();

    @Override
    public void saveAll(List<BancoEntity> bancos) {
        data.addAll(bancos);
    }

    @Override
    public List<BancoEntity> findAll() {
        return new ArrayList<>(data);
    }

    @Override
    public Optional<BancoEntity> findById(int bancoId) {
        return data.stream().filter(b -> b.getBancoId() == bancoId).findFirst();
    }

    @Override
    public void clear() {
        data.clear();
    }
}