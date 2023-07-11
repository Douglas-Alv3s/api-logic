package br.edu.ufal.logic.DAO.InterfaceDAO;

public interface IDAOFormulas<Tipo> {
    public Tipo consultar(int id);
    public void remover(int id);
    public int resgatarUltimoID();
    
}
