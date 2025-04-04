package com.transportes.urgentes.gestion_viajes.auth;

import com.transportes.urgentes.gestion_viajes.drivers.Conductor;
import com.transportes.urgentes.gestion_viajes.drivers.ConductorService;
import com.transportes.urgentes.gestion_viajes.users.User;
import com.transportes.urgentes.gestion_viajes.users.UserRole;
import com.transportes.urgentes.gestion_viajes.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ConductorService conductorService;

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Obtener el usuario autenticado
            User user = (User) authentication.getPrincipal();
            
            // Redirigir según el rol
            return switch (user.getRole()) {
                case ADMIN -> "redirect:/admin/dashboard";
                case DRIVER -> "redirect:/driver/dashboard";
                case CLIENT -> "redirect:/client/dashboard";
            };
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Credenciales inválidas");
            return "redirect:/login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", UserRole.values());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String email,
                         @RequestParam String nombre,
                         @RequestParam String apellido,
                         @RequestParam UserRole role,
                         @RequestParam(required = false) String licenciaConduccion,
                         @RequestParam(required = false) String tipoLicencia,
                         RedirectAttributes redirectAttributes) {
        try {
            User user;
            
            if (role == UserRole.DRIVER) {
                // Crear un conductor
                Conductor conductor = new Conductor();
                conductor.setUsername(username);
                conductor.setPassword(password);
                conductor.setEmail(email);
                conductor.setNombre(nombre);
                conductor.setApellido(apellido);
                conductor.setLicenciaConduccion(licenciaConduccion);
                conductor.setTipoLicencia(tipoLicencia);
                user = conductor;
            } else {
                // Crear un usuario normal
                user = new User();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                user.setNombre(nombre);
                user.setApellido(apellido);
                user.setRole(role);
            }
            
            // Guardar el usuario usando el UserService para que la contraseña se cifre correctamente
            userService.createUser(user);
            
            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        SecurityContextHolder.clearContext();
        return "redirect:/login?logout=true";
    }
} 