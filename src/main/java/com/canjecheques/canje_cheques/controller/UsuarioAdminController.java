package com.canjecheques.canje_cheques.controller;

import com.canjecheques.canje_cheques.admin.model.AdminUser;
import com.canjecheques.canje_cheques.admin.service.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
public class UsuarioAdminController {

    private final AdminUserService service;

    public UsuarioAdminController(AdminUserService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model, @RequestParam(value = "msg", required = false) String msg) {
        model.addAttribute("usuarios", service.listUsers());
        if (msg != null) model.addAttribute("msg", msg);
        return "admin/usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new AdminUser());
        model.addAttribute("roles", service.listRoles());
        model.addAttribute("mode", "create");
        return "admin/usuario-form";
    }

    @PostMapping("/nuevo")
    public String crear(@ModelAttribute("form") AdminUser form, RedirectAttributes ra, Model model) {
        try {
            service.createUser(form);
            ra.addAttribute("msg", "Usuario creado");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("roles", service.listRoles());
            model.addAttribute("mode", "create");
            model.addAttribute("error", e.getMessage());
            return "admin/usuario-form";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {
        model.addAttribute("form", service.getUser(id));
        model.addAttribute("roles", service.listRoles());
        model.addAttribute("mode", "edit");
        return "admin/usuario-form";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable int id, @ModelAttribute("form") AdminUser form,
                             RedirectAttributes ra, Model model) {
        try {
            service.updateUser(id, form);
            ra.addAttribute("msg", "Usuario actualizado");
            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            model.addAttribute("roles", service.listRoles());
            model.addAttribute("mode", "edit");
            model.addAttribute("error", e.getMessage());
            return "admin/usuario-form";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id, RedirectAttributes ra) {
        service.deleteUser(id);
        ra.addAttribute("msg", "Usuario eliminado");
        return "redirect:/admin/usuarios";
    }
}