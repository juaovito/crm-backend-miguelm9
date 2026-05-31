package com.m9.crm.controller;

import com.m9.crm.model.Usuario;
import com.m9.crm.repository.UsuarioRepository;
import com.m9.crm.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;
    private final UsuarioRepository usuarioRepository;

    public DashboardController(DashboardService dashboardService,
                               UsuarioRepository usuarioRepository) {
        this.dashboardService  = dashboardService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/resumo")
    public ResponseEntity<Map<String, Object>> resumo(Authentication auth) {
        Usuario usuario = resolverUsuario(auth);
        return ResponseEntity.ok(dashboardService.resumoGeral(usuario));
    }

    @GetMapping("/pipeline")
    public ResponseEntity<Map<String, Object>> pipeline(Authentication auth) {
        Usuario usuario = resolverUsuario(auth);
        return ResponseEntity.ok(dashboardService.pipeline(usuario));
    }

    // ── Resolve o Usuario a partir do Principal do JWT ───────────────────────
    private Usuario resolverUsuario(Authentication auth) {
        String login = auth.getName(); // getName() retorna o subject do JWT (login)
        return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalStateException("Usuário autenticado não encontrado: " + login));
    }
}
