package com.canjecheques.canje_cheques.repository;

import com.canjecheques.canje_cheques.entity.BancoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoJpaRepository extends JpaRepository<BancoEntity, Integer> {}