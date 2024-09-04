package com.example.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.member.MemberService;

import lombok.RequiredArgsConstructor;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {
	
	private final MemberService memberService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
	    .authorizeHttpRequests(auth -> 
	        auth.requestMatchers("/", "/members/login", "/members/new", "/members/login/error").permitAll()
	        	.requestMatchers("/admin/**").hasRole("ADMIN")
	        	.requestMatchers("/members/**").hasRole("USER")
	            .anyRequest().authenticated())
	    .formLogin(formlogin -> 
	        formlogin.loginPage("/members/login")
	            .defaultSuccessUrl("/")
	            .usernameParameter("email")
	            .failureUrl("/members/login/error"))
	    .logout(logout -> 
	        logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
	            .logoutSuccessUrl("/"));

		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
					.userDetailsService(memberService)
					.passwordEncoder(passwordEncoder)
					.and()
					.build();
	}

}
