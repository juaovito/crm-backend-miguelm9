package com.m9.crm.controller;

import com.m9.crm.dto.ClienteRequest;
import com.m9.crm.dto.MarcarPerdidoRequest;
import com.m9.crm.model.Cliente;
import com.m9.crm.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<Page<Cliente>> listar(
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String consultor,
            @PageableDefault(size = 20, sort = "empresa", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(clienteService.buscar(termo, status, consultor, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody ClienteRequest request) {
        Cliente salvo = clienteService.criar(request, null);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
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
}
