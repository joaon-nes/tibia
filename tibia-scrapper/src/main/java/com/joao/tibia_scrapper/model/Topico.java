package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String conteudo;

    private LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Postagem> postagens = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "topico_votos", joinColumns = @JoinColumn(name = "topico_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> votosUteis = new HashSet<>();

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