package com.canjecheques.canje_cheques.repository;

import com.canjecheques.canje_cheques.entity.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SucursalJpaRepository extends JpaRepository<SucursalEntity, Long> {
    List<SucursalEntity> findByBancoId(Integer bancoId);
}