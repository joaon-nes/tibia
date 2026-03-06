package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Topico;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    List<Topico> findAllByOrderByDataCriacaoDesc();

    @Query("SELECT t.autor.username, COUNT(t) FROM Topico t GROUP BY t.autor.username ORDER BY COUNT(t) DESC")
    List<Object[]> findTopAutores(Pageable pageable);
}