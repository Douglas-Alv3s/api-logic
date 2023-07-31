package br.edu.ufal.logic.DAO.InterfaceDAO;

import java.util.ArrayList;

import br.edu.ufal.logic.model.Guarda;

public interface IDAOGuarda {
    public ArrayList<Guarda> mostrarFormulasGuardadas() ;
    public void realizarRegistroArgumento(Guarda guarda) ;
    public Integer consultarRegistro(String id_usuario, String URL_requisitado, String tipoGeracao );
    public void realizarRegistroFBF(Guarda guarda);
    public void obterNomeClienteEFormulas() ;
}
