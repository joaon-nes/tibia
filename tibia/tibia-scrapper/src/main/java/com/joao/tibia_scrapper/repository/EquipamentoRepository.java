package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    List<Equipamento> findByVocacoesContainingAndLevelMinimoLessThanEqual(String vocacao, Integer level);
    Optional<Equipamento> findByNome(String nome);
    List<Equipamento> findByCategoria(String categoria);
    List<Equipamento> findByCategoriaOrderByNomeAsc(String categoria);
}