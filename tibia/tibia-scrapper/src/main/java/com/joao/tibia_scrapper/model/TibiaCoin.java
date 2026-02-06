package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tibia_coin")
public class TibiaCoin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String mundo;
    private Integer precoMedio;
    private String tempoReferenciaSite;
    private LocalDateTime ultimaAtualizacao;
}