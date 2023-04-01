package com.andreitoledo.clinica.online.web.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.andreitoledo.clinica.online.domain.Agendamento;
import com.andreitoledo.clinica.online.domain.Especialidade;
import com.andreitoledo.clinica.online.domain.Paciente;
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
		
		// busca os horarios livres, ou seja, sem agendamento
		@GetMapping("/horario/medico/{id}/data/{data}")
		public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
											 @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {
			
			return ResponseEntity.ok(service.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
		}
		
		// salvar um consulta agendada
		@PostMapping({"/salvar"})
		public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {
			Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
			String titulo = agendamento.getEspecialidade().getTitulo();
			Especialidade especialidade = especialidadeService
					.buscarPorTitulos(new String[] {titulo})
					.stream().findFirst().get();
			agendamento.setEspecialidade(especialidade);
			agendamento.setPaciente(paciente);
			service.salvar(agendamento);
			attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso.");
			return "redirect:/agendamentos/agendar";		
		}
		
}
