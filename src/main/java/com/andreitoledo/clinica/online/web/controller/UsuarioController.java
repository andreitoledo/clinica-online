package com.andreitoledo.clinica.online.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andreitoledo.clinica.online.domain.Usuario;

@Controller
@RequestMapping("u")
public class UsuarioController {
	
	// Abrir cadatro de usuario (medico/admin/paciente)
	@GetMapping("/novo/cadastro/usuario")
	public String CadastroPorAdminParaAdminMedicoPaciente(Usuario usurio) {
		
		return "usuario/cadastro";
	}

}
