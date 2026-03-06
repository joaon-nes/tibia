package com.joao.tibia_scrapper.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role;
    private String nome;

    @Column(unique = true)
    private String email;

    @Column(name = "email_confirmado")
    private Boolean emailConfirmado = false;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String avatar;

    private String charName;
    private Integer charLevel;
    private String charVocation;
    private String charWorld;
    private String charResidence;

    @Column(columnDefinition = "TEXT")
    private String setsEquipamentos;

    @Column(name = "ban_expiration")
    private LocalDateTime banExpiration;

    @Column(name = "magic_level")
    private Integer magicLevel = 0;
    @Column(name = "fist_skill")
    private Integer fistSkill = 10;
    @Column(name = "club_skill")
    private Integer clubSkill = 10;
    @Column(name = "sword_skill")
    private Integer swordSkill = 10;
    @Column(name = "axe_skill")
    private Integer axeSkill = 10;
    @Column(name = "distance_skill")
    private Integer distanceSkill = 10;
    @Column(name = "shielding_skill")
    private Integer shieldingSkill = 10;
    @Column(name = "fishing_skill")
    private Integer fishingSkill = 10;

    @Column(name = "is_using_2fa", columnDefinition = "boolean default false")
    private Boolean isUsing2FA = false;

    @Column(name = "secret_2fa")
    private String secret2FA;

    public Usuario(String username, String password, String role, String nome, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nome = nome;
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == null)
            return Collections.emptyList();
        String roleName = this.role.startsWith("ROLE_") ? this.role : "ROLE_" + this.role;
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"BANNED".equals(this.role);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}