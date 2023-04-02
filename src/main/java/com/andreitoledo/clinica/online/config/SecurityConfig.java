package com.andreitoledo.clinica.online.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.andreitoledo.clinica.online.domain.PerfilTipo;
import com.andreitoledo.clinica.online.service.UsuarioService;

// habilita o uso de anotações para a parte de segurança
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
    private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
    private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		// acessos públicos liberados		
			.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
			.antMatchers("/", "/home").permitAll()
			
		// acessos privados admin
			.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(PACIENTE, MEDICO)
			.antMatchers("/u/**").hasAuthority(ADMIN)
						
		// acessos privados medicos
			.antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(PACIENTE, MEDICO)
			.antMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/medicos/**").hasAuthority(MEDICO)
			
		// acessos privados pacientes
			.antMatchers("/pacientes/**").hasAuthority(PACIENTE)
		
		// acessos privados especialidades
			.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
			.antMatchers("/especialidades/**").hasAuthority(ADMIN)
			
		.anyRequest().fullyAuthenticated()
		.and()
		// configurando o login
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/", true)
			.failureUrl("/login-error")
			.permitAll()
		// configurando o logout
		.and()
			.logout()
			.logoutSuccessUrl("/")
		// configurando as mensagens para o tratamento de acesse negado
			.and()
			.exceptionHandling()
			.accessDeniedPage("/acesso-negado");
			
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}	
	
	
	
	
}
