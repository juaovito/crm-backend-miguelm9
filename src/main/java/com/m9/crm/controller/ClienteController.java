// src/main/java/com/m9/crm/controller/ClienteController.java
package com.m9.crm.controller;

import com.m9.crm.model.Cliente;
import com.m9.crm.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    // LISTAR TODOS
    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CADASTRAR
    @PostMapping
    public Cliente cadastrar(@RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // ATUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setEmpresa(dados.getEmpresa());
            cliente.setContato(dados.getContato());
            cliente.setStatus(dados.getStatus());
            cliente.setTelefone(dados.getTelefone());
            cliente.setEmail(dados.getEmail());
            cliente.setConsultor(dados.getConsultor());
            cliente.setValor(dados.getValor());
            cliente.setProrrogacao(dados.getProrrogacao());
            cliente.setObservacoes(dados.getObservacoes());
            return ResponseEntity.ok(clienteRepository.save(cliente));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
