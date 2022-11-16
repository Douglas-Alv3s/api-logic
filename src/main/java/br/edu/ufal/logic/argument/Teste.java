package br.edu.ufal.logic.argument;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Teste {

   

    public void lerTexto() throws IOException{
        
    }
    public static void main(String[] args) throws IOException {
        Path caminho = Paths.get("C:/Users/Douglas/Desktop/api-logic-main/AlloyTexto.txt");
        byte[] texto = Files.readAllBytes(caminho);
        String textoAlloy = new String(texto);


        System.out.println(textoAlloy);
    }

    
}
