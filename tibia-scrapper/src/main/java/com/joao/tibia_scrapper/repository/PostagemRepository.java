package com.joao.tibia_scrapper.repository;

import com.joao.tibia_scrapper.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {
}