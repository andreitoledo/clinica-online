package com.andreitoledo.clinica.online.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andreitoledo.clinica.online.domain.Paciente;
import com.andreitoledo.clinica.online.domain.Usuario;
import com.andreitoledo.clinica.online.service.PacienteService;

@Controller
@RequestMapping("pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteService service;
	
	// abrir pagina de dados pessoais do paciente
	@GetMapping("/dados")
	public String cadastrar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		paciente = service.buscarPorUsuarioEmail(user.getUsername());
		if (paciente.hasNotId()) {
			paciente.setUsuario(new Usuario(user.getUsername()));
		}
		model.addAttribute("paciente", paciente);
		return "paciente/cadastro";
	}

}
