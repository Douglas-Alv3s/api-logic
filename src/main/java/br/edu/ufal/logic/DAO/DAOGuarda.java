package br.edu.ufal.logic.DAO;

import java.util.ArrayList;

import br.edu.ufal.logic.DAO.InterfaceDAO.IDAOGuarda;
import br.edu.ufal.logic.DAO.dataSource.MySQLDataSource;
import br.edu.ufal.logic.model.Guarda;

public class DAOGuarda implements IDAOGuarda{

    private MySQLDataSource dataSource;

    public DAOGuarda( MySQLDataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList<Guarda> mostrarFormulasGuardadas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mostrarFormulasGuardadas'");
    }

    // @Override
    // public void realizarRegistro(Guarda guardar) {
    //     String sql = "INSERT INTO guarda (id_usuarioFK, id_argumentoFK, id_FBF_FK) VALUES ('" +
    //             guardar.getUsuario().getId_usuario() + "', '" +
    //             guardar.getForm_Argumento().getId_argumento() + "', '" +
    //             guardar.getForm_FBF().getId_FBF()+"')";

    //     try {
    //         dataSource.executarQueryGeral(sql);
    //     } catch (Exception e) {
    //         System.out.println("Erro ao adicionar a guardar.");
    //     }
    // }

    @Override
    public void realizarRegistroArgumento(Guarda guardar) {

        try {
            String sql = "INSERT INTO guarda (id_usuarioFK, id_argumentoFK) VALUES ('" +
                guardar.getUsuario().getId_usuario() + "', '" +
                guardar.getForm_Argumento().getId_argumento() +"')";
            dataSource.executarQueryGeral(sql);
            System.out.println("Registro adicionado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao adicionar a guardar: " + e.getMessage());
        }
    }

    @Override
    public void realizarRegistroFBF(Guarda guardar) {

        try {
            String sql = "INSERT INTO guarda (id_usuarioFK, id_FBF_FK) VALUES ('" +
                guardar.getUsuario().getId_usuario() + "', '" +
                guardar.getForm_FBF().getId_FBF()+"')";
            dataSource.executarQueryGeral(sql);
            System.out.println("Registro adicionado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao adicionar a guardar: " + e.getMessage());
        }
    }


    @Override
    public void obterNomeClienteEFormulas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obterNomeClienteEFormulas'");
    }

   
    
}
