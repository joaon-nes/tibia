package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class HuntRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String sessionStart;
    private String sessionLength;
    private String xpGain;
    private String loot;
    private String supplies;
    private String balance;
    private boolean isWaste;

    @Column(columnDefinition = "LONGTEXT")
    private String huntJsonOriginal;

    @Column(columnDefinition = "LONGTEXT")
    private String partySnapshot;

    @Column(unique = true, length = 15)
    private String slug;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private LocalDateTime dataRegistro = LocalDateTime.now();
}