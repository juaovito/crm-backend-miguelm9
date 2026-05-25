// src/main/java/com/m9/crm/model/Cliente.java
package com.m9.crm.model;

import jakarta.persistence.*;

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
    private String contato;
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
    private String data;
    private String prorrogacao;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

    private String responsavel;
    private Long criadoPor;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
    public String getContrato() { return contrato; }
    public void setContrato(String contrato) { this.contrato = contrato; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getContato() { return contato; }
    public void setContato(String contato) { this.contato = contato; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getTelefone2() { return telefone2; }
    public void setTelefone2(String telefone2) { this.telefone2 = telefone2; }
    public String getConsultor() { return consultor; }
    public void setConsultor(String consultor) { this.consultor = consultor; }
    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getProrrogacao() { return prorrogacao; }
    public void setProrrogacao(String prorrogacao) { this.prorrogacao = prorrogacao; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getResponsavel() { return responsavel; }
    public void setResponsavel(String responsavel) { this.responsavel = responsavel; }
    public Long getCriadoPor() { return criadoPor; }
    public void setCriadoPor(Long criadoPor) { this.criadoPor = criadoPor; }
}
