package com.vojtechruzicka.javafxweaverexample.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String login;
    private String pass;
    private int rol;

    public UserEntity(String login, String pass, int rol) {
        this.login = login;
        this.pass = pass;
        this.rol = rol;
    }

    public UserEntity() {
    }

    public Integer getId() {return id;}
    public String getLogin() {return login;}
    public void setLogin(String login) {this.login = login;}
    public String getPass() {return pass;}
    public void setPass(String pass) {this.pass = pass;}
    public int getRol() {return rol;}
    public void setRol(int rol) {this.rol = rol;}
}
