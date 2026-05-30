package com.m9.crm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MarcarPerdidoRequest(
        @NotBlank(message = "O motivo da perda é obrigatório")
        @Size(max = 1000, message = "Motivo deve ter no máximo 1000 caracteres")
        String motivo,

        @NotBlank(message = "O responsável é obrigatório")
        @Size(max = 100, message = "Responsável deve ter no máximo 100 caracteres")
        String responsavel
) {}
