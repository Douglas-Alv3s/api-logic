package br.edu.ufal.logic.DAO;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import br.edu.ufal.logic.DAO.InterfaceDAO.IDAOGuarda;
import br.edu.ufal.logic.DAO.dataSource.MySQLDataSource;
import br.edu.ufal.logic.model.Form_Argumento;
import br.edu.ufal.logic.model.Form_FBF;
import br.edu.ufal.logic.model.Guarda;
import br.edu.ufal.logic.model.Usuario;

public class DAOGuarda implements IDAOGuarda{

    private MySQLDataSource dataSource;

    public DAOGuarda( MySQLDataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public ArrayList<Guarda> mostrarFormulasGuardadas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mostrarFormulasGuardadas'");
    }

    public Guarda consultar(String id_usuario, String url_argumento)  {
        try {

            String sql = "SELECT * FROM guarda WHERE id_usuarioFK = '"+ id_usuario +"' AND url_argumentoFK = '" + url_argumento + "'";
            ResultSet resultado = dataSource.executarSelect(sql);
    
        //     String sql = "SELECT * FROM guarda WHERE formula_argumentoFK = '" + formulaID + "'";
        // ResultSet resultado = dataSource.executarSelect(sql);

            if (resultado.next()) {
                // Extrair os dados do ResultSet e criar um objeto Guarda
                String id_usuarioFK = resultado.getString("id_usuarioFK");
                String URL_argumento = resultado.getString("url_argumentoFK");
                String URL_FBF = resultado.getString("url_FBF_FK");
                int contagem = resultado.getInt("contagem");

                // Criar objetos Form_Argumento e Form_FBF, se necessário (não fornecidos no construtor)
                DAOUsuario daoUsuario = new DAOUsuario(dataSource);
                Usuario usuario = daoUsuario.consultarID(id_usuarioFK);
                Form_Argumento form_argumento = new Form_Argumento(null, null, URL_argumento);
                Form_FBF form_FBF = new Form_FBF(null, URL_FBF);

                // Criar objeto Guarda com os objetos Form_Argumento e Form_FBF
                Guarda guarda = new Guarda(usuario, form_FBF, form_argumento, contagem);
                return guarda;
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
    public void realizarRegistroArgumento(Guarda guardar) {

        try {
            // Boolean consultaResultado = consultar(guardar.getForm_Argumento().getFormula_argumento());
            UUID id_usuario = guardar.getUsuario().getId_usuario();
            String id_usuarioString = id_usuario.toString();
            String url_argumento = guardar.getForm_Argumento().getUrl_argumento();
            int contagem = guardar.getContagem();

            Guarda guardarExistente = consultar(id_usuarioString, url_argumento);

            if(guardarExistente!=null){
                   String sql = "UPDATE guarda SET contagem = '"+contagem+"' WHERE id_usuarioFK = '"+id_usuarioString+"' AND url_argumentoFK ='"+url_argumento+"'";
                    // System.out.println("Contagem atualizada para ->"+contagem); 

                dataSource.executarQueryGeral(sql);
                return;   
            }
            String sql = "INSERT INTO guarda (id_usuarioFK, url_argumentoFK, contagem) VALUES ('" +
                guardar.getUsuario().getId_usuario() + "', '" +
                guardar.getForm_Argumento().getUrl_argumento() +"','"+
                guardar.getContagem()+"')";
                dataSource.executarQueryGeral(sql);
                System.out.println("Registro adicionado com sucesso.");
            
        } catch (Exception e) {
            System.out.println("Erro ao adicionar a guardar: " + e.getMessage());
        }
    }

    @Override
    public void realizarRegistroFBF(Guarda guardar) {

        try {
            String sql = "INSERT INTO guarda (id_usuarioFK, url_FBF_FK, formula_FBF_FK) VALUES ('" +
                guardar.getUsuario().getId_usuario() + "', '" +
                guardar.getForm_FBF().getUrl_FBF()+"','"+
                guardar.getForm_FBF().getFormula_FBF()+"')";
            dataSource.executarQueryGeral(sql);
            System.out.println("Registro adicionado com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro ao adicionar a guardar: " + e.getMessage());
        }
    }


    @Override
    public void obterNomeClienteEFormulas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obterNomeClienteEFormulas'");
    }

    @Override
    public Integer consultarRegistro(String id_usuario, String URL_requisitado, String tipoGeracao) {
        Integer contadorDeRegistros = 0;
 
        try{
            String sql = null;
            // Cria a consulta SQL para obter todos as requisições do usuario, para aquele tipo de geração
            if(tipoGeracao.equals("argumento")){
                sql = "SELECT contagem  FROM guarda WHERE id_usuarioFK = '"+ id_usuario +"' AND url_argumentoFK = '"+ URL_requisitado + "'";
            }else if(tipoGeracao.equals("FBF")){
                sql = "SELECT contagem FROM guarda WHERE id_usuarioFK = '"+ id_usuario +"'AND url_FBF_FK = '"+ URL_requisitado +"'";
            }

            ResultSet resultSet = dataSource.executarSelect(sql);
            if(resultSet.next()){
                contadorDeRegistros = resultSet.getInt("contagem");
                System.out.println("\n\n------------------->>>> A contagem foi essa aqui: "+contadorDeRegistros+"\n\n");
            }
        }catch(Exception e){
            System.out.println("Erro causado por: "+e.getMessage());
        }
        return contadorDeRegistros;
    }

    
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
