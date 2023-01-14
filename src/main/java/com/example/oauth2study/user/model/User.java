package com.example.oauth2study.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Role role;

    @Builder

    public User(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
    public User update(String name){
        this.name = name;
        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
