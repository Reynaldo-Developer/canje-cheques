package com.canjecheques.canje_cheques.repository;

import com.canjecheques.canje_cheques.entity.BancoEntity;

import java.util.List;
import java.util.Optional;

public interface BancoRepository {
    void saveAll(List<BancoEntity> bancos);
    List<BancoEntity> findAll();
    Optional<BancoEntity> findById(int bancoId);
    void clear();
}