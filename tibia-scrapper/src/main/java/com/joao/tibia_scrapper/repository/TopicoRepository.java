package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    List<Topico> findAllByOrderByDataCriacaoDesc();
}