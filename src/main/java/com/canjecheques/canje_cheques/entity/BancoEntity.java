package com.canjecheques.canje_cheques.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "banco")
public class BancoEntity {

    @Id
    @Column(name = "banco_id")
    private Integer bancoId;

    @Column(nullable = false, length = 120)
    private String nombre;

    public BancoEntity() {}

    public BancoEntity(Integer bancoId, String nombre) {
        this.bancoId = bancoId;
        this.nombre = nombre;
    }

    public Integer getBancoId() { return bancoId; }
    public void setBancoId(Integer bancoId) { this.bancoId = bancoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "Banco{" +
                "bancoId=" + bancoId +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}