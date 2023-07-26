package br.edu.ufal.logic.DAO.InterfaceDAO;

public interface IDAOUsuario<Tipo> {
    public Tipo consultarEmail(String email);
    public Tipo consultarID(String id);
    public void remover(String email);
}
