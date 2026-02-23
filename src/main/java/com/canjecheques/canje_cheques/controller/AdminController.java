package com.canjecheques.canje_cheques.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Panel con botones: Usuarios / Bancos / Clientes / Sucursales
    @GetMapping
    public String panel() {
        return "admin/panel";
    }
}