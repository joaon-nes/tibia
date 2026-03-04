package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.AnuncioParty;
import com.joao.tibia_scrapper.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnuncioPartyRepository extends JpaRepository<AnuncioParty, Long> {
    
    List<AnuncioParty> findAllByOrderByDataCriacaoDesc();
    
    boolean existsByUsuario(Usuario usuario);
    
    Optional<AnuncioParty> findByUsuario(Usuario usuario);

    @Transactional
    void deleteByUsuario(Usuario usuario);
}