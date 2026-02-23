package com.canjecheques.canje_cheques.admin.model;

import java.util.ArrayList;
import java.util.List;

public class AdminUser {
    private Integer id;
    private String username;
    private String fullName;
    private Boolean enabled = true;

    // para crear/editar
    private String password; // en editar: si viene vacío, NO se cambia
    private List<Integer> roleIds = new ArrayList<>();

    // para mostrar
    private List<String> roleNames = new ArrayList<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<Integer> getRoleIds() { return roleIds; }
    public void setRoleIds(List<Integer> roleIds) { this.roleIds = roleIds; }

    public List<String> getRoleNames() { return roleNames; }
    public void setRoleNames(List<String> roleNames) { this.roleNames = roleNames; }
}