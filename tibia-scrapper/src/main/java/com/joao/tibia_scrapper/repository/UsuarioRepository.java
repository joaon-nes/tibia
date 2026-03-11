package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByVerificationToken(String token);
    
    Optional<Usuario> findByResetPasswordToken(String token);

    Optional<Usuario> findByCharNameIgnoreCase(String charName);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.charName IS NOT NULL AND u.charName <> ''")
    long countPersonagensLinkados();

    @Query("SELECT u FROM Usuario u WHERE u.charName IS NOT NULL AND u.charName <> ''")
    List<Usuario> findAllUsuariosComPersonagem();

    @Query("SELECT u.charVocation, COUNT(u) FROM Usuario u WHERE u.charVocation IS NOT NULL AND u.charVocation <> '' GROUP BY u.charVocation")
    List<Object[]> countByVocation();

    @Query("SELECT u.charWorld, COUNT(u) FROM Usuario u WHERE u.charWorld IS NOT NULL AND u.charWorld <> '' GROUP BY u.charWorld")
    List<Object[]> countByWorld();

    @Query("SELECT u.charResidence, COUNT(u) FROM Usuario u WHERE u.charResidence IS NOT NULL AND u.charResidence <> '' GROUP BY u.charResidence")
    List<Object[]> countByCity();

    List<Usuario> findTop5ByCharNameIsNotNullOrderByCharLevelDesc();

    List<Usuario> findTop5ByCharNameIsNotNullOrderByMagicLevelDesc();

    List<Usuario> findTop5ByCharNameIsNotNullOrderByDistanceSkillDesc();

    List<Usuario> findTop5ByCharNameIsNotNullOrderBySwordSkillDesc();

    List<Usuario> findTop5ByCharNameIsNotNullOrderByAxeSkillDesc();

    List<Usuario> findTop5ByCharNameIsNotNullOrderByClubSkillDesc();

    List<Usuario> findTop5ByCharNameIsNotNullOrderByFistSkillDesc();

    List<Usuario> findTop5ByCharNameIsNotNullOrderByShieldingSkillDesc();

    List<Usuario> findTop5ByCharNameIsNotNullOrderByFishingSkillDesc();

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :busca, '%')) OR LOWER(u.charName) LIKE LOWER(CONCAT('%', :busca, '%')) ORDER BY CASE u.role WHEN 'ADMIN' THEN 1 WHEN 'MODERATOR' THEN 2 WHEN 'USER' THEN 3 WHEN 'BANNED' THEN 4 ELSE 5 END, u.id DESC")
    List<Usuario> findUsuariosGerenciamento(@Param("busca") String busca);
}