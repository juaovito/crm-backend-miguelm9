// src/main/java/com/m9/crm/repository/ClienteRepository.java
package com.m9.crm.repository;

import com.m9.crm.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findByConsultor(String consultor);
}
