package com.andreitoledo.clinica.online.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.andreitoledo.clinica.online.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UsuarioService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		// acessos públicos liberados		
			.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
			.antMatchers("/", "/home").permitAll()
			
		// acessos privados admin
			.antMatchers("/u/**").hasAuthority("ADMIN")
						
		// acessos privados medicos			
			.antMatchers("/medicos/**").hasAuthority("MEDICO")
		
			
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
			.logoutSuccessUrl("/");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}	
	
	
	
	
}
