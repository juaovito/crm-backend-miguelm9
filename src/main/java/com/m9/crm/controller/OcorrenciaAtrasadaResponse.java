package com.m9.crm.controller;

import com.m9.crm.dto.OcorrenciaAtrasadaResponse;
import com.m9.crm.service.OcorrenciaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ocorrencias")
public class OcorrenciaGlobalController {

    private final OcorrenciaService ocorrenciaService;

    public OcorrenciaGlobalController(OcorrenciaService ocorrenciaService) {
        this.ocorrenciaService = ocorrenciaService;
    }

    /** GET /api/ocorrencias/atrasadas
     *  Retorna ocorrências com agendamento vencido e status "Em andamento".
     *  Consultor vê apenas seus clientes; Admin/Gerente veem todos.
     */
    @GetMapping("/atrasadas")
    public ResponseEntity<List<OcorrenciaAtrasadaResponse>> atrasadas(
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(ocorrenciaService.listarAtrasadas(principal));
    }
}
