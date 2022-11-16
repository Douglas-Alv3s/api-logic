package br.edu.ufal.logic.argument;

public class ArgumentDTO {

	private int id;
	
	private String argument;

	private String regs;
	
	public ArgumentDTO(){
		
	}
	
	public ArgumentDTO(int id, String argument, String regras) {
		this.id = id;
		this.argument = argument;
		this.regs = regras;
	}
	
	public String getArgument() {
		return argument;
	}
	
	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getRegs() {
		return regs;
	}

	public void setRegs(String regs) {
		this.regs = regs;
	}
	


}
