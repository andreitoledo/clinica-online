package com.andreitoledo.clinica.online.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.andreitoledo.clinica.online.domain.Medico;
import com.andreitoledo.clinica.online.domain.Perfil;
import com.andreitoledo.clinica.online.domain.PerfilTipo;
import com.andreitoledo.clinica.online.domain.Usuario;
import com.andreitoledo.clinica.online.service.MedicoService;
import com.andreitoledo.clinica.online.service.UsuarioService;



@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private MedicoService medicoService;
	
	// Abrir cadatro de usuario (medico/admin/paciente)
	@GetMapping("/novo/cadastro/usuario")
	public String CadastroPorAdminParaAdminMedicoPaciente(Usuario usurio) {
		
		return "usuario/cadastro";
	}
	
    // abrir lista de usuarios
    @GetMapping("/lista")
    public String listarUsuarios() {

        return "usuario/lista";
    }
    
    // listar usuarios na datatables
    @GetMapping("/datatables/server/usuarios")
    public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {

        return ResponseEntity.ok(service.buscarTodos(request));
    } 
    
 // salvar cadastro de usuarios por administrador
    @PostMapping("/cadastro/salvar")
    public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
    	List<Perfil> perfis = usuario.getPerfis();
    	if (perfis.size() > 2 || 
    			perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) ||
    			perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))) {
    		attr.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico.");
    		attr.addFlashAttribute("usuario", usuario);
    	} else {
    		try {
    			service.salvarUsuario(usuario); 
    			attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
    		} catch (DataIntegrityViolationException ex) {
    			attr.addFlashAttribute("falha", "Cadastro não realizado, email já existente.");
			}
    	}
    	return "redirect:/u/novo/cadastro/usuario";
    }
    
	/* pré edicao de credenciais de usuarios. Ao clicar no botão, a requisição cai
	 * aqui nesse método, que pega o id e faz a consulta pelo usuario em questão e 
	 * evia como resposta esse objeto usuario para a página de cadastro.	 * 
	 */

    @GetMapping("/editar/credenciais/usuario/{id}")
    public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {

        return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));
    }
    
    
    @GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
    public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId, 
    												   @PathVariable("perfis") Long[] perfisId) {
    	
    	Usuario us = service.buscarPorIdEPerfis(usuarioId, perfisId);
    	
    	// If para direcionar cada perfil a sua página permitida
    	
    	// Se for ADMIN, acessa os dados na pagina de credenciais, usuario/cadastro
    	if (us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
        		!us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod())) ) {
        		
        		return new ModelAndView("usuario/cadastro", "usuario", us);
        		
        	// Se for médico, acessa os dados na pagina medico/cadastro	
        	} else if (us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
        		
        		Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
        		
        		// Se esse médico não tem um Id, faz um insert.
        		// Se tem Id faz um update
        		return medico.hasNotId()
        				? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
        				: new ModelAndView("medico/cadastro", "medico", medico);
        	
        	// Se for paciente, não pode acessar os dados do usuário
        	} else if (us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
        		ModelAndView model = new ModelAndView("error");
        		// ModelAndView utiliza addObject e não addAttribute como ModelMap
        		model.addObject("status", 403);
        		model.addObject("error", "Área Restrita");
        		model.addObject("message", "Os dados de pacientes são restritos a ele.");
        		return model;
        	}
    	
    	return new ModelAndView("redirect:/u/lista");
    	
    }
    
    @GetMapping("/editar/senha")
    public String abrirEditarSenha() {
    	
    	return "usuario/editar-senha";
    }
    
    @PostMapping("/confirmar/senha")
    public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2, 
    						  @RequestParam("senha3") String s3, @AuthenticationPrincipal User user,
    						  RedirectAttributes attr) {
    	
    	if (!s1.equals(s2)) {
    		attr.addFlashAttribute("falha", "Senhas não conferem, tente novamente");
    		return "redirect:/u/editar/senha";
    	}
    	
    	Usuario u = service.buscarPorEmail(user.getUsername());
    	if(!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
    		attr.addFlashAttribute("falha", "Senha atual não confere, tente novamente");
    		return "redirect:/u/editar/senha";
    	}
    		
    	service.alterarSenha(u, s1);
    	attr.addFlashAttribute("sucesso", "Senha alterada com sucesso.");
    	return "redirect:/u/editar/senha";
    }
    
    // abrir pagina de novo cadastro de pacientes
    @GetMapping("/novo/cadastro")
    public String novoCadastro(Usuario usuario) {
    	
    	return "cadastrar-se";
    }
    
    // pagina de resposta do cadastro de pacientes
    @GetMapping("/cadastro/realizado")
    public String cadastroRealizado() {
    	
    	return "fragments/mensagem";
    }
    
    
    // recebe o form da pagina cadastrar-se
    @PostMapping("/cadastro/paciente/salvar")
    public String salvarCadastroPaciente(Usuario usuario, BindingResult result) {
    	
    	try {
    		service.salvarCadastroPaciente(usuario);
			
		} catch (DataIntegrityViolationException ex) {
			result.reject("email", "Ops... Este e-mail já existe na base de dados.");
			return "cadastrar-se";
			
		}
    	return "redirect:/u/cadastro/realizado";
    	
    }
    
    


}
