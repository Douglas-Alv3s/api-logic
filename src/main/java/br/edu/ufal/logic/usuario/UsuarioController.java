package br.edu.ufal.logic.usuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufal.logic.model.Usuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    private UsuarioService usuarioService;

    @GetMapping("cadastro/{email}/{nome}/{senha}")
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

    @GetMapping("login/{email}/{senha}")
    public ResponseEntity<String> loginUsuario(@PathVariable String email, @PathVariable String senha){
            // Verificar se o usuário existe no banco de dados
        Usuario usuario = usuarioService.verificarEmailExistente(email);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado.");
        }

        // Verificar se a senha está correta
        if (!usuario.getSenha().equals(senha)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta.");
        }

        // Realizar a autenticação do usuário (por exemplo, gerar um token de autenticação)

        // Retornar a resposta de sucesso com o token de autenticação
        return ResponseEntity.ok("Login bem-sucedido:" + usuario.toString());
    }
}
