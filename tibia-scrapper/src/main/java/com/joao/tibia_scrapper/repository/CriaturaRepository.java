package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Criatura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CriaturaRepository extends JpaRepository<Criatura, Long> {

    Optional<Criatura> findByNome(String nome);

    @Query("SELECT DISTINCT c.categoria FROM Criatura c WHERE c.categoria IS NOT NULL ORDER BY c.categoria")
    List<String> findDistinctCategorias();

    Page<Criatura> findByCategoriaOrderByNome(String categoria, Pageable pageable);
}
