package br.edu.ufal.logic.model;

import java.util.UUID;

public class Usuario {
    
    private UUID id_usuario;
    private String email;
    private String nome;
    private String senha;
    
    public Usuario(String email, String nome, String senha) {
        this.id_usuario = UUID.randomUUID();
        this.email = email;
        this.nome = nome;
        this.senha = senha;
    }

    public Usuario(UUID id_usuario, String email, String nome, String senha) {
        this.id_usuario = id_usuario;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
    }
    
    public UUID getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(UUID id_usuario) {
        this.id_usuario = id_usuario;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Usuario [email=" + email + ", nome=" + nome + "]";
    }

}
