package com.bbva.clientmanager.controller;

import com.bbva.clientmanager.security.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * Controlador REST para la autenticación de usuarios.
 * Provee un endpoint para generar tokens JWT al loguearse.
 *
 * @author Veronica
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    /**
     * Endpoint para loguearse y obtener un token JWT.
     *
     * @param username el nombre de usuario para generar el token
     * @param password el password de usuario para generar el token
     * para el caso del challenge se hardcodea el usuario sa y pass sa
     * @return el token JWT generado
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        if ("sa".equals(username) && "sa".equals(password)) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}
