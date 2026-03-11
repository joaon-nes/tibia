package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Runa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RunaRepository extends JpaRepository<Runa, Long> {
    Optional<Runa> findByNome(String nome);
}