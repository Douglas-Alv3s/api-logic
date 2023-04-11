package br.edu.ufal.logic.fbf;

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

import br.edu.ufal.logic.util.InstanciaRetorno;
import br.edu.ufal.logic.util.Util;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

@RestController
@RequestMapping("/fbfs")
public class FBFController {

	@Autowired
	private FBFService service;

	// Pega a especificação de um arquivo txt
	private String lerTxt() {
		Path caminho = Paths.get("FbfsTexto.txt");
        byte[] texto;
		try {
			texto = Files.readAllBytes(caminho);
			String textoAlloy = new String(texto);
			return textoAlloy;
		} catch (IOException e) {}
		return "0"; 
	}

	private String modelFormulaTeste = lerTxt();

	@GetMapping
	public ArrayList<FBFDTO> findAll() throws IOException {	//Cria uma ArrayList vazia 

		ArrayList<FBFDTO> fbfs = new ArrayList<>();

		return fbfs;
	}


	//http://127.0.0.1:8080/api-logic/fbfs/{atomosMin}/{atomosMax}/{quantidadeFbfs}/{listasExercicios}/{operadoresLista}/{todosOuAoMenosUm}
	@GetMapping("/{atomosMin}/{atomosMax}/{quantidadeFbfs}/{listasExercicios}/{operadoresLista}/{todosOuAoMenosUm}") //Endereço de FBFS na Web
	private ArrayList<FBFDTO> findFbfs(@PathVariable String atomosMin, @PathVariable String atomosMax,
			@PathVariable String quantidadeFbfs,@PathVariable String todosOuAoMenosUm,
			@PathVariable String operadoresLista, @PathVariable String listasExercicios) 
			throws IOException, Err {
		
		String quantAtomos = "";

		
		int atoMin = Integer.parseInt(atomosMin); // Transformação de String para int
		int atoMax = Integer.parseInt(atomosMax); // Transformação de String para int

		if (atoMin < atoMax) { // Condição do atomo Maximo for maior que atomo minimo
			quantAtomos = "#Atom >= " + atomosMin + " && #Atom <= " + atomosMax;
		} else {
			quantAtomos = "#Atom = " + atomosMax;
		}

		String config = "";


		String[] operadores = operadoresLista.split(","); //Construção da lista com os operadores
		
		ArrayList<String> oprs = new ArrayList<>(); // Criação de outra array que recebera os itens contido na lista de operadores
		for (String s : operadores) {
			oprs.add(s);
			
		}

		System.out.println("Todos ou pelo menos " + todosOuAoMenosUm);

		String operacoes = "\n#And=0 \n#Or=0 \n#BiImply=0 \n#Imply=0 \n#Not=0";
		String aoMenosUmaOperacao = ""; //#And+#Or+BiImply+#Imply+#Not
		if(todosOuAoMenosUm.equals("1")) {	// Cria a formula contendo as operações selecionadas
			if (oprs.contains("And")) {
				operacoes = operacoes.replace("#And=0", "#And>0");
			}
			if (oprs.contains("Or")) {
				operacoes = operacoes.replace("#Or=0", "#Or>0");
			}
			if (oprs.contains("BiImply")) {
				operacoes = operacoes.replace("#BiImply=0", "#BiImply>0");
			}
			if (oprs.contains("Imply")) {
				operacoes = operacoes.replace("#Imply=0", "#Imply>0");
			}
			if (oprs.contains("Not")) {
				operacoes = operacoes.replace("#Not=0", "#Not>0");
			}
		}else if(todosOuAoMenosUm.equals("2")) {  // Cria a formula contendo a menos uma das operações selecionadas
			aoMenosUmaOperacao = operadoresLista;
			if (oprs.contains("And")) {
				operacoes = operacoes.replace("#And=0", "#And>=0");
			}
			if (oprs.contains("Or")) {
				operacoes = operacoes.replace("#Or=0", "#Or>=0");
			}
			if (oprs.contains("BiImply")) {
				operacoes = operacoes.replace("#BiImply=0", "#BiImply>=0");
			}
			if (oprs.contains("Imply")) {
				operacoes = operacoes.replace("#Imply=0", "#Imply>=0");
			}
			if (oprs.contains("Not")) {
				operacoes = operacoes.replace("#Not=0", "#Not>=0");
			}
			// Trata a String de operações selecionadas
			// aoMenosUmaOperacao = "#" + aoMenosUmaOperacao;
			// aoMenosUmaOperacao = aoMenosUmaOperacao.replaceAll(",", "+#");
			// aoMenosUmaOperacao = aoMenosUmaOperacao + ">0";
		}
		// operacoes = operacoes + "\n" + aoMenosUmaOperacao;
		// operacoes = operacoes + "\n" + aoMenosUmaOperacao;
		
		int cont = 0;
		int valueRun = 4;

		ArrayList<FBFDTO> fbfs = new ArrayList<>();
		ArrayList<String> fbfsTeste = new ArrayList<>();

		Util util = new Util();
		Integer totalFormulas = (Integer.parseInt(quantidadeFbfs) * Integer.parseInt(listasExercicios)*10);
		
		while (cont < (totalFormulas)) {
			valueRun += 1;

			config = "pred ConfigFormula(){ \n" + operacoes + "\n"
					+ "	" + quantAtomos + "\n" + "	\n" + "}\n" + "run ConfigFormula for " + valueRun;

			System.out.println(config);

			A4Reporter rep = new A4Reporter();
			String[] alfabeto = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "I", "M", "N", "O", "P", "Q",
					"R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

			String model = modelFormulaTeste + config;

			Timestamp timestamp = new Timestamp(System.currentTimeMillis());

			File tmpAls = File.createTempFile("alloyFormula" + timestamp.getTime(), ".als");
			tmpAls.deleteOnExit();
			flushModelToFile(tmpAls, model);
			{
				CompModule world = CompUtil.parseEverything_fromFile(rep, null, tmpAls.getAbsolutePath());
				A4Options opt = new A4Options();
				opt.originalFilename = tmpAls.getAbsolutePath();
				opt.solver = A4Options.SatSolver.SAT4J;
				Command cmd = world.getAllCommands().get(0);

				A4Solution sol = TranslateAlloyToKodkod.execute_commandFromBook(rep, world.getAllReachableSigs(), cmd,
						opt);

				while (sol.satisfiable()) {
					InstanciaRetorno ir = util.montarInstancia(sol.toString());
					FBF fbf = util.montaFBF(ir.getMainOperator(), ir.getOperators(), ir.getNotRelacao(),
							ir.getLeftRelacao(), ir.getRightRelacao());

					fbf = util.fillWithAtoms(fbf, alfabeto);

					if (!fbfsTeste.contains(fbf.toString())) {
						fbfs.add(service.FBFToFBFDTO(fbf, cont));
						fbfsTeste.add(fbf.toString());
						cont += 1;
					} else {
						System.out.println(fbf);
					}
					sol = sol.next();
					if (cont == (totalFormulas)) {
						break;
					}
				}
			}
			tmpAls.delete();
		}

		ArrayList<FBFDTO> fbfsAleatorio = new ArrayList<>();
		int conter = 0;
		Random random = new Random();
		while(conter < (Integer.parseInt(quantidadeFbfs) * Integer.parseInt(listasExercicios))) {
			int indiceAleatorio = random.nextInt(totalFormulas);
			System.out.println("------\nEssa Aqui é da lista\n"+fbfs.get(indiceAleatorio).toString());
			
			if(!fbfsAleatorio.contains(fbfs.get(indiceAleatorio))){
				fbfsAleatorio.add(fbfs.get(indiceAleatorio));
				conter++;
			}
		}
		
		// return fbfs;
		return fbfsAleatorio;
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
