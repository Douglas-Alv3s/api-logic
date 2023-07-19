package br.edu.ufal.logic.DAO.InterfaceDAO;

import java.util.ArrayList;

public interface IDAOGenerico<Tipo> {
    public void adicionar(Tipo objeto);
    public void alterar(Tipo dadosAntigo, Tipo dadosNovos);
    public ArrayList<Tipo> obterTodos();
}
