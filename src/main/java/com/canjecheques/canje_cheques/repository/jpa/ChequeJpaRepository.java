package com.canjecheques.canje_cheques.repository.jpa;

import com.canjecheques.canje_cheques.entity.ChequeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChequeJpaRepository extends JpaRepository<ChequeEntity, Integer> {
    List<ChequeEntity> findByEstado(Integer estado);
    Optional<ChequeEntity> findByBancoIdAndSucursalIdAndChequeNumero(Integer bancoId, Integer sucursalId, String chequeNumero);
}