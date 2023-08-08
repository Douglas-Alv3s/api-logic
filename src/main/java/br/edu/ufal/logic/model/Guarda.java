package br.edu.ufal.logic.model;

public class Guarda {
    
    private Usuario usuario;
    private Form_FBF form_FBF;
    private Form_Argumento form_Argumento;
    private int contagem;

    public Guarda(Usuario usuario, Form_FBF form_FBF, Form_Argumento form_Argumento, int contagem) {
        this.usuario = usuario;
        this.form_FBF = form_FBF;
        this.form_Argumento = form_Argumento;
        this.contagem = contagem; 
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Form_FBF getForm_FBF() {
        return form_FBF;
    }

    public void setForm_FBF(Form_FBF form_FBF) {
        this.form_FBF = form_FBF;
    }

    public Form_Argumento getForm_Argumento() {
        return form_Argumento;
    }

    public void setForm_Argumento(Form_Argumento form_Argumento) {
        this.form_Argumento = form_Argumento;
    }

    public int getContagem() {
        return contagem;
    }

    public void setContagem(int contagem) {
        this.contagem = contagem;
    }

    @Override
    public String toString() {
        return "Guarda [usuario=" + usuario + ", form_FBF=" + form_FBF + ", form_Argumento=" + form_Argumento
                + ", contagem=" + contagem + "]";
    }
    
}
