package br.edu.ufal.logic.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufal.logic.model.Usuario;

@RestController
@RequestMapping("/usuario/cadastro")
public class CadastroController {

    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("{email}/{nome}/{senha}")
    public ResponseEntity<String> cadastroUsuario(@PathVariable String email, @PathVariable String nome, @PathVariable String senha){
        
        // Verifique se o email já está em uso antes de criar o usuário
        if (usuarioService.verificarEmailExistente(email) != null) {
            return ResponseEntity.badRequest().body("O email já está em uso.");
        }
        Usuario novoUsuario = new Usuario(email, nome, senha);

        // Crie o usuário
        usuarioService.criarNovoUsuario(novoUsuario);

        return ResponseEntity.ok("Usuário registrado com sucesso.");
    
    }
}
