package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.TibiaCoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TibiaCoinRepository extends JpaRepository<TibiaCoin, Long> {
    Optional<TibiaCoin> findByMundo(String mundo);

    List<TibiaCoin> findAllByOrderByMundoAsc();
}