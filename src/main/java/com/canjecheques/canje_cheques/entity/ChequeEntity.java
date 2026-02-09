package com.canjecheques.canje_cheques.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ChequeEntity {
    private int chequeId;
    private int bancoId;
    private int sucursalId;
    private String chequeNumero;
    private LocalDate fecha;
    private LocalTime hora;
    private int moneda; // 1=SOLES, 2=DOLARES (según tu txt)
    private BigDecimal monto;
    private int estado; // 1=ACTIVO, 0=ELIMINADO/ANULADO (soft delete)
    private int clienteId;

    public ChequeEntity() {}

    public ChequeEntity(int chequeId, int bancoId, int sucursalId, String chequeNumero,
                        LocalDate fecha, LocalTime hora, int moneda,
                        BigDecimal monto, int estado, int clienteId) {
        this.chequeId = chequeId;
        this.bancoId = bancoId;
        this.sucursalId = sucursalId;
        this.chequeNumero = chequeNumero;
        this.fecha = fecha;
        this.hora = hora;
        this.moneda = moneda;
        this.monto = monto;
        this.estado = estado;
        this.clienteId = clienteId;
    }

    public int getChequeId() { return chequeId; }
    public void setChequeId(int chequeId) { this.chequeId = chequeId; }

    public int getBancoId() { return bancoId; }
    public void setBancoId(int bancoId) { this.bancoId = bancoId; }

    public int getSucursalId() { return sucursalId; }
    public void setSucursalId(int sucursalId) { this.sucursalId = sucursalId; }

    public String getChequeNumero() { return chequeNumero; }
    public void setChequeNumero(String chequeNumero) { this.chequeNumero = chequeNumero; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public int getMoneda() { return moneda; }
    public void setMoneda(int moneda) { this.moneda = moneda; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    @Override
    public String toString() {
        return "Cheque{" +
                "chequeId=" + chequeId +
                ", bancoId=" + bancoId +
                ", sucursalId=" + sucursalId +
                ", chequeNumero='" + chequeNumero + '\'' +
                ", fecha=" + fecha +
                ", hora=" + hora +
                ", moneda=" + moneda +
                ", monto=" + monto +
                ", estado=" + estado +
                ", clienteId=" + clienteId +
                '}';
    }
}