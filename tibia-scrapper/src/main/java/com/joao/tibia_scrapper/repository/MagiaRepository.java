package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Magia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MagiaRepository extends JpaRepository<Magia, Long> {
    Optional<Magia> findByNome(String nome);
    boolean existsByNome(String nome);
}