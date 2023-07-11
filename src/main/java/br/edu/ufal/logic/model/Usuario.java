package br.edu.ufal.logic.model;

public class Usuario {
    
    private int id_usuario;
    private String email;
    private String nome;
    private String senha;
    
    public Usuario(String email, String nome, String senha) {
        this.email = email;
        this.nome = nome;
        this.senha = senha;
    }

    public Usuario(int id_usuario, String email, String nome, String senha) {
        this.id_usuario = id_usuario;
        this.email = email;
        this.nome = nome;
        this.senha = senha;
    }
    
    public int getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(int id_usuario) {
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
