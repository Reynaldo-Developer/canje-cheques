package com.canjecheques.canje_cheques.repository;

import com.canjecheques.canje_cheques.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Integer> {}