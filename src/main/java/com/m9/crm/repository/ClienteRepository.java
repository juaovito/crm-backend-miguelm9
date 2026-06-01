package com.m9.crm.repository;

import com.m9.crm.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findByConsultor(String consultor, Pageable pageable);
    Page<Cliente> findByCriadoPor(Long criadoPor, Pageable pageable);
    long countByCriadoPor(Long criadoPor);
    Page<Cliente> findByStatus(String status, Pageable pageable);

    // ── Verificações de duplicidade ──────────────────────────────────────────

    boolean existsByCnpjAndIdNot(String cnpj, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);

    // Versão para criação (sem id para excluir — passa 0L que nunca existe)
    default boolean existsByCnpj(String cnpj) {
        return existsByCnpjAndIdNot(cnpj, 0L);
    }

    default boolean existsByEmail(String email) {
        return existsByEmailAndIdNot(email, 0L);
    }

    @Query("""
            SELECT c FROM Cliente c
            WHERE (:termo IS NULL OR :termo = ''
                   OR LOWER(c.empresa)     LIKE LOWER(CONCAT('%', :termo, '%'))
                   OR LOWER(c.nomeContato) LIKE LOWER(CONCAT('%', :termo, '%'))
                   OR LOWER(c.email)       LIKE LOWER(CONCAT('%', :termo, '%'))
                   OR c.cnpj              LIKE CONCAT('%', :termo, '%')
                   OR c.cpf               LIKE CONCAT('%', :termo, '%'))
              AND (:status IS NULL OR :status = '' OR c.status = :status)
              AND (:consultor IS NULL OR :consultor = '' OR c.consultor = :consultor)
            """)
    Page<Cliente> buscar(@Param("termo") String termo,
                         @Param("status") String status,
                         @Param("consultor") String consultor,
                         Pageable pageable);

    // ── Contagens globais (admin/gerente) ────────────────────────────────────

    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM Cliente c WHERE c.status = :status")
    BigDecimal somarValorPorStatus(@Param("status") String status);

    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM Cliente c")
    BigDecimal somarValorTotal();

    @Query("SELECT c.status, COUNT(c), COALESCE(SUM(c.valor), 0) FROM Cliente c GROUP BY c.status")
    List<Object[]> resumoPorStatus();

    @Query("""
            SELECT c.consultor, COUNT(c), COALESCE(SUM(c.valor), 0)
            FROM Cliente c GROUP BY c.consultor ORDER BY COUNT(c) DESC
            """)
    List<Object[]> resumoPorConsultor();

    // ── Contagens filtradas por dono (consultor) ─────────────────────────────

    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.status = :status AND c.criadoPor = :criadoPor")
    long countByStatusAndCriadoPor(@Param("status") String status,
                                   @Param("criadoPor") Long criadoPor);

    @Query("SELECT COALESCE(SUM(c.valor), 0) FROM Cliente c WHERE c.criadoPor = :criadoPor")
    BigDecimal somarValorTotalPorDono(@Param("criadoPor") Long criadoPor);

    @Query("""
            SELECT c.status, COUNT(c), COALESCE(SUM(c.valor), 0)
            FROM Cliente c
            WHERE c.criadoPor = :criadoPor
            GROUP BY c.status
            """)
    List<Object[]> resumoPorStatusEDono(@Param("criadoPor") Long criadoPor);

    // ── Pipeline global: últimos N leads por status ──────────────────────────

    @Query("""
            SELECT c FROM Cliente c
            WHERE c.status = :status
            ORDER BY c.criadoEm DESC
            """)
    List<Cliente> findTopByStatusOrderByCriadoEmDesc(@Param("status") String status,
                                                      Pageable pageable);

    // ── Pipeline filtrado: últimos N leads por status e dono ─────────────────

    @Query("""
            SELECT c FROM Cliente c
            WHERE c.status = :status
              AND c.criadoPor = :criadoPor
            ORDER BY c.criadoEm DESC
            """)
    List<Cliente> findTopByStatusAndCriadoPorOrderByCriadoEmDesc(@Param("status") String status,
                                                                   @Param("criadoPor") Long criadoPor,
                                                                   Pageable pageable);

    // ── Retornos hoje ────────────────────────────────────────────────────────

    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.prorrogacao = :hoje")
    long countRetornosHoje(@Param("hoje") LocalDate hoje);

    @Query("SELECT COUNT(c) FROM Cliente c WHERE c.prorrogacao = :hoje AND c.criadoPor = :criadoPor")
    long countRetornosHojeEDono(@Param("hoje") LocalDate hoje,
                                @Param("criadoPor") Long criadoPor);
}
