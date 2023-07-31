package br.edu.ufal.logic.DAO.dataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class CriacaoBD{
    
    String url = "jdbc:mysql://localhost:3306";
    String user = "root";
    String senha = "";
    String nomeBancoDados = "api_logic";
    Connection sqlConexao = null; // Responsavel pela ligação ao banco de dados
    Statement sqlInterpretador = null; // Responsavel pela execução dos comandos SQL
     
    
    public CriacaoBD() {
        DAOCreateDB();

        // Cria a tabela usuario
        DAOCreateTB("CREATE TABLE usuario (id_usuario varchar(36) PRIMARY KEY , nome varchar(30), email varchar(40), senha varchar(30))", "usuario");

        // Cria a tabela argumentos
        DAOCreateTB("CREATE TABLE form_argumento (id_argumento int PRIMARY KEY AUTO_INCREMENT, formula_argumento varchar(70), regras varchar(30), URL_argumento varchar(40))", "form_argumento");
        
        // Cria a tabela form_FBF
        DAOCreateTB("CREATE TABLE form_FBF (id_FBF int PRIMARY KEY AUTO_INCREMENT, formula_FBF varchar(70), URL_FBF varchar(40))", "form_FBF");   

        // Cria a tabela do relacionamento ternario de usuario, form_argumento e form_FBF.
        DAOCreateTB("CREATE TABLE guarda (id_usuarioFK varchar(36), url_argumentoFK varchar(40), url_FBF_FK varchar(40), contagem INT,\n" + //
                " FOREIGN KEY (id_usuarioFK) REFERENCES usuario (id_usuario), FOREIGN KEY (url_argumentoFK) REFERENCES form_argumento (URL_argumento),  FOREIGN KEY (url_FBf_FK) REFERENCES form_FBF (URL_FBF))", "guarda");
        
    }

    // Padrão de projeto Singleton pattern
    private static CriacaoBD istance = null;
    static public CriacaoBD getInstance(){
        if (istance == null){
            istance = new CriacaoBD();
        }
        return istance;
    }

    // Criação do banco de dados
    public void DAOCreateDB(){
        try {
            Connection sqlConexao = DriverManager.getConnection(url, user, senha);
            Statement sqlInterpretador = sqlConexao.createStatement();
            // Verifica se o banco de dados já existe
            String checkDatabaseQuery = "SHOW DATABASES LIKE '" + nomeBancoDados + "'";
            ResultSet resultSet = sqlInterpretador.executeQuery(checkDatabaseQuery);
            if (resultSet.next()) {
                // System.out.println("O banco de dados " + nomeBancoDados + " já existe.");
            } else {
                // Cria o banco de dados apenas se ele não existir
                String createDatabaseQuery = "CREATE DATABASE " + nomeBancoDados;
                sqlInterpretador.executeUpdate(createDatabaseQuery);
                System.out.println("Banco de dados " + nomeBancoDados + " criado com sucesso.");
            }
            url = "jdbc:mysql://localhost:3306/"+nomeBancoDados;
            
            resultSet.close();
            sqlInterpretador.close();
            sqlConexao.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Criação das tabelas
    public void DAOCreateTB(String comandoTable, String nomeTable){
        String createTableQuery = comandoTable;
        
        try (Connection sqlConexao = DriverManager.getConnection(url, user, senha);
            Statement sqlInterpretador = sqlConexao.createStatement()) {
            
            // Verifica se a tabela já existe
            String checkTableQuery = "SHOW TABLES LIKE '" + nomeTable + "'";
            ResultSet resultSet = sqlInterpretador.executeQuery(checkTableQuery);

            if (resultSet.next()) {
                // System.out.println("A tabela '"+nomeTable+"' já existe. Não é necessário criar.");
            } else {
                sqlInterpretador.executeUpdate(createTableQuery);
                System.out.println("Tabela '"+nomeTable+"' criada com sucesso.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao criar ou verificar a tabela '"+nomeTable+"'.");
            e.printStackTrace();
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNomeBancoDados() {
        return nomeBancoDados;
    }

    public void setNomeBancoDados(String nomeBancoDados) {
        this.nomeBancoDados = nomeBancoDados;
    }
}
