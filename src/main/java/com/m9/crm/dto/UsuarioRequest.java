package com.m9.crm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank(message = "Login é obrigatório") @Size(max = 100) String login,
        @NotBlank(message = "Nome é obrigatório")  @Size(max = 100) String nome,
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres") String senha,
        @Size(max = 50)  String cargo,
        @Size(max = 500) String foto
) {}
