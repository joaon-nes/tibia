package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    private LocalDateTime dataPostagem = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToMany
    @JoinTable(name = "postagem_curtidas", joinColumns = @JoinColumn(name = "postagem_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> curtidas = new HashSet<>();

    public String getNomeAutor() {
        if (autor != null) {
            if (autor.getCharName() != null && !autor.getCharName().isEmpty()) {
                return autor.getCharName();
            }
            return autor.getNome();
        }
        return "Desconhecido";
    }

    public String getDetalhesAutor() {
        if (autor != null && autor.getCharLevel() != null) {
            return "Level " + autor.getCharLevel() + " "
                    + (autor.getCharVocation() != null ? autor.getCharVocation() : "");
        }
        return "";
    }
}