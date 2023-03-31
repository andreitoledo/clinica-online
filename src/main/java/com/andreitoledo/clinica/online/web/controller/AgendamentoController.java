package com.andreitoledo.clinica.online.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andreitoledo.clinica.online.domain.Agendamento;
import com.andreitoledo.clinica.online.service.AgendamentoService;
import com.andreitoledo.clinica.online.service.EspecialidadeService;
import com.andreitoledo.clinica.online.service.PacienteService;

@Controller
@RequestMapping("agendamentos")
public class AgendamentoController {
	
	@Autowired
	private AgendamentoService service;
	@Autowired
	private PacienteService pacienteService;
	@Autowired
	private EspecialidadeService especialidadeService;
	
	// abre a pagina de agendamento de consultas 
		@GetMapping({"/agendar"})
		public String agendarConsulta(Agendamento agendamento) {

			return "agendamento/cadastro";		
		}
		
}
