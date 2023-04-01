package com.andreitoledo.clinica.online.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.andreitoledo.clinica.online.domain.Agendamento;
import com.andreitoledo.clinica.online.domain.Horario;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long>{
	
	@Query("select h "
			+ "from Horario h "
			+ "where not exists("
				+ "select a.horario.id "
					+ "from Agendamento a "
					+ "where "
						+ "a.medico.id = :id and "
						+ "a.dataConsulta = :data and "
						+ "a.horario.id = h.id "
			+ ") "
			+ "order by h.horaMinuto asc")
	List<Horario> findByMedicoIdAndDataNotHorarioAgendado(Long id, LocalDate data);

}
