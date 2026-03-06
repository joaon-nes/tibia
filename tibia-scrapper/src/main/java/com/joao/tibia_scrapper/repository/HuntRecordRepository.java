package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.HuntRecord;
import com.joao.tibia_scrapper.model.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuntRecordRepository extends JpaRepository<HuntRecord, Long> {
    List<HuntRecord> findByUsuarioOrderByDataRegistroDesc(Usuario usuario);
    
    List<HuntRecord> findAllByOrderByDataRegistroDesc();

    HuntRecord findFirstByOrderByDataRegistroDesc();

    Optional<HuntRecord> findBySlug(String slug);

    @Query("SELECT h.usuario.username, COUNT(h) FROM HuntRecord h GROUP BY h.usuario.username ORDER BY COUNT(h) DESC")
    List<Object[]> findTopHuntCreators(Pageable pageable);
}