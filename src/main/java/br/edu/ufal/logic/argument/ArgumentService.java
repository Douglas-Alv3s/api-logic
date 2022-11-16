package br.edu.ufal.logic.argument;

import org.springframework.stereotype.Service;

@Service
public class ArgumentService {

	public ArgumentDTO argumentToArgumentDTO(int id, Argument argument, String regs) {
		ArgumentDTO argumentdto = new ArgumentDTO(id, argument.toString(), regs);

		return argumentdto;
	}

}
