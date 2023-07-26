package br.edu.ufal.logic.model;

public class Form_Argumento {
    
    private Integer id_argumento;
    private String formula_argumento;
    private String regras;
    private String url_argumento;
    
    
    public Form_Argumento(String formula_argumento, String regras, String url_argumento) {
        this.formula_argumento = formula_argumento;
        this.regras = regras;
        this.url_argumento = url_argumento;
    }

    public Form_Argumento(Integer id_argumento, String formula_argumento, String regras, String url_argumento) {
        this.id_argumento = id_argumento;
        this.formula_argumento = formula_argumento;
        this.regras = regras;
        this.url_argumento = url_argumento;
    }

    public Integer getId_argumento() {
        return id_argumento;
    }


    public void setId_argumento(Integer id_argumento) {
        this.id_argumento = id_argumento;
    }


    public String getRegras() {
        return regras;
    }


    public void setRegras(String regras) {
        this.regras = regras;
    }


    public String getFormula_argumento() {
        return formula_argumento;
    }


    public void setFormula_argumento(String formula_argumento) {
        this.formula_argumento = formula_argumento;
    }


    public String getUrl_argumento() {
        return url_argumento;
    }


    public void setUrl_argumento(String url_argumento) {
        this.url_argumento = url_argumento;
    }

    @Override
    public String toString() {
        return "Form_Argumento [id_argumento=" + id_argumento + ", regras=" + regras + ", formula_argumento="
                + formula_argumento + ", url_argumento=" + url_argumento + "]";
    }
    
}
