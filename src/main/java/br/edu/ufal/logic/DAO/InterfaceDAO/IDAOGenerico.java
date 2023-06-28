package br.edu.ufal.logic.DAO.InterfaceDAO;

import java.util.ArrayList;

public interface IDAOGenerico<Tipo> {
    public Tipo consultar(int id);
    public void adicionar(Tipo objeto);
    public void remover(int id);
    public void alterar(Tipo chaveAntiga, Tipo dadosNovos);
    public ArrayList<Tipo> obterTodos();
}
