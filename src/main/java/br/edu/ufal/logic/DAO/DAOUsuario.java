package br.edu.ufal.logic.DAO;

import java.sql.ResultSet;
import java.util.ArrayList;

import br.edu.ufal.logic.DAO.InterfaceDAO.IDAOGenerico;
import br.edu.ufal.logic.DAO.InterfaceDAO.IDAOUsuario;
import br.edu.ufal.logic.DAO.dataSource.MySQLDataSource;
import br.edu.ufal.logic.model.Usuario;


public class DAOUsuario implements IDAOGenerico<Usuario>, IDAOUsuario<Usuario>{
    private MySQLDataSource dataSource;

    public DAOUsuario(MySQLDataSource dataSource){
        this.dataSource = dataSource;
    }
    
    @Override
    public Usuario consultar(String emailVerificar) {
         try {
            String sql = "SELECT * FROM usuario WHERE email = '" + emailVerificar + "'";
            ResultSet resultado = dataSource.executarSelect(sql);
    
            if (resultado.next()) {
                // Extrair os dados do ResultSet e criar um objeto Usuario
                int id_usuario = resultado.getInt("id_usuario");
                String nome = resultado.getString("nome");
                String email = resultado.getString("email");
                String senha = resultado.getString("senha");
    
                Usuario usuario = new Usuario(id_usuario, nome, email, senha);
                return usuario;
            } else {
                // Usuario não encontrado
                return null;
            }
        } catch (Exception e) {
            // Tratar a exceção e retornar um valor padrão (pode ser null) em caso de erro
            System.err.println("Erro ao consultar Usuario no banco de dados: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void adicionar(Usuario usuario) {
        try {
            String email_usuario = usuario.getEmail();
            
            // Verificar se o Usuario já existe
            Usuario UsuarioExistente = consultar(email_usuario);          
            if (UsuarioExistente != null) {
                System.out.println("O Usuario com email '" + email_usuario + "' já existe no banco de dados.");
                return ; // Encerra o método, não adicionando um novo Usuario
            }
            
            String sql = "INSERT INTO usuario (id_usuario , nome, email, senha) VALUES ('"+usuario.getId_usuario() +"', '" + usuario.getNome() + "', '" + usuario.getEmail() + "', '" + usuario.getSenha() + "')";

            dataSource.executarQueryGeral(sql);
            
            // System.out.println("Usuario adicionado com sucesso: " + sql);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar Usuario no banco de dados: " + e.getMessage());
        }
    }

    @Override
    public void remover(String emailUsuario) {
        try {
            // Verificar se o Usuario já existe
            Usuario usuarioExistente = consultar(emailUsuario);
                       
            if (usuarioExistente == null) {
                System.out.println("O Usuario com id '" + emailUsuario + "' não existe no banco de dados. Impossivel remover.");
                return; // Encerra o método, não adicionando um novo Usuario
            }
            String sql = "DELETE FROM usuario WHERE email = '" + emailUsuario + "'";
            System.out.println("Usuario com email: '" + emailUsuario+"' removido." );
            dataSource.executarQueryGeral(sql);
        } catch (Exception e) {
            System.out.println("Erro ao remover Usuario do banco de dados");
        }
    }

    @Override
    public void alterar(Usuario dadosAntigo, Usuario dadosNovos) {
       try {
            String sql = "UPDATE usuario SET id_usuario = '" + dadosNovos.getId_usuario() + "', nome = '"+ dadosNovos.getNome()+"', email = " + dadosNovos.getEmail() + "', senha = "+ dadosNovos.getSenha() + "' WHERE id_usuario = '" + dadosAntigo.getId_usuario() + "'";
            // System.out.println("Comando SQL: "+sql);
            dataSource.executarQueryGeral(sql);
        } catch (Exception e) {
            System.out.println("Erro ao alterar usuario no banco de dados");
        }
    }

    @Override
    public ArrayList<Usuario> obterTodos() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            String sql = "SELECT * FROM usuario";
            ResultSet resultado = dataSource.executarSelect(sql);
            while (resultado.next()) {
                int id_usuario = resultado.getInt("id_usuario");
                String nome = resultado.getString("nome");
                String email = resultado.getString("email");
                String senha = resultado.getString("senha");

                Usuario usuario = new Usuario(id_usuario, nome, email, senha);
                usuarios.add(usuario);
            }
            resultado.close();
        } catch (Exception e) {
            System.out.println("Erro ao obter todos usuarios do banco de dados");
        }
        return usuarios;
    }
    
}
