package br.edu.ufal.logic.DAO.InterfaceDAO;

public interface IDAOUsuario<Tipo> {
    public Tipo consultar(String email);
    public void remover(String email);
}
