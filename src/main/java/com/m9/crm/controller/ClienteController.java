package com.m9.crm.controller;

import com.m9.crm.model.Cliente;
import com.m9.crm.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscar(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente) {
        Cliente salvo = clienteRepository.save(cliente);
        return ResponseEntity.status(201).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente dados) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setOrigem(dados.getOrigem());
            cliente.setContrato(dados.getContrato());
            cliente.setEmpresa(dados.getEmpresa());
            cliente.setCnpj(dados.getCnpj());
            cliente.setCpf(dados.getCpf());
            cliente.setNomeContato(dados.getNomeContato());
            cliente.setCargo(dados.getCargo());
            cliente.setEndereco(dados.getEndereco());
            cliente.setBairro(dados.getBairro());
            cliente.setCep(dados.getCep());
            cliente.setCidade(dados.getCidade());
            cliente.setEstado(dados.getEstado());
            cliente.setEmail(dados.getEmail());
            cliente.setTelefone(dados.getTelefone());
            cliente.setTelefone2(dados.getTelefone2());
            cliente.setConsultor(dados.getConsultor());
            cliente.setValor(dados.getValor());
            cliente.setStatus(dados.getStatus());
            cliente.setDataEntrada(dados.getDataEntrada());
            cliente.setProrrogacao(dados.getProrrogacao());
            cliente.setObservacoes(dados.getObservacoes());
            // responsavel e criadoPor são preservados do registro original — não sobrescrever
            return ResponseEntity.ok(clienteRepository.save(cliente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!clienteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
