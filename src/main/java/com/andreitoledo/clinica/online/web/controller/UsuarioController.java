package com.andreitoledo.clinica.online.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.andreitoledo.clinica.online.domain.Usuario;
import com.andreitoledo.clinica.online.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
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

}
