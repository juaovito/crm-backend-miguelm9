package com.m9.crm.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String login;

    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * ATENÇÃO: este campo deve armazenar um hash BCrypt, nunca senha em texto puro.
     * Ao criar/atualizar um usuário, aplique:
     *   new BCryptPasswordEncoder().encode(senhaDigitada)
     * A validação no login usa:
     *   passwordEncoder.matches(senhaDigitada, usuario.getSenha())
     *
     * A integração completa com Spring Security + JWT será feita em sessão separada.
     */
    @Column(nullable = false)
    private String senha;

    @Column(length = 50)
    private String cargo;

    private String foto;
}
