package com.example.demo.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.member.Member;
import com.example.demo.member.MemberFormDto;
import com.example.demo.member.MemberService;

import groovyjarjarantlr4.v4.parse.ANTLRParser.exceptionGroup_return;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class MemberControllerTest {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Test
	@DisplayName("로그인 성공 테스트")
	public void loginSuccessTest() throws Exception{
		String email = "test@email.com";
		String password = "1234";
		this.createMember(email, password);
		
	}
	
	
	
	
	private Member createMember(String email,String passowrd) {
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setEmail(email);
		memberFormDto.setAddress("서울");
		memberFormDto.setPassword(passowrd);
		memberFormDto.setName("홍길동");
		
		Member member = Member.createMember(memberFormDto, passwordEncoder);
		return memberService.saveMember(member);
	}

}
