package br.edu.ufal.logic.argument;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufal.logic.DAO.dataSource.CriacaoBD;
import br.edu.ufal.logic.fbf.FBF;
import br.edu.ufal.logic.util.InstanciaRetorno;
import br.edu.ufal.logic.util.Relacao;
import br.edu.ufal.logic.util.Util;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

@RestController
@RequestMapping("/arguments")
public class ArgumentController {

	@Autowired
	private ArgumentService service;
//	private boolean executando = false;


	// Pega um arquivo txt do Alloy
	private String lerTxt() {	
		Path caminho = Paths.get("AlloyTexto.txt");
        byte[] texto;
		try {
			texto = Files.readAllBytes(caminho);
			String textoAlloy = new String(texto);
			return textoAlloy;
		} catch (IOException e) {
			}
		return "0";
	}


	private String modelArgument = lerTxt();
	

	@GetMapping
	public ArrayList<ArgumentDTO> findAll() throws IOException {
		ArrayList<ArgumentDTO> argumentos = new ArrayList<>();
		
	return argumentos;
	}



	// @GetMapping("/{regras}/{Limitador}/{quantidade}/{listas}")
	// Limitador tera apenas 3 opções [1 ou 2 ou 3 ou 4 ou 5]

	@GetMapping("/{quantidade}/{listas}/{regras}/{limitador}")  // Endereço para acessar na url e parametros a receber
	public ArrayList<ArgumentDTO> findArguments(@PathVariable String regras,
			@PathVariable String quantidade, @PathVariable String listas, @PathVariable String limitador) throws IOException, Err {
		
		System.out.println(regras);
		// System.out.println(atomos);
		System.out.println(quantidade);
		System.out.println(listas);
		


		String[] regrasSplit = regras.split(",");
		ArrayList<String> regs = new ArrayList<>();
		for(String s: regrasSplit) {
			regs.add(s);
		}


		// Define o valor das operações
		String operacoes = "";

		// >>>>>>>>>>>>>>>>> Limitador da quantidade de REGRAS <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
		if(limitador.equals("1") || limitador.equals("2") || limitador.equals("3")){
			operacoes = "\n#NE=0 \n#NI=0 \n#CI=0 \n#CE=0 \n#DI=0 \n#DE=0 \n#BE=0 \n#BI=0 \n#MP=0 \n#MT=0 \n#SD=0\n"; 
			// Operaçoes nessas opção todas as regras ja são definidas.
		
			if(regs.contains("NE")) {
				operacoes = operacoes.replace("#NE=0","#NE > 0");
			}
			if(regs.contains("NI")) { // ---------------------- Ta conseguindo executar apenas uma lista só a partir de 7 tentativas
				operacoes = operacoes.replace("#NI=0","#NI > 0");
			}
			if(regs.contains("CI")) {
				operacoes = operacoes.replace("#CI=0","#CI > 0");
			}
			if(regs.contains("CE")) {
				operacoes = operacoes.replace("#CE=0","#CE > 0");
			}
			if(regs.contains("DI")) {
				operacoes = operacoes.replace("#DI=0","#DI > 0");
			}
			if(regs.contains("DE")) { // ----------------------Ta com Loop
				operacoes = operacoes.replace("#DE=0","#DE > 0");
			}
			if(regs.contains("BE")) { // ---------------------- Ta com Loop
				operacoes = operacoes.replace("#BE=0", "#BE > 0");
			}
			if(regs.contains("BI")) { // ---------------------- Ta com Loop e dificuldade na geração
				operacoes = operacoes.replace("#BI=0","#BI > 0");
			}
			if(regs.contains("MP")) {
				operacoes = operacoes.replace("#MP=0","#MP > 0");
			}
			if(regs.contains("MT")) {
				operacoes = operacoes.replace("#MT=0","#MT > 0");
			}
			if(regs.contains("SD")) {
				operacoes = operacoes.replace("#SD=0", "#SD > 0");
			}
		}
		
		// // A verificação das regras que serão passadas para o argumento, são definidas aqui em baixo.
		if(limitador.equals("4")){
			operacoes = "";
			if(regs.contains("NE")) {
				operacoes += "#NE > 0\n";
			}
			if(regs.contains("NI")) {
				operacoes += "#NI > 0\n";
			}
			if(regs.contains("CI")) {
				operacoes += "#CI > 0\n";
			}
			if(regs.contains("CE")) {
				operacoes += "#CE > 0\n";
			}
			if(regs.contains("DI")) {
				operacoes += "#DI > 0\n";
			}
			if(regs.contains("DE")) {
				operacoes += "#DE > 0\n";
			}
			if(regs.contains("BE")) {
				operacoes += "#BE > 0\n";
			}
			if(regs.contains("BI")) {
				operacoes += "#BI > 0\n";
			}
			if(regs.contains("MP")) {
				operacoes += "#MP > 0\n";
			}
			if(regs.contains("MT")) {
				operacoes += "#MT > 0\n";
			}
			if(regs.contains("SD")) {
				operacoes += "#SD > 0\n";
			}
		}
		
		// if(limitador.equals("5")){
		// 	operacoes = "\n\n#NE = 0 \n\n#NI = 0 \n\n#CI = 0 \n\n#CE = 0 \n\n#DI = 0 \n\n#DE = 0 \n\n#BE = 0 \n\n#BI = 0 \n\n#MP = 0 \n\n#MT = 0 \n\n#SD = 0\n\n";
		// 	// if(regs.contains("NE")) {
		// 	// 	operacoes += "#NE >= 0\n";
		// 	// }
		// 	// if(regs.contains("NI")) {
		// 	// 	operacoes += "#NI >= 0\n";
		// 	// }
		// 	// if(regs.contains("CI")) {
		// 	// 	operacoes += "#CI >= 0\n";
		// 	// }
		// 	// if(regs.contains("CE")) {
		// 	// 	operacoes += "#CE >= 0\n";
		// 	// }
		// 	// if(regs.contains("DI")) {
		// 	// 	operacoes += "#DI >= 0\n";
		// 	// }
		// 	// if(regs.contains("DE")) {
		// 	// 	operacoes += "#DE >= 0\n";
		// 	// }
		// 	// if(regs.contains("BE")) {
		// 	// 	operacoes += "#BE >= 0\n";
		// 	// }
		// 	// if(regs.contains("BI")) {
		// 	// 	operacoes += "#BI >= 0\n";
		// 	// }
		// 	// if(regs.contains("MP")) {
		// 	// 	operacoes += "#MP >= 0\n";
		// 	// }
		// 	// if(regs.contains("MT")) {
		// 	// 	operacoes += "#MT >= 0\n";
		// 	// }
		// 	// if(regs.contains("SD")) {
		// 	// 	operacoes += "#SD >= 0\n";
		// 	// }
		// 	if(regs.contains("NE")) {
		// 		operacoes = operacoes.replace("#NE = 0", "#NE >= 0");
		// 	}
		// 	if(regs.contains("NI")) {
		// 		operacoes = operacoes.replace("#NI = 0", "#NI >= 0");
		// 	}
		// 	if(regs.contains("CI")) {
		// 		operacoes = operacoes.replace("#CI = 0", "#CI >= 0");
		// 	}
		// 	if(regs.contains("CE")) {
		// 		operacoes = operacoes.replace("#CE = 0", "#CE >= 0");
		// 	}
		// 	if(regs.contains("DI")) {
		// 		operacoes = operacoes.replace("#DI = 0", "#DI >= 0");
		// 	}
		// 	if(regs.contains("DE")) {
		// 		operacoes = operacoes.replace("#DE = 0", "#DE >= 0");
		// 	}
		// 	if(regs.contains("BE")) {
		// 		operacoes = operacoes.replace("#BE = 0", "#BE >= 0");
		// 	}
		// 	if(regs.contains("BI")) {
		// 		operacoes = operacoes.replace("#BI = 0", "#BI >= 0");
		// 	}
		// 	if(regs.contains("MP")) {
		// 		operacoes = operacoes.replace("#MP = 0", "#MP >= 0");
		// 	}
		// 	if(regs.contains("MT")) {
		// 		operacoes = operacoes.replace("#MT = 0", "#MT >= 0");
		// 	}
		// 	if(regs.contains("SD")) {
		// 		operacoes = operacoes.replace("#SD = 0", "#SD >= 0");
		// 	}
		// }

		// Dando inicio ao Banco de dados
		CriacaoBD.getInstance();

		Util util = new Util();
		ArrayList<ArgumentDTO> argumentos = new ArrayList<>();
		ArrayList<String> argumentTeste = new ArrayList<>();
		
		A4Reporter rep = new A4Reporter();
		String[] alfabeto = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "I", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };

		int cont = 0;
		int valueRun = 4;
		
		Integer totalFormulas = (Integer.parseInt(quantidade) * Integer.parseInt(listas) *5);
		while(cont < totalFormulas) {
			

			System.out.println("Operações usadas: "+operacoes);
			

			// Random regRandom = new Random();
			// int quantidadeRegras = Integer.parseInt(limitador);
			
			// if(limitador.equals("1") || limitador.equals("2") || limitador.equals("3")){
			// 	operacoes = "\n#NE=0 \n#NI=0 \n#CI=0 \n#CE=0 \n#DI=0 \n#DE=0 \n#BE=0 \n#BI=0 \n#MP=0 \n#MT=0 \n#SD=0\n"; 

			// 	// Operaçoes nessas opção todas as regras ja são definidas.
			// 	if(limitador.equals("1")){
					
			// 		int regraAleatorio = regRandom.nextInt(regs.size());
			// 		System.out.println("\n\nREGRA CAINDO NESSA MIZERAAAAAAAAAAAAAAAAAAAAAAAA---"+regs.get(regraAleatorio)+" ----AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\n");
			// 		if(regs.get(regraAleatorio).equals("NE")) {
			// 			operacoes = operacoes.replace("#NE=0","#NE > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("NI")) {
			// 			operacoes = operacoes.replace("#NI=0","#NI > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("CI")) {
			// 			operacoes = operacoes.replace("#CI=0","#CI > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("CE")) {
			// 			operacoes = operacoes.replace("#CE=0","#CE > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("DI")) {
			// 			operacoes = operacoes.replace("#DI=0","#DI > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("DE")) {
			// 			operacoes = operacoes.replace("#DE=0","#DE > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("BE")) {
			// 			operacoes = operacoes.replace("#BE=0", "#BE > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("BI")) {
			// 			operacoes = operacoes.replace("#BI=0","#BI > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("MP")) {
			// 			operacoes = operacoes.replace("#MP=0","#MP > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("MT")) {
			// 			operacoes = operacoes.replace("#MT=0","#MT > 0");
			// 		}
			// 		if(regs.get(regraAleatorio).equals("SD")) {
			// 			operacoes = operacoes.replace("#SD=0", "#SD > 0");
			// 		}
			// 	}else if(limitador.equals("2")){
			// 		for(int i = 0; i <= quantidadeRegras; i++){
			// 			int regraAleatorio = regRandom.nextInt(regs.size());
			// 			if(regs.get(regraAleatorio).equals("NE")) {
			// 				operacoes = operacoes.replace("#NE=0","#NE > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("NI")) {
			// 				operacoes = operacoes.replace("#NI=0","#NI > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("CI")) {
			// 				operacoes = operacoes.replace("#CI=0","#CI > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("CE")) {
			// 				operacoes = operacoes.replace("#CE=0","#CE > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("DI")) {
			// 				operacoes = operacoes.replace("#DI=0","#DI > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("DE")) {
			// 				operacoes = operacoes.replace("#DE=0","#DE > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("BE")) {
			// 				operacoes = operacoes.replace("#BE=0", "#BE > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("BI")) {
			// 				operacoes = operacoes.replace("#BI=0","#BI > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("MP")) {
			// 				operacoes = operacoes.replace("#MP=0","#MP > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("MT")) {
			// 				operacoes = operacoes.replace("#MT=0","#MT > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("SD")) {
			// 				operacoes = operacoes.replace("#SD=0", "#SD > 0");
			// 			}
			// 		}
			// 	}else if(limitador.equals("3")){
			// 		for(int i = 0; i < quantidadeRegras; i++){
			// 			int regraAleatorio = regRandom.nextInt(regs.size());
			// 			if(regs.get(regraAleatorio).equals("NE")) {
			// 				operacoes = operacoes.replace("#NE=0","#NE > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("NI")) {
			// 				operacoes = operacoes.replace("#NI=0","#NI > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("CI")) {
			// 				operacoes = operacoes.replace("#CI=0","#CI > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("CE")) {
			// 				operacoes = operacoes.replace("#CE=0","#CE > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("DI")) {
			// 				operacoes = operacoes.replace("#DI=0","#DI > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("DE")) {
			// 				operacoes = operacoes.replace("#DE=0","#DE > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("BE")) {
			// 				operacoes = operacoes.replace("#BE=0", "#BE > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("BI")) {
			// 				operacoes = operacoes.replace("#BI=0","#BI > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("MP")) {
			// 				operacoes = operacoes.replace("#MP=0","#MP > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("MT")) {
			// 				operacoes = operacoes.replace("#MT=0","#MT > 0");
			// 			}
			// 			if(regs.get(regraAleatorio).equals("SD")) {
			// 				operacoes = operacoes.replace("#SD=0", "#SD > 0");
			// 			}
			// 		}
			// 	}
				
			// }

			valueRun += 1;
		
			// Aqui é repassado as especificações que o alloy ira receber
			String config = "pred ConfigArgument(){ \n" 
					+ " #Atom>1"+"\n" 
					//+ "	#MT!=0 => #MP=0	#MP!=0 => #MT=0\n" 
					+operacoes
				    + "	one ru,ru':Rule | ru.R in ru'.(P1+P2+p3)\n"
					+ "	one arg:Argument | all ru:Rule | ru.(P1+P2+p3) in arg.premisse\n"
					+ "	one arg:Argument | all ru:Rule | no fo:Formula | fo in ru.(P1+P2+p3) and fo in ru.R and fo in arg.conclusion\n"
					+ "	one arg:Argument | some ru:Rule | arg.conclusion=ru.R\n" + "}\n" + "\n" + "run ConfigArgument for "+valueRun+"\n"
					+ "";
			System.out.println(config);
			
			//if(valueRun > (Integer.parseInt(quantidade) * Integer.parseInt(listas))){
			//	break;
			//}

			String model = modelArgument + config;
			
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			System.out.println(model);
			System.out.println(timestamp.getTime());
			File tmpAls = File.createTempFile("alloyArgument"+timestamp.getTime(), ".als");
			tmpAls.deleteOnExit();
			flushModelToFile(tmpAls, model);
			{
				CompModule world = CompUtil.parseEverything_fromFile(rep, null, tmpAls.getAbsolutePath());
				A4Options opt = new A4Options();
				opt.originalFilename = tmpAls.getAbsolutePath();
				opt.solver = A4Options.SatSolver.SAT4J;
				Command cmd = world.getAllCommands().get(0);
				ConstList<Sig> cTEMP = world.getAllReachableSigs();
				A4Solution sol = TranslateAlloyToKodkod.execute_commandFromBook(rep, cTEMP, cmd, opt);

				while (sol.satisfiable()) {
					Argument arg = new Argument();
						
					InstanciaRetorno ir = util.montarInstancia(sol.toString());
					
					// Local que as regras estão sendo resgatas
					String regrinha = util.resgatarRegras(sol.toString());
					
					
					ArrayList<String> mainOperatorPremisses = new ArrayList<String>();

					ArrayList<Relacao> argumentRelacao = ir.getArgumentRelacao();

					for (Relacao relacao : argumentRelacao) {
						mainOperatorPremisses.add(relacao.getRight());
					}

					for (String mainOperator : mainOperatorPremisses) {
						FBF premisse = util.montaFBF(mainOperator, ir.getOperators(), ir.getNotRelacao(),
								ir.getLeftRelacao(), ir.getRightRelacao());
						arg.addPremisse(util.fillWithAtoms(premisse, alfabeto));
					}

					FBF conclusion = util.montaFBF(ir.getConclusion().getRight(), ir.getOperators(), ir.getNotRelacao(),
							ir.getLeftRelacao(), ir.getRightRelacao());
					arg.setConclusion(util.fillWithAtoms(conclusion, alfabeto));
					
					if(!argumentTeste.contains(arg.toString())){
						argumentos.add(service.argumentToArgumentDTO(cont, arg, regrinha));
						System.out.println("FORMULA ENTRANDO "+argumentos.get(cont));
						System.out.println(argumentos.size());
						argumentTeste.add(arg.toString());
						System.out.println(argumentos.size());
						cont += 1;
					}else {
						System.out.println("quant: "+arg);
					}
					sol = sol.next();
					
					if (cont == totalFormulas) {
						break;
					}

				}
 
			}
			tmpAls.delete();
		}

		ArrayList<ArgumentDTO> argumentosAleatorio = new ArrayList<>();
		int conter = 0;
		Random random = new Random();
		while(conter < (Integer.parseInt(quantidade) * Integer.parseInt(listas))) {
			int indiceAleatorio = random.nextInt(totalFormulas);
			System.out.println("------\nEssa Aqui é da lista\n"+argumentos.get(indiceAleatorio).toString());
			
			if(!argumentosAleatorio.contains(argumentos.get(indiceAleatorio))){
				argumentosAleatorio.add(argumentos.get(indiceAleatorio));
				conter++;
			}
		}

		return argumentosAleatorio;

		// return argumentos;

	}

	private static void flushModelToFile(File tmpAls, String model) throws IOException {
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(tmpAls));
			bos.write(model.getBytes());
			bos.flush();
		} finally {
			if (bos != null)
				bos.close();
		}
	}
}
