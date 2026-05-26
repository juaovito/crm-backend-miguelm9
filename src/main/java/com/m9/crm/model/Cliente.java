package com.m9.crm.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origem;
    private String contrato;
    private String empresa;
    private String cnpj;
    private String cpf;
    private String nomeContato;
    private String cargo;
    private String endereco;
    private String bairro;
    private String cep;
    private String cidade;
    private String estado;
    private String email;
    private String telefone;
    private String telefone2;
    private String consultor;
    private String valor;
    private String status;
    private String dataEntrada;
    private String prorrogacao;
    private String responsavel;
    private Long criadoPor;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
