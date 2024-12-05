package com.MunguVision.Xrange.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Représente une entité utilisateur avec des détails d'authentification.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Retourne les autorités accordées à l'utilisateur.
     *
     * @return une collection d'autorités accordées
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Retourne le mot de passe utilisé pour authentifier l'utilisateur.
     *
     * @return le mot de passe
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retourne le nom d'utilisateur utilisé pour authentifier l'utilisateur.
     *
     * @return le nom d'utilisateur
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indique si le compte de l'utilisateur a expiré.
     *
     * @return true si le compte n'est pas expiré, false sinon
     */
    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    /**
     * Indique si l'utilisateur est verrouillé ou déverrouillé.
     *
     * @return true si le compte n'est pas verrouillé, false sinon
     */
    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    /**
     * Indique si les informations d'identification (mot de passe) de l'utilisateur ont expiré.
     *
     * @return true si les informations d'identification ne sont pas expirées, false sinon
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    /**
     * Indique si l'utilisateur est activé ou désactivé.
     *
     * @return true si l'utilisateur est activé, false sinon
     */
    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}