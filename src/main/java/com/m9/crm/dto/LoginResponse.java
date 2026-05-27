package com.m9.crm.dto;

public record LoginResponse(
        Long id, String login, String nome, String cargo, String foto,
        String accessToken, String refreshToken, long expiresIn
) {}
