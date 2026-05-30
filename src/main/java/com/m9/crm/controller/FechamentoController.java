package com.m9.crm.controller;

import com.m9.crm.dto.FechamentoRequest;
import com.m9.crm.model.Fechamento;
import com.m9.crm.service.FechamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/fechamentos")
public class FechamentoController {

    private final FechamentoService fechamentoService;

    public FechamentoController(FechamentoService fechamentoService) {
        this.fechamentoService = fechamentoService;
    }

    // POST /api/fechamentos
    @PostMapping
    public ResponseEntity<Fechamento> criar(@Valid @RequestBody FechamentoRequest request) {
        Fechamento salvo = fechamentoService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(salvo);
    }

    // GET /api/fechamentos?clienteId=123
    @GetMapping
    public ResponseEntity<List<Fechamento>> listar(@RequestParam Long clienteId) {
        return ResponseEntity.ok(fechamentoService.listarPorCliente(clienteId));
    }

    // GET /api/fechamentos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Fechamento> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(fechamentoService.buscarPorId(id));
    }

    // DELETE /api/fechamentos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fechamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
