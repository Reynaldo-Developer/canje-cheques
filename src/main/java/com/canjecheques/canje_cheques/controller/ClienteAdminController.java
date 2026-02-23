package com.canjecheques.canje_cheques.controller;

import com.canjecheques.canje_cheques.entity.ClienteEntity;
import com.canjecheques.canje_cheques.repository.ClienteJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/clientes")
public class ClienteAdminController {

    private final ClienteJpaRepository clienteRepo;

    public ClienteAdminController(ClienteJpaRepository clienteRepo) {
        this.clienteRepo = clienteRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clientes", clienteRepo.findAll());
        model.addAttribute("form", new ClienteEntity());
        return "admin/clientes";
    }

    @PostMapping
    public String create(@ModelAttribute("form") ClienteEntity form) {
        if (form.getClienteId() == null) return "redirect:/admin/clientes?error";
        if (form.getNombre() == null || form.getNombre().isBlank()) return "redirect:/admin/clientes?error";
        if (form.getApellido() == null || form.getApellido().isBlank()) return "redirect:/admin/clientes?error";
        if (form.getCorreo() == null || form.getCorreo().isBlank()) return "redirect:/admin/clientes?error";
        if (form.getEstado() == null) form.setEstado(1);

        clienteRepo.save(form);
        return "redirect:/admin/clientes?ok";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        var cli = clienteRepo.findById(id).orElseThrow();
        model.addAttribute("cliente", cli);
        return "admin/clientes_edit";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable Integer id, @ModelAttribute ClienteEntity form) {
        var cli = clienteRepo.findById(id).orElseThrow();
        cli.setNombre(form.getNombre());
        cli.setApellido(form.getApellido());
        cli.setCorreo(form.getCorreo());
        cli.setEstado(form.getEstado());
        clienteRepo.save(cli);
        return "redirect:/admin/clientes?updated";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {
        clienteRepo.deleteById(id);
        return "redirect:/admin/clientes?deleted";
    }
}