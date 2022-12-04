package br.edu.ufal.logic.fbf;

public class FBFDTO {
	
	private int id; // Identificação da formula
	
	private String fbf; 

	// private String regras;
	
	public FBFDTO() {}
	
	public FBFDTO(int id, String fbf) {
		this.id = id;
		this.fbf = fbf;
		
	}
	
	public String getFbf() {
		return fbf;
	}
	
	public void setFbf(String fbf) {
		this.fbf = fbf;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	
}
