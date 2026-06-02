package com.m9.crm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClienteRequest(
        @Size(max = 50)  String origem,
        @Size(max = 50)  String contrato,
        @NotBlank(message = "Empresa é obrigatória") @Size(max = 150) String empresa,
        @Size(max = 18)  String cnpj,
        @Size(max = 14)  String cpf,
        @Size(max = 30)  String inscricaoEstadual,
        @Size(max = 100) String nomeContato,
        @Size(max = 80)  String cargo,
        @Size(max = 200) String endereco,
        @Size(max = 100) String bairro,
        @Size(max = 9)   String cep,
        @Size(max = 80)  String cidade,
        @Size(max = 2)   String estado,
        @Email(message = "E-mail inválido") @Size(max = 150) String email,
        @Size(max = 20)  String telefone,
        @Size(max = 20)  String telefone2,
        @Size(max = 100) String consultor,
        BigDecimal valor,
        @Pattern(
            regexp = "^(Novo|Contato|Proposta|Negociação|Fechado|Perdido)$",
            message = "Status inválido. Use: Novo, Contato, Proposta, Negociação, Fechado ou Perdido"
        )
        String status,
        LocalDate dataEntrada,
        LocalDate prorrogacao,
        @Size(max = 100) String responsavel,
        String observacoes
) {}
