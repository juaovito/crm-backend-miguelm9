package com.m9.crm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record OcorrenciaRequest(

        @NotBlank(message = "Informações são obrigatórias")
        @Size(max = 5000, message = "Informações devem ter no máximo 5000 caracteres")
        String informacoes,

        @Size(max = 50, message = "Estágio deve ter no máximo 50 caracteres")
        String estagio,

        LocalDateTime agendamento,

        @Size(max = 100, message = "Usuário deve ter no máximo 100 caracteres")
        String usuario
) {}
