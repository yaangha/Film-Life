package com.project.film.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

	@Bean // 필요한 곳에 의존성 주입!
	public PasswordEncoder passwordEncoder() { // 암호화 알고리즘 객체
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.formLogin().loginPage("/login").loginProcessingUrl("/login")
		.defaultSuccessUrl("/review/main")
		.usernameParameter("idName")
		.passwordParameter("password")
		.failureUrl("/login");
		
		http.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/review/main");
		
		return http.build();
	}
	
	
}
