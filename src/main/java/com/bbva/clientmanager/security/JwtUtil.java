package com.bbva.clientmanager.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Clase utilitaria para la generación, extracción y validación de tokens JWT.
 * Esta clase provee métodos para crear tokens JWT para usuarios autenticados,
 * extraer el nombre de usuario de un token, y validar su integridad y expiración.
 *
 * @author Veronica
 */
@Component
public class JwtUtil {
    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     * Debe tener al menos 256 bits para el algoritmo HS256.
     */
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Genera un token JWT que contiene el nombre de usuario como sujeto,
     * con la fecha de emisión actual y expiración en 1 hora.
     *
     * @param username el nombre de usuario que se incluirá en el token
     * @return el token JWT generado como String
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) de un token JWT dado.
     *
     * @param token el token JWT
     * @return el nombre de usuario extraído del token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    /**
     * Valida un token JWT dado, verificando su firma y expiración.
     *
     * @param token el token JWT a validar
     * @return true si el token es válido; false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
