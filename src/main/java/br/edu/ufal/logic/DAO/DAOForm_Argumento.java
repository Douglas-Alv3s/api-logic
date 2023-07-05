package br.edu.ufal.logic.DAO;

import java.sql.ResultSet;
import java.util.ArrayList;

import br.edu.ufal.logic.DAO.InterfaceDAO.IDAOFormulas;
import br.edu.ufal.logic.DAO.InterfaceDAO.IDAOGenerico;
import br.edu.ufal.logic.DAO.dataSource.MySQLDataSource;
import br.edu.ufal.logic.model.Form_Argumento;

public class DAOForm_Argumento implements IDAOGenerico<Form_Argumento>, IDAOFormulas<Form_Argumento>{
    
    private MySQLDataSource dataSource;
    
    
    public DAOForm_Argumento(MySQLDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Form_Argumento consultar(int id)  {
        try {
            String sql = "SELECT * FROM form_argumento WHERE id_argumento = '" + id + "'";
            ResultSet resultado = dataSource.executarSelect(sql);
    
            if (resultado.next()) {
                // Extrair os dados do ResultSet e criar um objeto Form_Argumento
                int id_argumento = resultado.getInt("id_argumento");
                String formula_argumento = resultado.getString("formula_argumento");
                String regras = resultado.getString("regras");
                String URL_argumento = resultado.getString("URL_argumento");
    
                Form_Argumento form_Argumento = new Form_Argumento(id_argumento, formula_argumento, regras, URL_argumento);
                return form_Argumento;
            } else {
                // Form_Argumento não encontrado
                return null;
            }
        } catch (Exception e) {
            // Tratar a exceção e retornar um valor padrão (pode ser null) em caso de erro
            System.err.println("Erro ao consultar Form_Argumento no banco de dados: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void adicionar(Form_Argumento form_Argumento)  {
        try {
            int id_argumento = form_Argumento.getId_argumento();
            
            // Verificar se o Form_Argumento já existe
            Form_Argumento form_ArgumentoExistente = consultar(id_argumento);          
            if (form_ArgumentoExistente != null) {
                System.out.println("O Form_Argumento com id '" + id_argumento + "' já existe no banco de dados.");
                return; // Encerra o método, não adicionando um novo Form_Argumento
            }
            
            String sql = "INSERT INTO form_argumento (id_argumento ,formula_argumento, regras, URL_argumento) VALUES ('"+form_Argumento.getId_argumento() +"', '" + form_Argumento.getFormula_argumento() + "', '" + form_Argumento.getRegras() + "', '" + form_Argumento.getUrl_argumento() + "')";

            dataSource.executarQueryGeral(sql);
            
            System.out.println("Form_Argumento adicionado com sucesso: " + sql);
        } catch (Exception e) {
            System.out.println("Erro ao adicionar Form_Argumento no banco de dados: " + e.getMessage());
        }
    }

    
    @Override
    public void remover(int id_argumento)  {
        try {
            // Verificar se o Form_Argumento já existe
            Form_Argumento form_ArgumentoExistente = consultar(id_argumento);
                       
            if (form_ArgumentoExistente == null) {
                System.out.println("O Form_Argumento com nome '" + id_argumento + "' não existe no banco de dados. Impossivel remover.");
                return; // Encerra o método, não adicionando um novo Form_Argumento
            }
            String sql = "DELETE FROM form_argumento WHERE id_argumento = '" + id_argumento + "'";
            System.out.println("Form_Argumento com nome: '" + id_argumento+"' removido." );
            dataSource.executarQueryGeral(sql);
        } catch (Exception e) {
            System.out.println("Erro ao remover Form_Argumento do banco de dados");
        }
    }

    @Override
    public void alterar(Form_Argumento dadosAntigo, Form_Argumento dadosNovos){
        try {
            String sql = "UPDATE form_argumento SET id_argumento = '" + dadosNovos.getId_argumento() + "', formula_argumento = '"+ dadosNovos.getFormula_argumento()+"', regras = " + dadosNovos.getRegras() + ", URL_argumento = " + dadosNovos.getUrl_argumento() + " WHERE id_argumento = '" + dadosAntigo.getId_argumento() + "'";
            // System.out.println("Comando SQL: "+sql);
            dataSource.executarQueryGeral(sql);
        } catch (Exception e) {
            System.out.println("Erro ao alterar Form_Argumento no banco de dados");
        }
    }
   

    @Override
    public ArrayList<Form_Argumento> obterTodos() {
        ArrayList<Form_Argumento> form_argumentos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM form_argumento";
            ResultSet resultado = dataSource.executarSelect(sql);
            while (resultado.next()) {
                int id_argumento = resultado.getInt("id_argumento");
                String formula_argumento = resultado.getString("formula_argumento");
                String regras = resultado.getString("regras");
                String URL_argumento = resultado.getString("URL_argumento");

                Form_Argumento form_argumento = new Form_Argumento(id_argumento, formula_argumento, regras, URL_argumento);
                form_argumento.setUrl_argumento(URL_argumento);
                form_argumentos.add(form_argumento);
            }
            resultado.close();
        } catch (Exception e) {
            System.out.println("Erro ao obter todos os clientes do banco de dados");
        }
        return form_argumentos;
    }

    @Override
    public int resgatarUltimoID() {
        int ultimoId = 0;

        try{
            // Cria a consulta SQL para obter o último id adicionado
            String sql = "SELECT * FROM form_argumento ORDER BY id_argumento DESC LIMIT 1";
            // Executa a consulta
            ResultSet resultSet  = dataSource.executarSelect(sql);

            if(resultSet.next()){
                ultimoId = resultSet.getInt("id_argumento");
            }
        }catch(Exception e){

        }
        return ultimoId;
    }

    
}
