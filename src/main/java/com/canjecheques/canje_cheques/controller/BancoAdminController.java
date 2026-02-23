package com.canjecheques.canje_cheques.controller;

import com.canjecheques.canje_cheques.entity.BancoEntity;
import com.canjecheques.canje_cheques.repository.BancoJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/bancos")
public class BancoAdminController {

    private final BancoJpaRepository bancoRepo;

    public BancoAdminController(BancoJpaRepository bancoRepo) {
        this.bancoRepo = bancoRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("bancos", bancoRepo.findAll());
        model.addAttribute("form", new BancoEntity());
        return "admin/bancos";
    }

    @PostMapping
    public String create(@ModelAttribute("form") BancoEntity form) {
        if (form.getBancoId() == null || form.getNombre() == null || form.getNombre().isBlank()) {
            return "redirect:/admin/bancos?error";
        }
        bancoRepo.save(form);
        return "redirect:/admin/bancos?ok";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        var banco = bancoRepo.findById(id).orElseThrow();
        model.addAttribute("banco", banco);
        return "admin/bancos_edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id, @ModelAttribute BancoEntity form) {
        var banco = bancoRepo.findById(id).orElseThrow();
        banco.setNombre(form.getNombre());
        bancoRepo.save(banco);
        return "redirect:/admin/bancos?updated";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        bancoRepo.deleteById(id);
        return "redirect:/admin/bancos?deleted";
    }
}