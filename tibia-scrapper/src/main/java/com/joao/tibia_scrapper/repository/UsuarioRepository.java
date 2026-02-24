package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.charName IS NOT NULL AND u.charName <> ''")
    long countPersonagensLinkados();

    @Query("SELECT u.charVocation, COUNT(u) FROM Usuario u WHERE u.charVocation IS NOT NULL AND u.charVocation <> '' GROUP BY u.charVocation")
    List<Object[]> countByVocation();

    @Query("SELECT u.charWorld, COUNT(u) FROM Usuario u WHERE u.charWorld IS NOT NULL AND u.charWorld <> '' GROUP BY u.charWorld")
    List<Object[]> countByWorld();

    @Query("SELECT u.charResidence, COUNT(u) FROM Usuario u WHERE u.charResidence IS NOT NULL AND u.charResidence <> '' GROUP BY u.charResidence")
    List<Object[]> countByCity();
}