package com.canjecheques.canje_cheques.entity;

public class BancoEntity {
    private int bancoId;
    private String nombre;

    public BancoEntity() {}

    public BancoEntity(int bancoId, String nombre) {
        this.bancoId = bancoId;
        this.nombre = nombre;
    }

    public int getBancoId() { return bancoId; }
    public void setBancoId(int bancoId) { this.bancoId = bancoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "Banco{" + "bancoId=" + bancoId + ", nombre='" + nombre + '\'' + '}';
    }
}