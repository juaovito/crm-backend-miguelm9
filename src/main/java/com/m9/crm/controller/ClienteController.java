package com.m9.crm.controller;

import com.m9.crm.dto.ClienteRequest;
import com.m9.crm.dto.MarcarPerdidoRequest;
import com.m9.crm.model.Cliente;
import com.m9.crm.model.Usuario;
import com.m9.crm.repository.UsuarioRepository;
import com.m9.crm.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioRepository usuarioRepository;

    public ClienteController(ClienteService clienteService, UsuarioRepository usuarioRepository) {
        this.clienteService    = clienteService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Cliente>> listar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String consultor,
            @AuthenticationPrincipal UserDetails principal,
            @PageableDefault(size = 20, sort = "empresa", direction = Sort.Direction.ASC) Pageable pageable) {

        String consultorFiltro = resolverFiltroConsultor(consultor, principal);
        return ResponseEntity.ok(clienteService.buscar(termo, status, consultorFiltro, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(
            @Valid @RequestBody ClienteRequest request,
            @AuthenticationPrincipal UserDetails principal) {

        Long usuarioId = resolverUsuarioId(principal);
        Cliente salvo  = clienteService.criar(request, usuarioId);
        URI location   = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id,
                                              @Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.ok(clienteService.atualizar(id, request));
    }

    @PatchMapping("/{id}/perdido")
    public ResponseEntity<Cliente> marcarPerdido(@PathVariable Long id,
                                                  @Valid @RequestBody MarcarPerdidoRequest request) {
        return ResponseEntity.ok(clienteService.marcarPerdido(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Busca o id real do usuário logado no banco via login extraído do JWT.
     * Retorna null apenas se o principal for nulo (não deveria ocorrer em endpoints autenticados).
     */
    private Long resolverUsuarioId(UserDetails principal) {
        if (principal == null) return null;
        return usuarioRepository.findByLogin(principal.getUsername())
                .map(Usuario::getId)
                .orElse(null);
    }

    /**
     * Regra de isolamento por cargo:
     * - CONSULTOR  → ignora o param e força o próprio nome (campo consultor no Cliente).
     * - ADMIN / GERENTE → respeita o param (null = sem filtro, vê todos).
     */
    private String resolverFiltroConsultor(String consultorParam, UserDetails principal) {
        if (principal == null) return consultorParam;

        boolean isConsultor = principal.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN")
                             || a.getAuthority().equals("ROLE_GERENTE"));

        if (isConsultor) {
            return usuarioRepository.findByLogin(principal.getUsername())
                    .map(Usuario::getNome)
                    .orElse(principal.getUsername());
        }

        return consultorParam;
    }
}
