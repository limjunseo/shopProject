package com.example.demo.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId="";
		
		if(authentication != null) {
			userId = authentication.getName();
		}
		return Optional.of(userId);
	}

}
