package com.m9.crm.controller;

import com.m9.crm.dto.OcorrenciaRequest;
import com.m9.crm.dto.OcorrenciaStatusRequest;
import com.m9.crm.model.Ocorrencia;
import com.m9.crm.service.OcorrenciaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clientes/{clienteId}/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaService ocorrenciaService;

    public OcorrenciaController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    /** GET /api/clientes/{clienteId}/ocorrencias */
    @GetMapping
    public ResponseEntity<List<Ocorrencia>> listar(@PathVariable Long clienteId) {
        return ResponseEntity.ok(ocorrenciaService.listarPorCliente(clienteId));
    }

    /** POST /api/clientes/{clienteId}/ocorrencias */
    @PostMapping
    public ResponseEntity<Ocorrencia> criar(@PathVariable Long clienteId,
                                             @Valid @RequestBody OcorrenciaRequest request) {
        Ocorrencia criada = ocorrenciaService.criar(clienteId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criada.getId()).toUri();
        return ResponseEntity.created(location).body(criada);
    }

    /** PATCH /api/clientes/{clienteId}/ocorrencias/{ocorrenciaId}/status */
    @PatchMapping("/{ocorrenciaId}/status")
    public ResponseEntity<Ocorrencia> atualizarStatus(@PathVariable Long clienteId,
                                                       @PathVariable Long ocorrenciaId,
                                                       @Valid @RequestBody OcorrenciaStatusRequest request) {
        return ResponseEntity.ok(ocorrenciaService.atualizarStatus(clienteId, ocorrenciaId, request));
    }
}
