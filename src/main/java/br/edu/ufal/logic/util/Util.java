package br.edu.ufal.logic.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import br.edu.ufal.logic.fbf.FBF;


public class Util {

	public InstanciaRetorno montarInstancia(String instancia) {

		String mainOperatorReturn = "", unary = "", binaryLeft = "", binaryRight = "", premisses = "", conclusion = "",
				mainOperator = "";

		String[] retorno = instancia.split("\n");

		for (String string : retorno) {
			if (string.startsWith("this/Unary<:child")) {
				unary = string;
			} else if (string.startsWith("this/Binary<:right")) {
				binaryRight = string;
			} else if (string.startsWith("this/Binary<:left")) {
				binaryLeft = string;
			} else if (string.startsWith("this/FBF<:")) {
				mainOperatorReturn = string;
			} else if (string.startsWith("this/Argument<:premisse")) {
				premisses = string;
			} else if (string.startsWith("this/Argument<:conclusion")) {
				conclusion = string;
			} 

		} // falta o Atom
		
		mainOperatorReturn = mainOperatorReturn.replace("this/FBF<:mainOperator={", "");
		mainOperatorReturn = mainOperatorReturn.replace("}", "");
		
		if (mainOperatorReturn.length() > 0) {
			String mainOperatorSplit[] = mainOperatorReturn.split("->");
			mainOperator = mainOperatorSplit[1];

		}

		ArrayList<Relacao> leftRelacao = new ArrayList<Relacao>();
		ArrayList<Relacao> rightRelacao = new ArrayList<Relacao>();
		ArrayList<Relacao> notRelacao = new ArrayList<Relacao>();
		ArrayList<Relacao> argumentRelacao = new ArrayList<Relacao>();
		Relacao conclusionRelacion = new Relacao();

		Map<String, String> operators = new HashMap<String, String>();

		// operators.put("Not", "~");
		// operators.put("And", "^");
		// operators.put("Or", "v");
		// operators.put("Imply", "->");
		// operators.put("BiImply", "<->");

		// Transformação dos operadores para os seus simbolos
		// operators.put("Not", "¬");
		// operators.put("And", "ᐱ");
		// operators.put("Or", "ᐻ");
		// operators.put("Imply", "➡");
		// operators.put("BiImply", "⬌");

		operators.put("Not", "¬");
		operators.put("And", "∧");
		operators.put("Or", "∨");
		operators.put("Imply", "→");
		operators.put("BiImply", "↔");
		// not
		
		unary = unary.replace("this/Unary<:child={", "");
		unary = unary.replace("}", "");
		
		if (unary.length() > 0) {
			String notFinal[] = unary.split(", ");

			for (int i = 0; i < notFinal.length; ++i) {
				String[] relacoes = notFinal[i].split("->");
				notRelacao.add(new Relacao(relacoes[0], relacoes[1]));
			}
		}

		// and
		binaryRight = binaryRight.replace("this/Binary<:right={", "");
		binaryRight = binaryRight.replace("}", "");
		if (binaryRight.length() > 0) {
			String binaryRightFinal[] = binaryRight.split(", ");
			for (int i = 0; i < binaryRightFinal.length; ++i) {
				String[] relacoes = binaryRightFinal[i].split("->");
				rightRelacao.add(new Relacao(relacoes[0], relacoes[1]));
			}
		}

		binaryLeft = binaryLeft.replace("this/Binary<:left={", "");
		binaryLeft = binaryLeft.replace("}", "");
		if (binaryLeft.length() > 0) {
			String binaryLeftFinal[] = binaryLeft.split(", ");

			for (int i = 0; i < binaryLeftFinal.length; ++i) {
				String[] relacoes = binaryLeftFinal[i].split("->");
				leftRelacao.add(new Relacao(relacoes[0], relacoes[1]));
			}
		}

		premisses = premisses.replace("this/Argument<:premisse={", "");
		premisses = premisses.replace("}", "");
		if (premisses.length() > 0) {
			String[] premissesFinal = premisses.split(", ");

			for (int i = 0; i < premissesFinal.length; ++i) {
				String[] relacoes = premissesFinal[i].split("->");
				argumentRelacao.add(new Relacao(relacoes[0], relacoes[1]));
			}
		}

		conclusion = conclusion.replace("this/Argument<:conclusion={", "");
		conclusion = conclusion.replace("}", "");
		if (conclusion.length() > 0) {
			String[] conclusionRelacao = conclusion.split("->");
			conclusionRelacion.setLeft(conclusionRelacao[0]);
			conclusionRelacion.setRight(conclusionRelacao[1]);

		}



		InstanciaRetorno ir = new InstanciaRetorno(mainOperator, operators, leftRelacao, rightRelacao, notRelacao,
				argumentRelacao, conclusionRelacion);
		return ir;
	}

	public FBF fillWithAtoms(FBF fbf, String[] alfabeto) {
		if (!fbf.getLeft().equals("") && !(fbf.getLeft() instanceof FBF)) {
			String retorno = (String) fbf.getLeft();
			int indice = Integer.parseInt(retorno.substring(5));
			fbf.setLeft(alfabeto[indice]);
		} else {
			if (!fbf.getLeft().equals("")) {
				fillWithAtoms((FBF) fbf.getLeft(), alfabeto);
			}
		}
		if (!fbf.getRight().equals("") && !(fbf.getRight() instanceof FBF)) {
			String retorno = (String) fbf.getRight();
			int indice = Integer.parseInt(retorno.substring(5));
			fbf.setRight(alfabeto[indice]);
		} else {
			if (!fbf.getRight().equals("")) {
				fillWithAtoms((FBF) fbf.getRight(), alfabeto);
			}
		}
		return fbf;
	}

	public FBF montaFBF(String mainOperator, Map<String, String> operators, ArrayList<Relacao> notRelacao,
			ArrayList<Relacao> leftRelacao, ArrayList<Relacao> rightRelacao) {
		FBF fbf = new FBF();

		if (mainOperator.startsWith("Atom")) {
			fbf.setLeft("");
			fbf.setOperator("");
			fbf.setRight(mainOperator);

		} else if (mainOperator.startsWith("Not")) {
			fbf.setOperator(operators.get("Not"));

			for (int i = 0; i < notRelacao.size(); ++i) {
				if (mainOperator.equals(notRelacao.get(i).getLeft())) {
					Relacao r = notRelacao.get(i);
					fbf.setLeft("");
					fbf.setRight((r.getRight().startsWith("Atom")) ? r.getRight()
							: montaFBF(r.getRight(), operators, notRelacao, leftRelacao, rightRelacao));
				}
			}
		} else if (!mainOperator.startsWith("Not") && !mainOperator.startsWith("Atom")) {

			if (mainOperator.startsWith("And")) {
				fbf.setOperator(operators.get("And"));
			} else if (mainOperator.startsWith("Or")) {
				fbf.setOperator(operators.get("Or"));
			} else if (mainOperator.startsWith("Imply")) {
				fbf.setOperator(operators.get("Imply"));
			} else if (mainOperator.startsWith("BiImply")) {
				fbf.setOperator(operators.get("BiImply"));
			}
			for (int i = 0; i < leftRelacao.size(); ++i) {
				if (mainOperator.equals(leftRelacao.get(i).getLeft())) {
					Relacao r = leftRelacao.get(i);
					fbf.setLeft((r.getRight().startsWith("Atom")) ? r.getRight()
							: montaFBF(r.getRight(), operators, notRelacao, leftRelacao, rightRelacao));
				}
			}

			for (int i = 0; i < rightRelacao.size(); ++i) {
				if (mainOperator.equals(rightRelacao.get(i).getLeft())) {
					Relacao r = rightRelacao.get(i);
					fbf.setRight((r.getRight().startsWith("Atom")) ? r.getRight()
							: montaFBF(r.getRight(), operators, notRelacao, leftRelacao, rightRelacao));
				}
			}

		}
		return fbf;
	}

	// public String resgatarRegras(String instancia){
	// 	String argRegras = "";

	// 	String[] retorno = instancia.split("\n");

	// 	for (String string : retorno) {
	// 		if (string.startsWith("this/Rule=")){
	// 			argRegras = string;
	// 		System.out.println("----> A do this/Rule: "+argRegras);

	// 		}

	// 		argRegras = argRegras.replace("this/Rule={", "");
	// 		argRegras = argRegras.replace("}", "");
	// 		argRegras = argRegras.replace("$", "");
	// 		// argRegras = argRegras.replaceAll(argRegras, "");

	// 	} 

	// 	String regrasLista[] = argRegras.split(", ") ;
		
	// 	ArrayList<String> regsLista = new ArrayList<String>();
	// 	for (String reg : regrasLista){
	// 		if(!reg.contains("$")){
	// 			regsLista.add(reg);
	// 		}
	// 	}
		
	// 	String saidaJson = String.join(", ", regsLista);
 
	// 	System.out.println("As do json: "+ saidaJson);
	// 	return saidaJson;
	// }
	// "this/Rule={NE$0, MP$0}"

	public String resgatarRegras(String instancia){
		// USANDO REGEX PARA TRABALHAR COM AS REGRAS
		// System.out.println("\n\t----------REGEX-------------");

		// TRATANDO A INFORMAÇÃO PEGADA DO ALLOY
		String argRegras = "";
		String[] retorno = instancia.split("\n");

		for (String string : retorno) {
			if (string.startsWith("this/Rule=")){
				argRegras = string;
				// System.out.println("----> A do this/Rule: "+argRegras);
			}
			argRegras = argRegras.replace("$", "");

		} 

		// INICIO DO TRATAMENTO DA STRING VINDO DO ALLOY E SEPARANDO REGRAS

		String regex = "0";  							// PADRÃO A ENCONTRAR
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(argRegras);
		
		String regra = "";								// OBTERA A REGRA PARA ADICIONAR POSTERIORMENTE
		ArrayList<String> regsLista = new ArrayList<String>();

		while (matcher.find()){
			
            // System.out.print(matcher.start() + " ");	// OBTEM O INDICE DO PADRÃO
            // System.out.println(argRegras.substring((matcher.start()-2), matcher.start()));
            regra = argRegras.substring((matcher.start()-2), matcher.start());
            regsLista.add(regra);
        }
		
		String saidaJson = String.join(", ", regsLista);
 
		// System.out.println("As do json: "+ saidaJson);
		// System.out.println("\t----------Fim Regex---------\n");
		return saidaJson;
	}
}
