package com.canjecheques.canje_cheques.entity;

public class ClienteEntity {
    private int clienteId;
    private String nombre;

    public ClienteEntity() {}

    public ClienteEntity(int clienteId, String nombre) {
        this.clienteId = clienteId;
        this.nombre = nombre;
    }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return "Cliente{" + "clienteId=" + clienteId + ", nombre='" + nombre + '\'' + '}';
    }
}