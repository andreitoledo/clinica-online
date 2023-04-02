package com.andreitoledo.clinica.online.web.Exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.andreitoledo.clinica.online.exception.AcessoNegadoException;

/*essa anotação serve como um ouvinte na aplicação. Fica esperando
que algumas dessas regras sejam verdadeiras, quando forem, essa classe
ativa o método.

As regras criadas aqui, são regras para capturar Exception/exceção.
*/

@ControllerAdvice
public class ExceptionController {
	
	// essa anotação define a exceção que queremos tratar no método.
	@ExceptionHandler(UsernameNotFoundException.class)
	public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
		ModelAndView model = new ModelAndView("error");
		model.addObject("status", 404);
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", ex.getMessage());
		return model;
	}
		
	@ExceptionHandler(AcessoNegadoException.class)
	public ModelAndView acessoNegadoException(AcessoNegadoException ex) {
		ModelAndView model = new ModelAndView("error");
		model.addObject("status", 403);
		model.addObject("error", "Operação não pode ser realizada.");
		model.addObject("message", ex.getMessage());
		return model;
	}

}
