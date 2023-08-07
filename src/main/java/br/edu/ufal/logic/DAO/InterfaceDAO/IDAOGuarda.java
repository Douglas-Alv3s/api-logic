package br.edu.ufal.logic.DAO.InterfaceDAO;

import java.util.ArrayList;

import br.edu.ufal.logic.model.Guarda;

public interface IDAOGuarda {
    public void realizarRegistro(Guarda guarda) ;
    public Integer consultarRegistro(String id_usuario, String URL_requisitado);

}
