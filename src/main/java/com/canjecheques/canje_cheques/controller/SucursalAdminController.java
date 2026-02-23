package com.canjecheques.canje_cheques.controller;

import com.canjecheques.canje_cheques.entity.SucursalEntity;
import com.canjecheques.canje_cheques.repository.SucursalJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/sucursales")
public class SucursalAdminController {

    private final SucursalJpaRepository sucursalRepo;

    public SucursalAdminController(SucursalJpaRepository sucursalRepo) {
        this.sucursalRepo = sucursalRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("sucursales", sucursalRepo.findAll());
        model.addAttribute("form", new SucursalEntity());
        return "admin/sucursales";
    }

    @PostMapping
    public String create(@ModelAttribute("form") SucursalEntity form) {
        if (form.getBancoId() == null) return "redirect:/admin/sucursales?error";
        if (form.getSucursalId() == null) return "redirect:/admin/sucursales?error";
        if (form.getNombre() == null || form.getNombre().isBlank()) return "redirect:/admin/sucursales?error";
        if (form.getUbigeo() == null || form.getUbigeo().isBlank()) return "redirect:/admin/sucursales?error";
        if (form.getDireccion() == null || form.getDireccion().isBlank()) return "redirect:/admin/sucursales?error";
        if (form.getExclusiva() == null) form.setExclusiva(false);

        sucursalRepo.save(form);
        return "redirect:/admin/sucursales?ok";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        var s = sucursalRepo.findById(id).orElseThrow();
        model.addAttribute("sucursal", s);
        return "admin/sucursales_edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, @ModelAttribute SucursalEntity form) {
        var s = sucursalRepo.findById(id).orElseThrow();
        s.setBancoId(form.getBancoId());
        s.setSucursalId(form.getSucursalId());
        s.setNombre(form.getNombre());
        s.setUbigeo(form.getUbigeo());
        s.setDireccion(form.getDireccion());
        s.setExclusiva(form.getExclusiva());
        sucursalRepo.save(s);
        return "redirect:/admin/sucursales?updated";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        sucursalRepo.deleteById(id);
        return "redirect:/admin/sucursales?deleted";
    }
}