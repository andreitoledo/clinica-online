package com.andreitoledo.clinica.online.repository.projection;

import com.andreitoledo.clinica.online.domain.Especialidade;
import com.andreitoledo.clinica.online.domain.Medico;
import com.andreitoledo.clinica.online.domain.Paciente;

public interface HistoricoPaciente {

	Long getId();
	
	Paciente getPaciente();
	
	String getDataConsulta();
	
	Medico getMedico();
	
	Especialidade getEspecialidade();
}
