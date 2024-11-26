package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(sa.id, sa.date, sa.amount, s.name) " +
            "FROM Sale sa " +
            "JOIN sa.seller s " +
            "WHERE sa.date >= :minDate " +
            "AND sa.date <= :maxDate " +
            "AND UPPER(s.name) LIKE CONCAT('%', UPPER(:name), '%')")
    Page<SaleReportDTO> getSalesReport(
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate,
            @Param("name") String name,
            Pageable pageable
    );

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(s.name, SUM(sa.amount)) " +
            "FROM Sale sa " +
            "JOIN sa.seller s " +
            "WHERE (sa.date >= :minDate) " +
            "AND (sa.date <= :maxDate) " +
            "GROUP BY s.name")
    Page<SaleSummaryDTO> getSalesSummary(
            @Param("minDate") LocalDate minDate,
            @Param("maxDate") LocalDate maxDate,
            Pageable pageable
    );
}
