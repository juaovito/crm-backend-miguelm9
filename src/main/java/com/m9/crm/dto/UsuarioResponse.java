package com.m9.crm.dto;

import com.m9.crm.model.Usuario;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id, String login, String nome, String cargo,
        String foto, boolean ativo, LocalDateTime criadoEm, LocalDateTime atualizadoEm
) {
    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(u.getId(), u.getLogin(), u.getNome(), u.getCargo(),
                u.getFoto(), u.isAtivo(), u.getCriadoEm(), u.getAtualizadoEm());
    }
}
