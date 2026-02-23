package com.canjecheques.canje_cheques.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "cheque")
public class ChequeEntity {

    @Id
    @Column(name = "cheque_id")
    private Integer chequeId;

    private Integer bancoId;
    private Integer sucursalId;

    @Column(length = 40, nullable = false)
    private String chequeNumero;

    private LocalDate fecha;
    private LocalTime hora;

    private Integer moneda;

    @Column(precision = 18, scale = 2)
    private BigDecimal monto;

    private Integer estado;
    private Integer clienteId;

    public Integer getChequeId() { return chequeId; }
    public void setChequeId(Integer chequeId) { this.chequeId = chequeId; }

    public Integer getBancoId() { return bancoId; }
    public void setBancoId(Integer bancoId) { this.bancoId = bancoId; }

    public Integer getSucursalId() { return sucursalId; }
    public void setSucursalId(Integer sucursalId) { this.sucursalId = sucursalId; }

    public String getChequeNumero() { return chequeNumero; }
    public void setChequeNumero(String chequeNumero) { this.chequeNumero = chequeNumero; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }

    public Integer getMoneda() { return moneda; }
    public void setMoneda(Integer moneda) { this.moneda = moneda; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

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