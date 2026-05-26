package com.m9.crm.controller;

import com.m9.crm.model.Usuario;
import com.m9.crm.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String login = body.get("login");
        String senha = body.get("senha");

        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("mensagem", "Login e senha são obrigatórios"));
        }

        Optional<Usuario> usuarioOpt = usuarioRepository.findByLogin(login);

        /**
         * TODO: quando Spring Security + BCrypt for integrado, substituir a comparação abaixo por:
         *   passwordEncoder.matches(senha, usuario.getSenha())
         *
         * A comparação direta é insegura e deve ser substituída antes de ir para produção
         * com usuários reais.
         */
        if (usuarioOpt.isEmpty() || !usuarioOpt.get().getSenha().equals(senha)) {
            return ResponseEntity.status(401)
                    .body(Map.of("mensagem", "Usuário ou senha incorretos"));
        }

        Usuario usuario = usuarioOpt.get();

        return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "login", usuario.getLogin(),
                "nome", usuario.getNome() != null ? usuario.getNome() : usuario.getLogin(),
                "cargo", usuario.getCargo() != null ? usuario.getCargo() : "consultor",
                "foto", usuario.getFoto() != null ? usuario.getFoto() : ""
        ));
    }
}
