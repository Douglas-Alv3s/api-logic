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
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufal.logic.DAO.DAOForm_Argumento;
import br.edu.ufal.logic.DAO.DAOForm_FBF;
import br.edu.ufal.logic.DAO.DAOGuarda;
import br.edu.ufal.logic.DAO.DAOUsuario;
import br.edu.ufal.logic.DAO.dataSource.CriacaoBD;
import br.edu.ufal.logic.DAO.dataSource.MySQLDataSource;
import br.edu.ufal.logic.model.Form_Argumento;
import br.edu.ufal.logic.model.Form_FBF;
import br.edu.ufal.logic.model.Guarda;
import br.edu.ufal.logic.model.Usuario;
import br.edu.ufal.logic.util.InstanciaRetorno;
import br.edu.ufal.logic.util.Util;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
// import edu.mit.csail.sdg.alloy4compiler.ast.Command;
// import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
// import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
// import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
// import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
// import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;

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
	private ArrayList<FBFDTO> findAll() throws IOException {	//Cria uma ArrayList vazia 

		ArrayList<FBFDTO> fbfs = new ArrayList<>();

		return fbfs;
	}


	//http://127.0.0.1:8080/api-logic/fbfs/{atomosMin}/{atomosMax}/{quantidadeFbfs}/{listasExercicios}/{operadoresLista}/{todosOuAoMenosUm}
	//localhost:8080/api-logic/fbfs/3/5/5/1/And,Or,Not,Imply/1
	@GetMapping("/{atomosMin}/{atomosMax}/{quantidadeFbfs}/{listasExercicios}/{operadoresLista}/{todosOuAoMenosUm}/{metodo}/{idUsuarioLogado}") //Endereço de FBFS na Web
	private ArrayList<FBFDTO> findFbfs(@PathVariable String atomosMin, @PathVariable String atomosMax,
			@PathVariable String quantidadeFbfs,@PathVariable String todosOuAoMenosUm,
			@PathVariable String operadoresLista, @PathVariable String listasExercicios,
			@PathVariable String metodo, @PathVariable String idUsuarioLogado) 
			throws IOException, Err {
		
		String URL_FBF = "/"+atomosMin+"/"+atomosMax+"/"+operadoresLista+"/"+todosOuAoMenosUm;
		// String URL_FBF = "/"+atomosMin+"/"+atomosMax+"/"+operadoresLista+"/"+todosOuAoMenosUm;
		System.out.println(URL_FBF);

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

		}

		
		// Dando inicio ao Banco de dados
		CriacaoBD.getInstance();

		int cont = 0;
		int valueRun = 4;

		ArrayList<FBFDTO> fbfs = new ArrayList<>();
		ArrayList<String> fbfsTeste = new ArrayList<>();

		Util util = new Util();
		
		// Quantidade de formulas requeridas por cada método
		Integer formulasRequeridas = (Integer.parseInt(quantidadeFbfs) * Integer.parseInt(listasExercicios));
		Integer totalFormulas;
		if(metodo.equals("1")){ // Estrategia com base no usuario
			int ultimoID = 0;
			try {
				DAOGuarda daoGuarda = new DAOGuarda(MySQLDataSource.getInstance(), "FBF");
				ultimoID = daoGuarda.consultarRegistro(idUsuarioLogado, URL_FBF);
				System.out.println("COntagem -> "+ ultimoID);
			} catch (Exception e) {
				System.out.println("");
			}
			
			totalFormulas = formulasRequeridas + ultimoID;
			System.out.println("--------> Total pela função: "+ totalFormulas);
		}else if(metodo.equals("2")){ // Estrategia modo global
			int ultimoID = 0;
			try {
				DAOForm_FBF daoForm_FBF = new DAOForm_FBF(MySQLDataSource.getInstance());
				// ultimoID = daoForm_FBF.resgatarUltimoID();
				ultimoID = daoForm_FBF.contarFBF(URL_FBF);
			} catch (Exception e) {
				System.out.println("");
			}
			System.out.println("--------> Id obtido pela função: "+ ultimoID);

			totalFormulas = formulasRequeridas + ultimoID;
		}else if(metodo.equals("3")){ // Geração de mais formulas para a garantir a aleatoriedade
			totalFormulas = formulasRequeridas*10;
		}else { // Trás as formulas total requeridas pelo usuario
			totalFormulas = formulasRequeridas;
		}
		
		
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


						if(metodo.equals("1")){
							String fbfString = fbf.toString(); // Pegando a formula gerada
							Form_FBF form_FBF = new Form_FBF(fbfString,  URL_FBF);
							// System.out.println("Objeto para o banco de dados "+form_FBF.toString());

							try{
								DAOUsuario daoUsuario = new DAOUsuario(MySQLDataSource.getInstance());
								DAOForm_FBF daoForm_FBF = new DAOForm_FBF(MySQLDataSource.getInstance());
								DAOGuarda daoGuarda = new DAOGuarda(MySQLDataSource.getInstance(), "FBF");

								Usuario usuario = daoUsuario.consultarID(idUsuarioLogado);
								Guarda guarda = new Guarda(usuario, form_FBF, null, totalFormulas);
								System.out.println(guarda.toString());
								
								daoForm_FBF.adicionar(form_FBF);
								daoGuarda.realizarRegistro(guarda);
								
							}catch(Exception e){
								System.out.println("Não esta sendo adicionado");
							}
						}else if(metodo.equals("2")){
							String stringFBF = fbf.toString();
							Form_FBF form_FBF = new Form_FBF(stringFBF, URL_FBF);
							// System.out.println("Objeto para o banco de dados "+form_FBF.toString());
							try{
								DAOForm_FBF daoForm_FBF = new DAOForm_FBF(MySQLDataSource.getInstance());
								daoForm_FBF.adicionar(form_FBF);
							}catch(Exception e){
								System.out.println("Não esta sendo adicionado");
							}
						}
					
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

		if(metodo.equals("1") || metodo.equals("2")){	// Trazendo novas formulas/ Usando método com banco de dados
			ArrayList<FBFDTO> ultimasFBFs = new ArrayList<>(fbfs.subList(fbfs.size() - formulasRequeridas, fbfs.size()));
			return ultimasFBFs;
		}else if(metodo.equals("3")){ //Geração de formulas aleatorias/ Sem usar banco de dados
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
			return fbfsAleatorio;
		}else{ // Retorna todas as formulas geradas/ Sem usar banco de dados
			return fbfs;
		}
		



		
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
