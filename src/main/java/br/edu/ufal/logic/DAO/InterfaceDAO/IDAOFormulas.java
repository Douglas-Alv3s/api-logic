package br.edu.ufal.logic.DAO.InterfaceDAO;

public interface IDAOFormulas<Tipo> {
    public Tipo consultar(String id);
    public int resgatarUltimoID();
    
}
