package br.edu.ufal.logic.model;

public class Form_FBF {
    
    private Integer id_FBF;
    private String formula_FBF;
    private String url_FBF;

    public Form_FBF(String formula_FBF, String url_FBF) {
        this.formula_FBF = formula_FBF;
        this.url_FBF = url_FBF;
    }

    public Form_FBF(Integer id_FBF, String formula_FBF, String url_FBF) {
        this.id_FBF = id_FBF;
        this.formula_FBF = formula_FBF;
        this.url_FBF = url_FBF;
    }

    public Integer getId_FBF() {
        return id_FBF;
    }

    public void setId_FBF(Integer id_FBF) {
        this.id_FBF = id_FBF;
    }

    public String getFormula_FBF() {
        return formula_FBF;
    }

    public void setFormula_FBF(String formula_FBF) {
        this.formula_FBF = formula_FBF;
    }

    public String getUrl_FBF() {
        return url_FBF;
    }

    public void setUrl_FBF(String url_FBF) {
        this.url_FBF = url_FBF;
    }

    @Override
    public String toString() {
        return "Form_FBF [id_FBF=" + id_FBF + ", formula_FBF="
                + formula_FBF + ", url_FBF=" + url_FBF + "]";
    }
}
