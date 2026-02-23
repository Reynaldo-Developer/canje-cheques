package com.canjecheques.canje_cheques.controller;

import com.canjecheques.canje_cheques.repository.jpa.ChequeJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChequeController {

    private final ChequeJpaRepository repo;

    public ChequeController(ChequeJpaRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/cheques")
    public String listar(Model model) {
        model.addAttribute("cheques", repo.findByEstado(1));
        return "cheques";
    }
}