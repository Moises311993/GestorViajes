package com.transportes.urgentes.gestion_viajes.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    private UserService usuarioService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
