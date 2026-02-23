package com.canjecheques.canje_cheques.admin.model;

public class AdminRole {
    private int id;
    private String name;

    public AdminRole() {}

    public AdminRole(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}