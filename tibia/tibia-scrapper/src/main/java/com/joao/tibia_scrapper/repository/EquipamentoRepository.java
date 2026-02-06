package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    Optional<Equipamento> findByNome(String nome);

    @Query("SELECT e FROM Equipamento e WHERE (e.categoria = :categoria) AND " +
        "(:level IS NULL OR e.levelMinimo >= :level) AND " + 
        "(:vocacao IS NULL OR e.vocacoes LIKE %:vocacao%) AND " +
        "(:protecao IS NULL OR (e.protecao LIKE %:protecao% OR e.atributos LIKE %:protecao%)) AND " +
        "(:elemento IS NULL OR (e.danoElemental LIKE %:elemento% OR e.elementalBond LIKE %:elemento% OR e.protecao LIKE %:elemento% OR e.atributos LIKE %:elemento%)) AND " +
        "(:bonus IS NULL OR (e.bonusSkill LIKE %:bonus% OR e.atributos LIKE %:bonus%)) AND " +
        "(:cargas IS NULL OR e.cargas LIKE %:cargas%) AND " +
        "(:duracao IS NULL OR e.duracao LIKE %:duracao%) AND " +
        "(:range IS NULL OR e.alcance = :range) AND " +
        "(:slots IS NULL OR e.slots = :slots) AND " +
        "(:tier IS NULL OR e.tier = :tier) " +
        "ORDER BY e.nome ASC")
    List<Equipamento> findComFiltrosAvancados(
        @Param("categoria") String categoria, @Param("level") Integer level,
        @Param("vocacao") String vocacao, @Param("protecao") String protecao,
        @Param("elemento") String elemento, @Param("bonus") String bonus,
        @Param("atributos") String atributos, @Param("cargas") String cargas,
        @Param("duracao") String duracao, @Param("range") Integer range,
        @Param("slots") Integer slots, @Param("tier") Integer tier
    );

    List<Equipamento> findByCategoriaOrderByNomeAsc(String categoria);
}