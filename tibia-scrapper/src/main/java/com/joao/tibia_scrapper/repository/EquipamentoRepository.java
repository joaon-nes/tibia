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

       List<Equipamento> findByNomeContainingIgnoreCase(String nome);

       boolean existsByNome(String nome);

       @Query("SELECT e FROM Equipamento e WHERE " +
                     "(:categorias IS NULL OR e.categoria IN :categorias) AND " +
                     "(:level IS NULL OR e.levelMinimo <= :level) AND " +
                     "(:vocacao IS NULL OR :vocacao = '' OR e.vocacoes IS NULL OR e.vocacoes = '' OR LOWER(e.vocacoes) LIKE LOWER(CONCAT('%', :vocacao, '%')) OR LOWER(e.vocacoes) LIKE '%todas%') AND "
                     +
                     "(:elemento IS NULL OR :elemento = '' OR LOWER(e.danoElemental) LIKE LOWER(CONCAT('%', :elemento, '%'))) AND "
                     +
                     "(:protecao IS NULL OR :protecao = '' OR LOWER(e.protecao) LIKE LOWER(CONCAT('%', :protecao, '%'))) AND "
                     +
                     "(:atributos IS NULL OR :atributos = '' OR LOWER(e.atributos) LIKE LOWER(CONCAT('%', :atributos, '%'))) AND "
                     +
                     "(:bonusEspecial IS NULL OR :bonusEspecial = '' OR " +
                     "   (:bonusEspecial = 'Life Leech' AND e.lifeLeechChance > 0) OR " +
                     "   (:bonusEspecial = 'Mana Leech' AND e.manaLeechChance > 0) OR " +
                     "   (:bonusEspecial = 'Critical' AND e.criticalChance > 0) OR " +
                     "   (:bonusEspecial = 'Mantra' AND e.mantra > 0) " +
                     ") AND " +
                     "(:slots IS NULL OR e.slots = :slots) AND " +
                     "(:tier IS NULL OR e.tier = :tier)")
       List<Equipamento> findComFiltrosAvancados(
                     @Param("categorias") List<String> categorias,
                     @Param("level") Integer level,
                     @Param("vocacao") String vocacao,
                     @Param("elemento") String elemento,
                     @Param("protecao") String protecao,
                     @Param("atributos") String atributos,
                     @Param("bonusEspecial") String bonusEspecial,
                     @Param("slots") Integer slots,
                     @Param("tier") Integer tier);

       @Query("SELECT e FROM Equipamento e WHERE LOWER(e.nome) LIKE LOWER(CONCAT('%', :termo, '%')) " +
                     "AND (:levelUser = 0 OR e.levelMinimo IS NULL OR e.levelMinimo <= :levelUser) " +
                     "AND (:vocUser = '' OR e.vocacoes IS NULL OR e.vocacoes = '' OR LOWER(e.vocacoes) LIKE '%todas%' OR LOWER(e.vocacoes) LIKE LOWER(CONCAT('%', :vocUser, '%')))")
       List<Equipamento> findFiltrado(@Param("termo") String termo,
                     @Param("levelUser") int levelUser,
                     @Param("vocUser") String vocUser);
}