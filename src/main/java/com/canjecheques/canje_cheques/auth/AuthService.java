package com.canjecheques.canje_cheques.auth;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    // Usuarios demo (para entrega parcial)
    private final List<Usuario> usuarios = List.of(
            new Usuario("admin", "admin", "Administrador"),
            new Usuario("alfonso", "1234", "Alfonso Yzaguirre")
    );

    public Optional<Usuario> login(String user, String pass) {
        return usuarios.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(user))
                .filter(u -> u.getPassword().equals(pass))
                .findFirst();
    }
}