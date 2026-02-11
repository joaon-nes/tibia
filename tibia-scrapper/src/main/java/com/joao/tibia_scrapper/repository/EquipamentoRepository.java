package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

        Optional<Equipamento> findByNome(String nome);

        Optional<Equipamento> findByNomeAndCategoria(String nome, String categoria);

        List<Equipamento> findByCategoriaIgnoreCase(String categoria);

        @Query("SELECT e FROM Equipamento e WHERE " +
                        "(:categorias IS NULL OR e.categoria IN :categorias) AND " +
                        "(:level IS NULL OR e.levelMinimo <= :level) AND " +
                        "(:vocacao IS NULL OR e.vocacoes LIKE %:vocacao% OR e.vocacoes IS NULL OR e.vocacoes = '' OR e.vocacoes = 'todas') AND "
                        +
                        "(:protecao IS NULL OR e.protecao LIKE %:protecao%) AND " +
                        "(:elemento IS NULL OR e.danoElemental LIKE %:elemento%) AND " +
                        "(:bonus IS NULL OR e.bonusSkill LIKE %:bonus%) AND " +
                        "(:atributos IS NULL OR e.atributos LIKE %:atributos%) AND " +
                        "(:range IS NULL OR e.alcance = :range) AND " +
                        "(:slots IS NULL OR e.slots = :slots) AND " +
                        "(:tier IS NULL OR e.tier = :tier)")
        List<Equipamento> findComFiltrosAvancados(
                        @Param("categorias") List<String> categorias,
                        @Param("level") Integer level,
                        @Param("vocacao") String vocacao,
                        @Param("protecao") String protecao,
                        @Param("elemento") String elemento,
                        @Param("bonus") String bonus,
                        @Param("atributos") String atributos,
                        @Param("range") Integer range,
                        @Param("slots") Integer slots,
                        @Param("tier") Integer tier);
}