package com.example.demo.member;

import java.util.Optional;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{
	private final MemberRepository memberRepository;

	public Member saveMember(Member member) {
		validateDuplicateMember(member);
		Member savedMember = memberRepository.save(member);
		return savedMember;
	}
	
	private void validateDuplicateMember(Member member) {
		Optional<Member> findMember = memberRepository.findByEmail(member.getEmail());
		if(!findMember.isEmpty()) {
			throw new DuplicateKeyException("이미 가입된 회원");
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Member member = memberRepository.findByEmail(email).orElseThrow(() ->  new UsernameNotFoundException(email + "유저가 없음"));
		
		return User.builder()
					.username(member.getName())
					.password(member.getPassword())
					.roles(member.getRole().toString())
					.build();
	}



}
