package com.example.oauth2study.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProviderType provider;

    @Column(nullable = false,unique = true)
    private String providerId;

    @Column(unique = true)
    private String refreshToken;

    @Builder
    public User(String password, String name, String email, Role role, ProviderType provider, String providerId) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
    public User update(String name){
        this.name = name;
        return this;
    }

    public void assignRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
