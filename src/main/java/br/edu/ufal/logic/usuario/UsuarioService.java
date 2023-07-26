package br.edu.ufal.logic.usuario;

import org.springframework.stereotype.Service;

import br.edu.ufal.logic.DAO.DAOUsuario;
import br.edu.ufal.logic.DAO.dataSource.MySQLDataSource;
import br.edu.ufal.logic.model.Usuario;

@Service
public class UsuarioService {
    
    
    private DAOUsuario daoUsuario = new DAOUsuario(MySQLDataSource.getInstance());

    public void criarNovoUsuario(Usuario usuario){
        try {
            daoUsuario.adicionar(usuario);
        } catch (Exception e) {
            System.out.println("Email existente.");
        }
    }

    public Usuario verificarEmailExistente(String email){
        try{
            return daoUsuario.consultarEmail(email);
        } catch (Exception e) {
            return daoUsuario.consultarEmail(email);
        }
    }
    
}
