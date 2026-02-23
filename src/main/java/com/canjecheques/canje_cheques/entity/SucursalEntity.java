package com.canjecheques.canje_cheques.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sucursal")
public class SucursalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "banco_id", nullable = false)
    private Integer bancoId;

    @Column(name = "sucursal_id", nullable = false)
    private Integer sucursalId;

    @Column(nullable = false, length = 140)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String ubigeo;

    @Column(nullable = false, length = 200)
    private String direccion;

    @Column(nullable = false)
    private Boolean exclusiva;

    public SucursalEntity() {}

    public Long getId() { return id; }

    public Integer getBancoId() { return bancoId; }
    public void setBancoId(Integer bancoId) { this.bancoId = bancoId; }

    public Integer getSucursalId() { return sucursalId; }
    public void setSucursalId(Integer sucursalId) { this.sucursalId = sucursalId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbigeo() { return ubigeo; }
    public void setUbigeo(String ubigeo) { this.ubigeo = ubigeo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public Boolean getExclusiva() { return exclusiva; }
    public void setExclusiva(Boolean exclusiva) { this.exclusiva = exclusiva; }

    @Override
    public String toString() {
        return "Sucursal{" +
                "id=" + id +
                ", bancoId=" + bancoId +
                ", sucursalId=" + sucursalId +
                ", nombre='" + nombre + '\'' +
                ", ubigeo='" + ubigeo + '\'' +
                ", direccion='" + direccion + '\'' +
                ", exclusiva=" + exclusiva +
                '}';
    }
}