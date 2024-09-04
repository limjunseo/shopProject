package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import com.example.demo.member.Member;
import com.example.demo.member.MemberFormDto;
import com.example.demo.member.MemberService;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(locations ="classpath:application-test.properties")
public class MemberServiceTest {
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	
	
	@Test
	@DisplayName("회원가입 테스트")
	public void saveMemberTest() {
		Member member = createMember();
		Member savedMember = memberService.saveMember(member);
		
		assertThat(member.getAddress()).isEqualTo(savedMember.getAddress());
		assertThat(member.getEmail()).isEqualTo(savedMember.getEmail());
		assertThat(member.getName()).isEqualTo(savedMember.getName());
		assertThat(member.getPassword()).isEqualTo(savedMember.getPassword());
		assertThat(member.getRole()).isEqualTo(savedMember.getRole());
	}
	
	@Test
	@DisplayName("중복 회원 가입 테스트")
	public void saveDuplicateMemberTest() {
		Member member1 = createMember();
		Member member2 = createMember();
		
		Member savedMember1 = memberService.saveMember(member1);
		Throwable e = assertThrows(IllegalStateException.class, () -> memberService.saveMember(member2));
		assertEquals("이미 가입된 회원", e.getMessage());
		
	}
	
	private Member createMember() {
		MemberFormDto memberFormDto = new MemberFormDto();
		memberFormDto.setEmail("test@email.com");
		memberFormDto.setName("홍길동");
		memberFormDto.setAddress("서울시 마포구 합정동");
		memberFormDto.setPassword("1234");
		return Member.createMember(memberFormDto, passwordEncoder);
	}

}
