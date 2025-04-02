package com.transportes.urgentes.gestion_viajes.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository usuarioRepository;

    public User guardarUsuario(User usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<User> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}
