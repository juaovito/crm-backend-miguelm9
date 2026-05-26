package com.m9.crm.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String origem;

    @Column(length = 50)
    private String contrato;

    @Column(length = 150)
    private String empresa;

    @Column(length = 18)
    private String cnpj;

    @Column(length = 14)
    private String cpf;

    @Column(length = 100)
    private String nomeContato;

    @Column(length = 80)
    private String cargo;

    @Column(length = 200)
    private String endereco;

    @Column(length = 100)
    private String bairro;

    @Column(length = 9)
    private String cep;

    @Column(length = 80)
    private String cidade;

    @Column(length = 2)
    private String estado;

    @Column(length = 150)
    private String email;

    @Column(length = 20)
    private String telefone;

    @Column(length = 20)
    private String telefone2;

    @Column(length = 100)
    private String consultor;

    /**
     * Valor do contrato como BigDecimal para permitir cálculos e ordenação correta.
     * No banco será armazenado como DECIMAL(15,2).
     */
    @Column(precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(length = 50)
    private String status;

    @Column(length = 20)
    private String dataEntrada;

    @Column(length = 20)
    private String prorrogacao;

    @Column(length = 100)
    private String responsavel;

    private Long criadoPor;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
