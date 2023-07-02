package br.edu.ufal.logic.DAO;

import java.sql.ResultSet;
import java.util.ArrayList;

import br.edu.ufal.logic.DAO.InterfaceDAO.IDAOGenerico;
import br.edu.ufal.logic.DAO.dataSource.MySQLDataSource;
import br.edu.ufal.logic.model.Form_FBF;
import br.edu.ufal.logic.model.Form_FBF;
import br.edu.ufal.logic.model.Form_FBF;
import br.edu.ufal.logic.model.Form_FBF;

public class DAOForm_FBF implements IDAOGenerico<Form_FBF>{

    private MySQLDataSource dataSource;

    public DAOForm_FBF(MySQLDataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public Form_FBF consultar(int id) {
         try {
            String sql = "SELECT * FROM form_fbf WHERE id_FBF = '" + id + "'";
            ResultSet resultado = dataSource.executarSelect(sql);
    
            if (resultado.next()) {
                // Extrair os dados do ResultSet e criar um objeto Form_FBF
                int id_fbf = resultado.getInt("id_FBF");
                String formula_fbf = resultado.getString("formula_FBF");
                String URL_fbf = resultado.getString("URL_FBF");
    
                Form_FBF form_fbf = new Form_FBF(id_fbf, formula_fbf, URL_fbf);
                return form_fbf;
            } else {
                // Form_FBF não encontrado
                return null;
            }
        } catch (Exception e) {
            // Tratar a exceção e retornar um valor padrão (pode ser null) em caso de erro
            System.err.println("Erro ao consultar Form_FBF no banco de dados: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void adicionar(Form_FBF form_FBF) {
        try {
            int id_FBF = form_FBF.getId_FBF();
            
            // Verificar se o Form_FBF já existe
            Form_FBF form_FBFExistente = consultar(id_FBF);          
            if (form_FBFExistente != null) {
                System.out.println("O Form_FBF com id '" + id_FBF + "' já existe no banco de dados.");
                return; // Encerra o método, não adicionando um novo Form_FBF
            }
            
            String sql = "INSERT INTO form_fbf (id_FBF ,formula_FBF, URL_FBF) VALUES ('"+form_FBF.getId_FBF() +"', '" + form_FBF.getFormula_FBF() + "', '" + form_FBF.getUrl_FBF() + "')";

            dataSource.executarQueryGeral(sql);
            
            System.out.println("Form_FBF adicionado com sucesso: " + sql);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar Form_FBF no banco de dados: " + e.getMessage());
        }
    }

    @Override
    public void remover(int id_FBF) {
        try {
            // Verificar se o Form_FBF já existe
            Form_FBF form_FBFExistente = consultar(id_FBF);
                       
            if (form_FBFExistente == null) {
                System.out.println("O Form_FBF com id '" + id_FBF + "' não existe no banco de dados. Impossivel remover.");
                return; // Encerra o método, não adicionando um novo Form_FBF
            }
            String sql = "DELETE FROM form_fbf WHERE id_FBF = '" + id_FBF + "'";
            System.out.println("Form_FBF com id: '" + id_FBF+"' removido." );
            dataSource.executarQueryGeral(sql);
        } catch (Exception e) {
            System.out.println("Erro ao remover Form_FBF do banco de dados");
        }
    }

    @Override
    public void alterar(Form_FBF dadosAntigo, Form_FBF dadosNovos) {
        try {
            String sql = "UPDATE form_fbf SET id_FBF = '" + dadosNovos.getId_FBF() + "', formula_FBF = '"+ dadosNovos.getFormula_FBF()+"', URL_FBF = " + dadosNovos.getUrl_FBF() + " WHERE id_FBF = '" + dadosAntigo.getId_FBF() + "'";
            // System.out.println("Comando SQL: "+sql);
            dataSource.executarQueryGeral(sql);
        } catch (Exception e) {
            System.out.println("Erro ao alterar Form_FBF no banco de dados");
        }
    }

    @Override
    public ArrayList<Form_FBF> obterTodos() {
        ArrayList<Form_FBF> form_FBFs = new ArrayList<>();
        try {
            String sql = "SELECT * FROM form_FBF";
            ResultSet resultado = dataSource.executarSelect(sql);
            while (resultado.next()) {
                int id_FBF = resultado.getInt("id_FBF");
                String formula_FBF = resultado.getString("formula_FBF");
                String URL_FBF = resultado.getString("URL_FBF");

                Form_FBF form_FBF = new Form_FBF(id_FBF, formula_FBF, URL_FBF);
                form_FBF.setUrl_FBF(URL_FBF);
                form_FBFs.add(form_FBF);
            }
            resultado.close();
        } catch (Exception e) {
            System.out.println("Erro ao obter todos os clientes do banco de dados");
        }
        return form_FBFs;
    }
}
    

