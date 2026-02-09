package com.canjecheques.canje_cheques.entity;

public class SucursalEntity {
    private int bancoId;
    private int sucursalId;
    private String nombre;

    public SucursalEntity() {}

    public SucursalEntity(int bancoId, int sucursalId, String nombre) {
        this.bancoId = bancoId;
        this.sucursalId = sucursalId;
        this.nombre = nombre;
    }

    public int getBancoId() { return bancoId; }
    public void setBancoId(int bancoId) { this.bancoId = bancoId; }

    public int getSucursalId() { return sucursalId; }
    public void setSucursalId(int sucursalId) { this.sucursalId = sucursalId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "Sucursal{" +
                "bancoId=" + bancoId +
                ", sucursalId=" + sucursalId +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}